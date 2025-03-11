package io.github.foundationgames.automobility.fabric.controller.controlify;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.api.guide.ActionPriority;
import dev.isxander.controlify.api.ingameguide.ActionLocation;
import dev.isxander.controlify.bindings.BindContext;
import io.github.foundationgames.automobility.Automobility;
import io.github.foundationgames.automobility.entity.AutomobileEntity;
import net.minecraft.network.chat.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ControlifyCompat implements ControlifyEntrypoint {
    public static final Set<InputBindingSupplier> AUTOMOBILITY_BINDINGS = new HashSet<>();

    @Override
    public void onControlifyPreInit(ControlifyApi controlify) {
        BindContext drivingCtx = new BindContext(Automobility.rl("driving"), mc -> {
            if (mc.player != null) {
                var veh = mc.player.getVehicle();
                return veh != null && veh instanceof AutomobileEntity && veh.getControllingPassenger() == mc.player;
            }
            return false;
        });
        Component category = Component.translatable("controlify.binding_category.driving");

        ControlifyController.accelerateBinding = ControlifyBindApi.get().registerBinding(builder -> builder
                .id(Automobility.rl("accelerate_automobile"))
                .allowedContexts(drivingCtx)
                .category(category));
        ControlifyController.brakeBinding = ControlifyBindApi.get().registerBinding(builder -> builder
                .id(Automobility.rl("brake_automobile"))
                .allowedContexts(drivingCtx)
                .category(category));
        ControlifyController.driftBinding = ControlifyBindApi.get().registerBinding(builder -> builder
                .id(Automobility.rl("drift_automobile"))
                .allowedContexts(drivingCtx)
                .category(category));

        AUTOMOBILITY_BINDINGS.clear();
        AUTOMOBILITY_BINDINGS.add(ControlifyController.accelerateBinding);
        AUTOMOBILITY_BINDINGS.add(ControlifyController.brakeBinding);
        AUTOMOBILITY_BINDINGS.add(ControlifyController.driftBinding);

        ControlifyEvents.INGAME_GUIDE_REGISTRY.register(ev -> {
            var bindings = ev.bindings();

            ev.registry().registerGuideAction(ControlifyController.accelerateBinding.on(bindings), ActionLocation.LEFT, ActionPriority.LOW, ctx -> {
                if (ctx.player().getVehicle() instanceof AutomobileEntity)
                    return Optional.of(Component.translatable("controlify.binding.automobility.accelerate_automobile"));
                return Optional.empty();
            });
            ev.registry().registerGuideAction(ControlifyController.brakeBinding.on(bindings), ActionLocation.LEFT, ActionPriority.LOW, ctx -> {
                if (ctx.player().getVehicle() instanceof AutomobileEntity)
                    return Optional.of(Component.translatable("controlify.binding.automobility.brake_automobile"));
                return Optional.empty();
            });
            ev.registry().registerGuideAction(ControlifyController.driftBinding.on(bindings), ActionLocation.LEFT, ActionPriority.LOW, ctx -> {
                if (ctx.player().getVehicle() instanceof AutomobileEntity)
                    return Optional.of(Component.translatable("controlify.binding.automobility.drift_automobile"));
                return Optional.empty();
            });
        });
    }

    @Override
    public void onControllersDiscovered(ControlifyApi controlify) {

    }
}
