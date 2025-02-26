package io.github.foundationgames.automobility.forge;

import io.github.foundationgames.automobility.AutomobilityClient;
import io.github.foundationgames.automobility.block.AutomobilityBlocks;
import io.github.foundationgames.automobility.block.model.SlopeBakedModel;
import io.github.foundationgames.automobility.entity.AutomobileEntity;
import io.github.foundationgames.automobility.forge.block.render.ForgeSlopeBakedModel;
import io.github.foundationgames.automobility.particle.AutomobilityParticles;
import io.github.foundationgames.automobility.particle.DriftSmokeParticle;
import io.github.foundationgames.automobility.screen.AutomobileHud;
import io.github.foundationgames.automobility.util.InitlessConstants;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(value = Dist.CLIENT, modid = InitlessConstants.AUTOMOBILITY, bus = EventBusSubscriber.Bus.MOD)
public class AutomobilityClientForge {
    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent setup) {
        NeoForgePlatform.init();

        AutomobilityClient.init();

        SlopeBakedModel.impl = ForgeSlopeBakedModel::new;

        NeoForge.EVENT_BUS.<RenderGuiEvent.Pre>addListener(evt -> {
            var player = Minecraft.getInstance().player;
            if (player.getVehicle() instanceof AutomobileEntity auto) {
                AutomobileHud.render(evt.getGuiGraphics(), player, auto, evt.getPartialTick().getGameTimeDeltaTicks());
            }
        });

        NeoForge.EVENT_BUS.<ViewportEvent.ComputeFov>addListener(evt ->
                evt.setFOV(AutomobilityClient.modifyBoostFov(Minecraft.getInstance(), evt.getFOV(), (float) evt.getPartialTick())));
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent evt) {
        evt.registerSpriteSet(AutomobilityParticles.DRIFT_SMOKE.require(), DriftSmokeParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block evt) {
        evt.register(AutomobilityClient.GRASS_COLOR, AutomobilityBlocks.GRASS_OFF_ROAD.require());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item evt) {
        evt.register(AutomobilityClient.GRASS_ITEM_COLOR, AutomobilityBlocks.GRASS_OFF_ROAD.require());
    }
}
