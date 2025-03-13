package io.github.foundationgames.automobility.mixin;

import com.mojang.blaze3d.audio.Channel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Channel.class)
public interface SoundChannelAccess {
    @Accessor("source")
    int automobility$getSource();
}
