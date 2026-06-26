package io.github.milkucha.momentum;

import io.github.foundationgames.automobility.entity.AutomobileEntity;
import io.github.milkucha.momentum.accessor.SteeringDebugAccessor;
import io.github.milkucha.momentum.config.MomentumConfig;

public final class MomentumCruiseControl {
    private static final double TO_KMH = 72.0;

    private static boolean active = false;
    private static int entityId = -1;
    private static float targetKmh = 0f;
    private static float lastSpeedKmh = 0f;
    private static boolean accelerating = false;

    private MomentumCruiseControl() {
    }

    public static boolean toggle(AutomobileEntity auto) {
        if (!MomentumConfig.get().enabled || !MomentumConfig.get().cruise.enabled) {
            deactivate();
            return false;
        }

        if (active && entityId == auto.getId()) {
            deactivate();
            return false;
        }

        if (isReversing(auto)) {
            deactivate();
            return false;
        }

        MomentumConfig.Cruise cruise = MomentumConfig.get().cruise;
        float speed = currentKmh(auto);
        if (speed < cruise.minActivationKmh) {
            deactivate();
            return false;
        }

        active = true;
        entityId = auto.getId();
        targetKmh = clamp(speed, cruise.minActivationKmh, cruise.maxTargetKmh);
        lastSpeedKmh = speed;
        accelerating = false;
        return true;
    }

    public static AdjustedInput adjustInput(AutomobileEntity auto, boolean fwd, boolean back, boolean left, boolean right, boolean space, boolean ctrl) {
        if (!active) {
            return null;
        }
        if (!MomentumConfig.get().enabled || !MomentumConfig.get().cruise.enabled || entityId != auto.getId()) {
            deactivate();
            return null;
        }

        if (back || space || MomentumBrakeState.brakeHeld || MomentumDriftState.driftKeyHeld || isReversing(auto)) {
            deactivate();
            return null;
        }

        MomentumConfig.Cruise cruise = MomentumConfig.get().cruise;
        float speed = currentKmh(auto);

        if (lastSpeedKmh >= cruise.impactCancelMinSpeedKmh
                && lastSpeedKmh - speed >= cruise.impactCancelDropKmh) {
            deactivate();
            return null;
        }

        if (fwd) {
            targetKmh = clamp(Math.max(targetKmh, speed), cruise.minActivationKmh, cruise.maxTargetKmh);
            accelerating = true;
        } else if (accelerating) {
            if (speed >= targetKmh - cruise.cutThrottleBelowTargetKmh) {
                accelerating = false;
            }
        } else if (speed <= targetKmh - cruise.resumeThrottleBelowTargetKmh) {
            accelerating = true;
        }

        lastSpeedKmh = speed;
        return new AdjustedInput(accelerating, false, left, right, false, ctrl);
    }

    public static void cancelOnImpact(AutomobileEntity auto, float speedKmh) {
        if (active && auto != null && entityId == auto.getId()
                && speedKmh >= MomentumConfig.get().cruise.impactCancelMinSpeedKmh) {
            deactivate();
        }
    }

    public static void deactivate() {
        active = false;
        entityId = -1;
        targetKmh = 0f;
        lastSpeedKmh = 0f;
        accelerating = false;
    }

    public static boolean isActive() {
        return active;
    }

    public static boolean isActiveFor(AutomobileEntity auto) {
        return active && auto != null && entityId == auto.getId();
    }

    public static float getTargetKmh() {
        return targetKmh;
    }

    public static boolean isAccelerating() {
        return active && accelerating;
    }

    public static float currentKmh(AutomobileEntity auto) {
        return (float) (auto.getEffectiveSpeed() * TO_KMH);
    }

    private static boolean isReversing(AutomobileEntity auto) {
        return auto instanceof SteeringDebugAccessor accessor && accessor.momentum$getEngineSpeed() < -0.02f;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public record AdjustedInput(boolean fwd, boolean back, boolean left, boolean right, boolean space, boolean ctrl) {
    }
}
