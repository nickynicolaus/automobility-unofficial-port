package io.github.milkucha.momentum.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MomentumConfigTest {
    @AfterEach
    void clearServerConfig() {
        MomentumConfig.clearServerGameplayConfig();
    }

    @Test
    void sanitizeRepairsUnsafeNumericValues() {
        var config = new MomentumConfig();
        config.movement.accelerationScale = 0f;
        config.barHud.maxSpeedKmh = Float.NaN;
        config.barHud.barWidth = 0;
        config.responsiveDrift.slipConvergeRate = Float.POSITIVE_INFINITY;
        config.sound.enginePitchCeiling = 0f;

        config.sanitize();

        assertTrue(config.movement.accelerationScale > 0f);
        assertTrue(Float.isFinite(config.barHud.maxSpeedKmh));
        assertEquals(1, config.barHud.barWidth);
        assertTrue(Float.isFinite(config.responsiveDrift.slipConvergeRate));
        assertTrue(config.sound.enginePitchCeiling > 0f);
    }

    @Test
    void sanitizeRestoresMissingGroups() {
        var config = new MomentumConfig();
        config.movement = null;
        config.barHud = null;
        config.oDrift = null;

        config.sanitize();

        assertTrue(config.movement.accelerationScale > 0f);
        assertTrue(config.barHud.maxSpeedKmh > 0f);
        assertEquals(MomentumConfig.ODrift.Profile.RESPONSIVE, config.oDrift.profile);
    }

    @Test
    void serverGameplayConfigOverridesPhysicsValues() {
        assertTrue(MomentumConfig.applyServerGameplayConfig("""
                {
                  "movement": {"accelerationScale": 7.25},
                  "oDrift": {"profile": "ARCADE"}
                }
                """));

        assertEquals(7.25f, MomentumConfig.gameplay().movement.accelerationScale);
        assertEquals(MomentumConfig.ODrift.Profile.ARCADE, MomentumConfig.gameplay().oDrift.profile);
    }
}
