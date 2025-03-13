package io.github.foundationgames.automobility.sound;

import io.github.foundationgames.automobility.automobile.HornSoundDefinition;
import io.github.foundationgames.automobility.entity.AutomobileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public abstract class SlicedLoopingAutomobileSoundInstance extends AutomobileSoundInstance implements AdvancedTickableSoundInstance {
    public static final float TICK_LENGTH = 1f / 20;

    public final float loopStart;
    public final float loopEnd;
    protected boolean continueSlicedLoop = false;

    public SlicedLoopingAutomobileSoundInstance(SoundEvent sound, Minecraft client, AutomobileEntity automobile, float loopStart, float loopEnd) {
        super(sound, client, automobile);
        this.loopStart = loopStart;
        this.loopEnd = loopEnd;
        this.looping = false;
    }

    @Override
    public void updateALState(int source) {
        int buffer = AL10.alGetSourcei(source, AL10.AL_BUFFER);

        int size = AL10.alGetBufferi(buffer, AL10.AL_SIZE);
        int channels = AL10.alGetBufferi(buffer, AL10.AL_CHANNELS);
        int bits = AL10.alGetBufferi(buffer, AL10.AL_BITS);
        int freq = AL10.alGetBufferi(buffer, AL10.AL_FREQUENCY);

        float duration = (float) (size * 8) / (channels * bits * freq);

        float lStart = loopStart;
        float lEnd = loopEnd;
        boolean mayLoop = lStart > 0 && lEnd > 0;

        lStart = Math.max(0, lStart);
        lEnd = Math.min(duration, lEnd);

        if (mayLoop) {
            float os = AL10.alGetSourcef(source, AL11.AL_SEC_OFFSET);
            if (continueSlicedLoop) {
                if (os > lEnd - TICK_LENGTH) {
                    AL10.alSourcef(source, AL11.AL_SEC_OFFSET, lStart);
                }
            } else {
                if (os < lStart) {
                    AL10.alSourcef(source, AL11.AL_SEC_OFFSET, Math.clamp(duration - lStart, lEnd, duration));
                } else if (os < lEnd) {
                    AL10.alSourcef(source, AL11.AL_SEC_OFFSET, lEnd);
                }
            }
        }
    }

    public static class HornSound extends SlicedLoopingAutomobileSoundInstance {
        private final float hornPitch;

        public HornSound(Minecraft client, AutomobileEntity automobile, HornSoundDefinition hornSound, float pitch) {
            super(hornSound.sound().get(), client, automobile, hornSound.loopStart(), hornSound.loopEnd());
            this.hornPitch = pitch;
            this.continueSlicedLoop = true;
        }

        @Override
        protected boolean canPlay(AutomobileEntity automobile) {
            return !isStopped();
        }

        @Override
        public void tick() {
            super.tick();

            if (!this.automobile.honking()) {
                this.continueSlicedLoop = false;
            }
        }

        @Override
        protected float getPitch(AutomobileEntity automobile) {
            return hornPitch;
        }

        @Override
        protected float getVolume(AutomobileEntity automobile) {
            return 2;
        }
    }
}
