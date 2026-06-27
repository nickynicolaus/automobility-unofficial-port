package io.github.milkucha.momentum.hud;

import io.github.foundationgames.automobility.entity.AutomobileEntity;
import io.github.milkucha.momentum.MomentumCruiseControl;
import io.github.milkucha.momentum.accessor.SteeringDebugAccessor;
import io.github.milkucha.momentum.config.MomentumConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * BarHud - minimal procedural velocimeter.
 *
 * Renders a horizontal row of thin vertical bar segments that fill left-to-right
 * proportional to speed. No textures - drawn entirely with fill() and drawText().
 *
 * Layout tuned under the "barHud" config section.
 *
 * Layout maths:
 *   numBars = floor((totalWidth + barSpacing) / (barWidth + barSpacing))
 *   filledBars = round(clamp(speedKmh / maxSpeedKmh, 0, 1) * numBars)
 *
 * With defaults (totalWidth=90, barWidth=5, barSpacing=2):
 *   numBars = (90 + 2) / (5 + 2) = 13 segments
 */
public class BarHud {

    private static final double TO_KMH = 72.0;

    // Debug panel colors
    private static final int COL_PANEL_BG   = 0xAA000000;
    private static final int COL_PANEL_EDGE = 0xFF444444;
    private static final Identifier CRUISE_ICON = Identifier.fromNamespaceAndPath("momentum", "textures/gui/cruise_control.png");
    private static final int CRUISE_ICON_TEXTURE_SIZE = 64;
    private static final int CRUISE_ICON_DISPLAY_SIZE = 16;

    public static void render(GuiGraphicsExtractor graphics, float tickDelta) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;

        Entity vehicle = client.player.getVehicle();
        if (!(vehicle instanceof AutomobileEntity auto)) return;
        if (client.screen != null) return;

        MomentumConfig cfg = MomentumConfig.get();
        MomentumConfig.BarHud b = cfg.barHud;
        boolean cruiseActive = MomentumCruiseControl.isActiveFor(auto);
        int cruiseColor = MomentumCruiseControl.isAccelerating()
                ? cfg.cruise.acceleratingColor
                : cfg.cruise.coastColor;
        int barColor = cruiseActive ? cruiseColor : b.barColor;
        int textColor = cruiseActive ? cruiseColor : b.textColor;

        int screenW = client.getWindow().getGuiScaledWidth();
        int screenH = client.getWindow().getGuiScaledHeight();

        int originX = b.x >= 0 ? b.x : screenW - b.totalWidth - (int)(screenW * b.xFraction);
        int originY = b.y >= 0 ? b.y : screenH - b.totalHeight - b.marginBottom;

        double speedKmh = auto.getEffectiveSpeed() * TO_KMH;

        // Separate engine vs boost contributions so boost bars can be coloured differently.
        // engineSpeed is the base; hSpeed = engineSpeed + boostSpeed.
        double engineKmh = (auto instanceof SteeringDebugAccessor acc)
                ? acc.momentum$getEngineSpeed() * TO_KMH
                : speedKmh;
        engineKmh = Math.max(0.0, engineKmh); // clamp: engineSpeed can go negative in reverse

        // How many segments fit and how many should be lit
        int numBars         = (b.totalWidth + b.barSpacing) / (b.barWidth + b.barSpacing);
        int engineBars      = Math.max(1, (int) Math.round(Math.min(engineKmh / b.maxSpeedKmh, 1.0) * numBars));
        int totalFilledBars = Math.max(1, (int) Math.round(Math.min(speedKmh  / b.maxSpeedKmh, 1.0) * numBars));

        for (int i = 0; i < totalFilledBars; i++) {
            int bx    = originX + i * (b.barWidth + b.barSpacing);
            int color = i < engineBars ? barColor : b.boostBarColor;
            graphics.fill(bx, originY, bx + b.barWidth, originY + b.totalHeight, color);
        }

        // Speed text - positioned relative to bar origin via textOffsetX/Y
        String speedStr = String.format("%.0f km/h", speedKmh);
        int textX = originX + b.textOffsetX;
        int textY = originY + b.textOffsetY;
        graphics.text(client.font, Component.literal(speedStr),
                textX,
                textY,
                textColor);

