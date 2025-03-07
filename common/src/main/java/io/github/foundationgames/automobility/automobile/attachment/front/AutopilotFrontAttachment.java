package io.github.foundationgames.automobility.automobile.attachment.front;

import io.github.foundationgames.automobility.automobile.attachment.FrontAttachmentType;
import io.github.foundationgames.automobility.block.AutopilotSignBlock;
import io.github.foundationgames.automobility.entity.AutomobileEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class AutopilotFrontAttachment extends FrontAttachment {
    public static final int MAX_HEADING_COMMAND_TIME = 6000;

    private @Nullable AutopilotSignBlock.Heading currentHeading = null;
    private int headingTimeLimit = 0;
    private int animationTimer = 0;

    private Vec3 lastAutoPos;
    double lastHeadingToPathDir = 0;

    public AutopilotFrontAttachment(FrontAttachmentType<?> type, AutomobileEntity automobile) {
        super(type, automobile);

        lastAutoPos = automobile.position();
    }

    @Override
    public boolean canDrive(Entity entity) {
        return false;
    }

    @Override
    public boolean isProvidingAlternativeInputs(AutomobileEntity automobile, @Nullable Entity driver) {
        return true;
    }

    @Override
    public void provideAlternativeInputs(AutomobileEntity automobile, AutomobileEntity.Input input, @Nullable Entity driver) {
        if (currentHeading == null) {
            input.clearInputs();
        } else {
            var autoPos = autoPos();
            var autoMovement = automobile.position().subtract(lastAutoPos);

            var pathToPath = currentHeading.pathToPath(autoPos);
            double distToPath = pathToPath.length();

            var dirToPath = pathToPath.normalize();
            double autoSpeedIntoPath = currentHeading.stop() ? autoMovement.length() : Math.max(0, -autoMovement.dot(dirToPath));

            double favorDirToPath = Math.clamp(distToPath * 0.1, 0, 1);

            if (currentHeading.stop() && currentHeading.origin().distanceToSqr(autoPos) < 25 + 100 * autoMovement.lengthSqr()) {
                input.accelerating = false;
                input.braking = autoSpeedIntoPath > distToPath * 0.2;
                favorDirToPath = Math.sqrt(favorDirToPath);
            } else {
                input.accelerating = autoSpeedIntoPath * autoSpeedIntoPath * distToPath * distToPath < 6;
                input.braking = false;
            }

            var dirAlongPath = currentHeading.dir();

            var dirHeading = automobile.getLookAngle().toVector3f().mul(1, 0, 1);
            var dirDesiredHeading = dirAlongPath.lerp(dirToPath, favorDirToPath).normalize().toVector3f().mul(1, 0, 1);

            double headingToPathDir = dirHeading.angleSigned(dirDesiredHeading, new Vector3f(0, 1, 0)) / Mth.HALF_PI;
            double dHeadingToPathDir = headingToPathDir - this.lastHeadingToPathDir;
            input.steering = (float) Math.clamp(2 * headingToPathDir, -1, 1);

            float damp = (float) Mth.clamp(Math.abs(distToPath) + autoSpeedIntoPath, 0, 1);
            input.steering *= damp * damp;

            this.lastHeadingToPathDir = headingToPathDir;
        }

        lastAutoPos = automobile.position();
    }

    protected Vec3 autoPos() {
        return automobile().position().add(0, 0.5, 0);
    }

    public void notifyHeadingCommand(AutopilotSignBlock.Heading heading) {
        var autoPos = autoPos();
        if (heading.inFrontOfLimitPlane(autoPos) && heading.withinReasonableDistance(autoPos)) {
            if (currentHeading != null) {
                if (currentHeading.origin().distanceToSqr(autoPos) < heading.origin().distanceToSqr(autoPos)) {
                    return;
                }
            }

            currentHeading = heading;
            headingTimeLimit = MAX_HEADING_COMMAND_TIME;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!world().isClientSide()) {
            if (currentHeading != null && !currentHeading.withinReasonableDistance(autoPos())) {
                currentHeading = null;
            }

            float anim = 0;
            if (this.currentHeading != null) {
                anim = this.currentHeading.stop() ? 2 : 1;
            }

            this.updateTrackedAnimation(anim);

            if (this.headingTimeLimit > 0) {
                this.headingTimeLimit--;

                if (this.headingTimeLimit <= 0) {
                    this.currentHeading = null;
                }
            }
        }

        this.animationTimer++;

        if (this.animationTimer >= 60) {
            this.animationTimer = 0;
        }
    }

    @Override
    public void writeNbt(CompoundTag nbt, HolderLookup.Provider registry) {
        super.writeNbt(nbt, registry);

        if (this.currentHeading != null) {
            nbt.put("Command", this.currentHeading.toNbt());
        }

        nbt.putInt("timeout", this.headingTimeLimit);
    }

    @Override
    public void readNbt(CompoundTag nbt, HolderLookup.Provider reg) {
        super.readNbt(nbt, reg);

        if (nbt.contains("Command")) {
            this.currentHeading = AutopilotSignBlock.Heading.fromNbt(nbt.getCompound("Command"));
        } else {
            this.currentHeading = null;
        }

        this.headingTimeLimit = nbt.getInt("timeout");
    }

    public int getAnimationTimer() {
        return animationTimer;
    }

    public State getState() {
        int anim = Math.clamp((int) animation(), 0, State.values().length);
        return State.values()[anim];
    }

    public enum State {
        IDLE(0xfff8e0, 0xffae00, 15, 0),
        GO(0xe8fffb, 0x00ffd5, 12, 4),
        HALT(0xffbdbd, 0xff0000, 0, 0);

        public final int lightColor;
        public final int glowColor;
        public final int flashPeriod;
        public final int flashSubPeriod;

        State(int lightColor, int glowColor, int flashPeriod, int flashSubPeriod) {
            this.lightColor = lightColor;
            this.glowColor = glowColor;
            this.flashPeriod = flashPeriod;
            this.flashSubPeriod = flashSubPeriod;
        }
    }
}
