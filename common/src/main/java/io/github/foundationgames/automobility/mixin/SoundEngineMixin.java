package io.github.foundationgames.automobility.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.foundationgames.automobility.sound.AdvancedTickableSoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(method = "tickNonPaused",
            at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0,
                     target = "Lnet/minecraft/client/sounds/ChannelAccess$ChannelHandle;execute(Ljava/util/function/Consumer;)V"))
    private void automobility$exposeALSourceToSound(CallbackInfo ci,
                                                    @Local(ordinal = 0) TickableSoundInstance sound,
                                                    @Local(ordinal = 0) ChannelAccess.ChannelHandle channelHandle) {
        if (sound instanceof AdvancedTickableSoundInstance adv) {
            channelHandle.execute(ch -> {
                int source = ((SoundChannelAccess) ch).automobility$getSource();
                adv.updateALState(source);
            });
        }
    }
}
