package io.github.foundationgames.automobility.mixin;

import io.github.foundationgames.automobility.entity.AutomobileEntity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "stopRiding", at = @At("HEAD"))
    private void automobility$clientFinalSyncBeforeDismountAutomobile(CallbackInfo ci) {
        var self = (Entity) (Object) this;
        if (!self.level().isClientSide()) {
            return;
        }

        if (self instanceof LocalPlayer player) {
            var vehicle = player.getVehicle();

            if (vehicle instanceof AutomobileEntity auto && auto.isDriving(player)) {
                auto.clientOnAboutToDismount();
            }
        }
    }
}
