package io.github.foundationgames.automobility.forge;

import io.github.foundationgames.automobility.Automobility;
import io.github.foundationgames.automobility.forge.network.AutomobilityPacketHandler;
import io.github.foundationgames.automobility.util.InitlessConstants;
import io.github.foundationgames.automobility.util.RegistryQueue;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(InitlessConstants.AUTOMOBILITY)
@EventBusSubscriber(modid = InitlessConstants.AUTOMOBILITY, bus = EventBusSubscriber.Bus.MOD)
public class AutomobilityNeoForge {
    public AutomobilityNeoForge() {
        NeoForgePlatform.init();

        Automobility.init();
        AutomobilityPacketHandler.init();
    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public static void registerAll(RegisterEvent evt) {
        register(BuiltInRegistries.BLOCK, evt);
        register(BuiltInRegistries.BLOCK_ENTITY_TYPE, evt);
        register(BuiltInRegistries.ITEM, evt);
        register(BuiltInRegistries.ENTITY_TYPE, evt);
        register(BuiltInRegistries.PARTICLE_TYPE, evt);
        register(BuiltInRegistries.SOUND_EVENT, evt);
        register(BuiltInRegistries.MENU, evt);
        register(BuiltInRegistries.RECIPE_TYPE, evt);
        register(BuiltInRegistries.RECIPE_SERIALIZER, evt);
        register(BuiltInRegistries.CREATIVE_MODE_TAB, evt);
    }

    public static <T> void register(Registry<T> registry, RegisterEvent evt) {
        if (registry == evt.getRegistry()) {
            for (var entry : RegistryQueue.getQueue(registry)) {
                evt.register(registry.key(), entry.rl(), entry.entry()::create);
            }
        }
    }
}