        if (cruiseActive) {
            int iconX = textX + client.font.width(speedStr) + 5;
            int iconY = textY - 4;
            drawCruiseIconFallback(graphics, iconX, iconY, CRUISE_ICON_DISPLAY_SIZE);
            graphics.blit(RenderPipelines.GUI_TEXTURED, CRUISE_ICON, iconX, iconY, 0, 0,
                    CRUISE_ICON_DISPLAY_SIZE, CRUISE_ICON_DISPLAY_SIZE,
                    CRUISE_ICON_TEXTURE_SIZE, CRUISE_ICON_TEXTURE_SIZE);
            graphics.text(client.font, Component.literal(String.format("%.0f", MomentumCruiseControl.getTargetKmh())),
                    iconX + CRUISE_ICON_DISPLAY_SIZE + 3,
                    textY,
                    cruiseColor);
        }
    }

    // ── Debug overlay ─────────────────────────────────────────────────────────

    public static void renderDebug(GuiGraphicsExtractor graphics, float tickDelta) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;
        Entity vehicle = client.player.getVehicle();
        if (!(vehicle instanceof AutomobileEntity auto)) return;
        if (client.screen != null) return;

        MomentumConfig cfg = MomentumConfig.get();
        if (!cfg.barHud.debug) return;
        if (!(auto instanceof SteeringDebugAccessor dbg)) return;

        float   steering  = dbg.momentum$getSteering();
        float   hSpd      = dbg.momentum$getHSpeed();
        float   angSpd    = dbg.momentum$getAngularSpeed();
        float   engSpd    = dbg.momentum$getEngineSpeed();
        boolean drifting  = dbg.momentum$isDrifting();
        boolean onGround  = dbg.momentum$isOnGround();
        boolean cruise    = MomentumCruiseControl.isActiveFor(auto);

        List<String>  texts  = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        row(texts, colors, "GENERAL",                                           0xFF999999);
        row(texts, colors, String.format("steer:  %+.3f", steering),            0xFFFFFF55);
        row(texts, colors, String.format("hSpd:   %.3f",  hSpd),                0xFF55FFFF);
        row(texts, colors, String.format("engSpd: %.3f",  engSpd),              0xFFAAAAAA);
        row(texts, colors, String.format("angSpd: %+.3f", angSpd),              0xFFFF55FF);
        row(texts, colors, "ground: " + yn(onGround),     onGround ? 0xFF55FF55 : 0xFF999999);
        row(texts, colors, "drift:  " + yn(drifting),     drifting ? 0xFF55FF55 : 0xFF999999);
        row(texts, colors, String.format("cruise: %s %.0f", yn(cruise), MomentumCruiseControl.getTargetKmh()),
                cruise ? MomentumConfig.get().cruise.activeColor : 0xFF999999);

        int lineH  = 9;
        int padX   = 6;
        int padY   = 4;
        int dbgW   = 152;
        int dbgH   = padY * 2 + texts.size() * lineH;

        int screenW = client.getWindow().getGuiScaledWidth();
        int dbgX = cfg.barHud.debugX >= 0
                ? cfg.barHud.debugX
                : screenW - dbgW - (int)(screenW * cfg.barHud.debugXFraction);
        int dbgY = cfg.barHud.debugY;

        drawPanel(graphics, dbgX, dbgY, dbgW, dbgH);
        for (int i = 0; i < texts.size(); i++) {
            graphics.text(client.font, Component.literal(texts.get(i)),
                    dbgX + padX, dbgY + padY + i * lineH, colors.get(i));
        }
    }

    private static void row(List<String> texts, List<Integer> colors, String text, int color) {
        texts.add(text);
        colors.add(color);
    }

    private static String yn(boolean v) { return v ? "YES" : "no"; }

    private static void drawCruiseIconFallback(GuiGraphicsExtractor g, int x, int y, int size) {
        int unit = Math.max(1, size / 16);
        int rim = 0xFFBFC7C9;
        int shadow = 0xFF101414;
        int green = 0xFF39FF4F;
        int greenDark = 0xFF158A28;

        rect(g, x, y, unit, 2, 11, 1, 1, rim);
        rect(g, x, y, unit, 1, 3, 1, 1, rim);
        rect(g, x, y, unit, 12, 3, 1, 1, rim);
        rect(g, x, y, unit, 0, 5, 2, 6, shadow);
        rect(g, x, y, unit, 14, 5, 2, 6, shadow);
        rect(g, x, y, unit, 3, 1, 2, 2, shadow);
        rect(g, x, y, unit, 6, 0, 4, 2, shadow);
        rect(g, x, y, unit, 11, 1, 2, 2, shadow);
        rect(g, x, y, unit, 2, 4, 2, 7, shadow);
        rect(g, x, y, unit, 12, 4, 2, 7, shadow);
        rect(g, x, y, unit, 4, 2, 8, 3, shadow);
        rect(g, x, y, unit, 5, 5, 6, 1, rim);
        rect(g, x, y, unit, 7, 6, 2, 5, rim);
        rect(g, x, y, unit, 8, 10, 6, 2, green);
        rect(g, x, y, unit, 11, 7, 2, 8, green);
        rect(g, x, y, unit, 13, 9, 2, 4, green);
        rect(g, x, y, unit, 7, 12, 6, 2, greenDark);
    }

    private static void rect(GuiGraphicsExtractor g, int x, int y, int unit, int rx, int ry, int rw, int rh, int color) {
        g.fill(x + rx * unit, y + ry * unit, x + (rx + rw) * unit, y + (ry + rh) * unit, color);
    }

    private static void drawPanel(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        g.fill(x, y, x + w, y + h, COL_PANEL_BG);
        g.fill(x,         y,         x + w,     y + 1,     COL_PANEL_EDGE); // top
        g.fill(x,         y + h - 1, x + w,     y + h,     COL_PANEL_EDGE); // bottom
        g.fill(x,         y,         x + 1,     y + h,     COL_PANEL_EDGE); // left
        g.fill(x + w - 1, y,         x + w,     y + h,     COL_PANEL_EDGE); // right
    }
}
