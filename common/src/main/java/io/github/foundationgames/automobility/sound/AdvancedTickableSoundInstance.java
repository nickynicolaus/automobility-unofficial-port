package io.github.foundationgames.automobility.sound;

import net.minecraft.client.resources.sounds.TickableSoundInstance;

public interface AdvancedTickableSoundInstance extends TickableSoundInstance {
    void updateALState(int source);
}
