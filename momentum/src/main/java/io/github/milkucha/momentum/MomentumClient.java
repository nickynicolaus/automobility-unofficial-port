package io.github.milkucha.momentum;

import io.github.foundationgames.automobility.entity.AutomobileEntity;
import io.github.milkucha.momentum.accessor.SteeringDebugAccessor;
import io.github.milkucha.momentum.config.MomentumConfig;
import io.github.milkucha.momentum.hud.BarHud;
import io.github.milkucha.momentum.network.KeyStatePacket;
import io.github.milkucha.momentum.sound.BrakingSkidSound;
import io.github.milkucha.momentum.sound.ArcadeDriftSkidSound;
import io.github.milkucha.momentum.sound.ResponsiveDriftSkidSound;
import io.github.milkucha.momentum.sound.VanillaDriftSkidSound;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class MomentumClient implements ClientModInitializer {
    private static final KeyMapping.Category MOMENTUM_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("momentum", "momentum"));

    // ── Registered KeyBindings ────────────────────────────────────────────────
    // NOTE: Brake and Drift keys are NOT registered as Minecraft KeyBindings.
    // They are stored in MomentumConfig as GLFW key codes (brakeKey / driftKey).
    // This avoids Fabric's key conflict suppression in multiplayer - registering
    // them on Space/S would interfere with vanilla Jump and Move Backwards.

    // ── Tick state ────────────────────────────────────────────────────────────

    private boolean prevBrakeHeld        = false;
    private boolean prevVanillaDriftActive = false;
    private boolean prevArcadeDriftActive  = false;
    private boolean prevResponsiveDriftActive = false;
    private float   kCameraDriftYawOffset  = 0f;
    private float   mCameraDriftYawOffset  = 0f;
    private float   steeringTiltOffset     = 0f;
    private float   reverseYawOffset       = 0f;
    private float   brakeZoomVelocity      = 0f;
    private float   prevHSpeed             = 0f;

    // Last key-state snapshot sent to the server; used to send only on changes.
    private boolean pktBrake = false;
    private boolean pktDrift = false;

    @Override
    public void onInitializeClient() {
        MomentumConfig.get();

        // ── KeyBinding registrations ──────────────────────────────────────────
        // These appear in Minecraft's vanilla Controls screen under "Momentum".

        KeyMapping reloadKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.momentum.reload_config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_F6,
                MOMENTUM_CATEGORY
        ));

        final KeyMapping openOptionsKey;
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
            openOptionsKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                    "key.momentum.open_options",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_PERIOD,
                    MOMENTUM_CATEGORY
            ));
        } else {
            openOptionsKey = null;
        }

        // ── HUD rendering ─────────────────────────────────────────────────────

        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, Identifier.fromNamespaceAndPath("momentum", "bar_hud"), (drawContext, tickCounter) -> {
            MomentumConfig cfg = MomentumConfig.get();
            if (!cfg.enabled) return;
            if (!cfg.barHud.enabled) return;
            float tickDelta = tickCounter.getGameTimeDeltaPartialTick(true);
            BarHud.render(drawContext, tickDelta);
            BarHud.renderDebug(drawContext, tickDelta);
        });

        // ── Key polling ───────────────────────────────────────────────────────
        // Poll the KeyBinding held state before entity ticks so both client and
        // server entities read the correct values this frame.

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null && client.player.getVehicle() instanceof AutomobileEntity) {
                long win = client.getWindow().handle();
                MomentumConfig mc = MomentumConfig.get();
                MomentumBrakeState.brakeHeld    = isKeyHeld(mc.brakeKey, win);
                MomentumDriftState.driftKeyHeld = isKeyHeld(mc.driftKey, win);
            } else {
                MomentumBrakeState.brakeHeld    = false;
                MomentumDriftState.driftKeyHeld = false;
            }

            // Send key state to server whenever any value changes.
            boolean nb = MomentumBrakeState.brakeHeld;
            boolean nd = MomentumDriftState.driftKeyHeld;
            if (client.getConnection() != null
                    && (nb != pktBrake || nd != pktDrift)) {
                pktBrake = nb; pktDrift = nd;
                ClientPlayNetworking.send(new KeyStatePacket(nb, nd));
            }
        });

        // ── Per-tick effects ──────────────────────────────────────────────────

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (reloadKey.consumeClick() && client.player != null) {
                MomentumConfig.reload();
                client.player.sendOverlayMessage(Component.literal("[Momentum] Config reloaded"));
            }
            if (openOptionsKey != null && openOptionsKey.consumeClick()) {
                client.execute(() -> {
                    try {
                        var screenClass = Class.forName("io.github.milkucha.momentum.config.MomentumConfigScreen");
                        var create = screenClass.getMethod("create", net.minecraft.client.gui.screens.Screen.class);
                        client.setScreen((net.minecraft.client.gui.screens.Screen) create.invoke(null, client.screen));
                    } catch (ReflectiveOperationException | LinkageError e) {
                        if (client.player != null) {
                            client.player.sendOverlayMessage(Component.literal("[Momentum] Config screen unavailable"));
                        }
                    }
                });
            }

            if (client.player != null && client.player.getVehicle() instanceof AutomobileEntity auto) {
                MomentumConfig cfg = MomentumConfig.get();
                SteeringDebugAccessor accessor = (SteeringDebugAccessor) auto;

                if (!cfg.enabled) {
                    resetTickState();
                    return;
                }

                // Independent camera lerp for Arcade and Responsive drift.
                boolean arcadeDriftCamActive    = accessor.momentum$isArcadeDriftActive();
                boolean responsiveDriftCamActive = accessor.momentum$isResponsiveDriftActive();

                float kTarget = cfg.arcadeDrift.cameraEnabled
                        ? accessor.momentum$getArcadeDriftOffset() * cfg.arcadeDrift.cameraScale : 0f;
                float mTarget = cfg.responsiveDrift.cameraEnabled
                        ? accessor.momentum$getResponsiveDriftOffset() * cfg.responsiveDrift.cameraScale : 0f;

                kCameraDriftYawOffset += (kTarget - kCameraDriftYawOffset)
                        * (arcadeDriftCamActive ? cfg.arcadeDrift.cameraLerpIn : cfg.arcadeDrift.cameraLerpOut);
                mCameraDriftYawOffset += (mTarget - mCameraDriftYawOffset)
                        * (responsiveDriftCamActive ? cfg.responsiveDrift.cameraLerpIn : cfg.responsiveDrift.cameraLerpOut);

                // Steering tilt: subtle yaw lean toward the turn direction.
                float tiltTarget = cfg.camera.enabled
                        ? accessor.momentum$getSteering() * cfg.camera.steeringTilt : 0f;
                steeringTiltOffset += (tiltTarget - steeringTiltOffset) * cfg.camera.steeringTiltLerp;

                // Reverse camera: lerp to 180° offset when engine speed is negative.
                boolean reversing = cfg.camera.enabled && cfg.camera.reverseFlip
                        && accessor.momentum$getEngineSpeed() < -0.01f;
                float reverseTarget = reversing ? 180f : 0f;
                reverseYawOffset += (reverseTarget - reverseYawOffset) * cfg.camera.reverseFlipLerp;

                if (cfg.camera.enabled && cfg.camera.lock) {
                    client.player.setYRot(auto.getYRot() + kCameraDriftYawOffset + mCameraDriftYawOffset + steeringTiltOffset + reverseYawOffset);
                    client.player.setXRot(cfg.camera.pitch);
                }

                // Brake skid sound
                boolean brakeHeld = MomentumBrakeState.brakeHeld;
                if (brakeHeld && !prevBrakeHeld) {
                    client.getSoundManager().play(new BrakingSkidSound(auto));
                }
                prevBrakeHeld = brakeHeld;

                // Brake zoom - spring-damper driven by deceleration.
                float hSpd = accessor.momentum$getHSpeed();
                float decel = Math.max(0f, prevHSpeed - hSpd);
                prevHSpeed = hSpd;
                if (cfg.camera.enabled) {
                    float inputForce = decel * cfg.camera.brakeZoomInputScale;
                    brakeZoomVelocity = brakeZoomVelocity * cfg.camera.brakeZoomDamping
                        + inputForce
                        - cfg.camera.brakeZoomSpring * MomentumBrakeState.brakeZoomOffset;
                    MomentumBrakeState.brakeZoomOffset += brakeZoomVelocity;
                    if (MomentumBrakeState.brakeZoomOffset > cfg.camera.brakeZoomFov) {
                        MomentumBrakeState.brakeZoomOffset = cfg.camera.brakeZoomFov;
                        brakeZoomVelocity = Math.min(brakeZoomVelocity, 0f);
                    } else if (MomentumBrakeState.brakeZoomOffset < 0f) {
                        MomentumBrakeState.brakeZoomOffset = 0f;
                        brakeZoomVelocity = Math.max(brakeZoomVelocity, 0f);
                    }
                } else {
                    brakeZoomVelocity = 0f;
                    MomentumBrakeState.brakeZoomOffset = 0f;
                }

                // Drift skid sounds - one per drift type, triggered on rising edge of active state
                boolean vanillaActive = accessor.momentum$isDrifting();
                if (vanillaActive && !prevVanillaDriftActive) {
                    client.getSoundManager().play(new VanillaDriftSkidSound(auto));
                }
                prevVanillaDriftActive = vanillaActive;

                boolean arcadeActive = accessor.momentum$isArcadeDriftActive();
                if (arcadeActive && !prevArcadeDriftActive) {
                    client.getSoundManager().play(new ArcadeDriftSkidSound(auto));
                }
                prevArcadeDriftActive = arcadeActive;

                boolean responsiveActive = accessor.momentum$isResponsiveDriftActive();
                if (responsiveActive && !prevResponsiveDriftActive) {
                    client.getSoundManager().play(new ResponsiveDriftSkidSound(auto));
                }
                prevResponsiveDriftActive = responsiveActive;

            } else {
                resetTickState();
            }
        });
    }

    private void resetTickState() {
        prevBrakeHeld             = false;
        prevVanillaDriftActive    = false;
        prevArcadeDriftActive     = false;
        prevResponsiveDriftActive = false;
        kCameraDriftYawOffset              = 0f;
        mCameraDriftYawOffset              = 0f;
        steeringTiltOffset                 = 0f;
        reverseYawOffset                   = 0f;
        brakeZoomVelocity                  = 0f;
        prevHSpeed                         = 0f;
        MomentumBrakeState.brakeZoomOffset = 0f;
    }

    /**
     * Returns true if the given GLFW key code is currently held down.
     * Reads raw GLFW state - does not consume key events or interfere with
     * vanilla key bindings (Jump, Move Backwards, etc.).
     */
    private static boolean isKeyHeld(int glfwKeyCode, long windowHandle) {
        return glfwKeyCode != GLFW.GLFW_KEY_UNKNOWN
                && GLFW.glfwGetKey(windowHandle, glfwKeyCode) == GLFW.GLFW_PRESS;
    }
}
