package io.github.foundationgames.automobility.fabric.mixin.controlify;

import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
//@Mixin(value = ControllerBindingImpl.class, remap = false)
public abstract class ControllerBindingImplMixin {
//    @Shadow @Final private Controller<?, ?> controller;
//
//    @Shadow public abstract IBind<ControllerState> getBind();
//
//    @Inject(method = "held", at = @At("HEAD"), cancellable = true)
//    private void automobility$makeAutomobileInputsWork(CallbackInfoReturnable<Boolean> cir) {
//        var minecraft = Minecraft.getInstance();
//        var player = minecraft.player;
//        if (player != null && player.getVehicle() instanceof AutomobileEntity auto &&
//                auto.getControllingPassenger() == player && minecraft.screen == null) {
//            var controller = this.controller;
//            for (var supplier : ControlifyCompat.AUTOMOBILITY_BINDINGS) {
//                var binding = supplier.onController(controller);
//                if (binding != this && binding.getBind().equals(this.getBind())) {
//                    cir.setReturnValue(false);
//                }
//            }
//        }
//    }
//
//    @Inject(method = "prevHeld", at = @At("HEAD"), cancellable = true)
//    private void automobility$makeAutomobileInputsHaveWorked(CallbackInfoReturnable<Boolean> cir) {
//        this.automobility$makeAutomobileInputsWork(cir);
//    }
}
