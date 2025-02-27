package io.github.foundationgames.automobility.automobile.render;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import io.github.foundationgames.automobility.Automobility;
import io.github.foundationgames.automobility.automobile.model.ModelDefinition;
import io.github.foundationgames.automobility.automobile.model.ModelType;
import io.github.foundationgames.automobility.automobile.render.attachment.front.HarvesterFrontAttachmentModel;
import io.github.foundationgames.automobility.automobile.render.attachment.rear.BannerPostRearAttachmentModel;
import io.github.foundationgames.automobility.automobile.render.attachment.rear.ChestRearAttachmentModel;
import io.github.foundationgames.automobility.automobile.render.attachment.rear.GrindstoneRearAttachmentModel;
import io.github.foundationgames.automobility.automobile.render.attachment.rear.PlowRearAttachmentModel;
import io.github.foundationgames.automobility.automobile.render.attachment.rear.StonecutterRearAttachmentModel;
import io.github.foundationgames.automobility.util.EntityRenderHelper;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AutomobileModels implements ResourceManagerReloadListener {
    public static final ResourceLocation RELOADER_ID = Automobility.rl("automobile_models");
    private static final ResourceLocation EMPTY = Automobility.rl("empty");
    public static final Gson GSON = new Gson();

    private static Model skidEffect = new EmptyModel();
    private static Model exhaustFumes = new EmptyModel();

    private static final Map<ResourceLocation, ModelDefinition> modelProviders = new HashMap<>();
    private static final Map<ResourceLocation, Model> models = new HashMap<>();

    public static final ModelDefinition FRAME_STANDARD = ModelDefinition.ofYaw(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/frame/standard"), "main"),
            -90
    );
    public static final ModelDefinition FRAME_TRACTOR = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/frame/tractor"), "main")
    );
    public static final ModelDefinition FRAME_SHOPPING_CART = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            new ModelLayerLocation(Automobility.rl("automobile/frame/shopping_cart"), "main")
    );
    public static final ModelDefinition FRAME_C_ARR = ModelDefinition.ofYaw(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            new ModelLayerLocation(Automobility.rl("automobile/frame/c_arr"), "main"),
            -90
    );
    public static final ModelDefinition FRAME_PINEAPPLE = ModelDefinition.ofScale(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/frame/pineapple"), "main"),
            2
    );
    public static final ModelDefinition FRAME_MOTORCAR = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.TRANSLUCENT,
            new ModelLayerLocation(Automobility.rl("automobile/frame/motorcar"), "main")
    );
    public static final ModelDefinition FRAME_RICKSHAW = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            new ModelLayerLocation(Automobility.rl("automobile/frame/rickshaw"), "main")
    );

    public static final ModelDefinition WHEEL_STANDARD = ModelDefinition.ofYaw(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/wheel/standard"), "main"),
            -90
    );
    public static final ModelDefinition WHEEL_OFF_ROAD = ModelDefinition.ofYaw(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/wheel/off_road"), "main"),
            -90
    );
    public static final ModelDefinition WHEEL_STEEL = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/wheel/steel"), "main")
    );
    public static final ModelDefinition WHEEL_TRACTOR = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/wheel/tractor"), "main")
    );
    public static final ModelDefinition WHEEL_CARRIAGE = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            new ModelLayerLocation(Automobility.rl("automobile/wheel/carriage"), "main")
    );

    public static final ModelDefinition ENGINE_STONE = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/engine/stone"), "main")
    );
    public static final ModelDefinition ENGINE_IRON = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/engine/iron"), "main")
    );
    public static final ModelDefinition ENGINE_COPPER = ModelDefinition.ofYaw(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/engine/copper"), "main"),
            180
    );
    public static final ModelDefinition ENGINE_GOLD = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/engine/gold"), "main")
    );
    public static final ModelDefinition ENGINE_DIAMOND = ModelDefinition.of(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/engine/diamond"), "main")
    );
    public static final ModelDefinition ENGINE_CREATIVE = ModelDefinition.ofYaw(
            ModelType.BASIC, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/engine/creative"), "main"),
            180
    );

    public static final ModelDefinition REAR_ATT_PASSENGER_SEAT = ModelDefinition.of(
            ModelType.REAR_ATTACHMENT, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            new ModelLayerLocation(Automobility.rl("automobile/rear_attachment/passenger_seat"), "main")
    );
    public static final ModelDefinition REAR_ATT_BLOCK = ModelDefinition.of(
            ModelType.REAR_ATTACHMENT, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            new ModelLayerLocation(Automobility.rl("automobile/rear_attachment/block"), "main")
    );
    public static final ModelDefinition REAR_ATT_GRINDSTONE = ModelDefinition.of(
            ModelType.GRINDSTONE_REAR_ATTACHMENT, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            GrindstoneRearAttachmentModel.MODEL_LAYER
    );
    public static final ModelDefinition REAR_ATT_STONECUTTER = ModelDefinition.of(
            ModelType.STONECUTTER_REAR_ATTACHMENT, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            StonecutterRearAttachmentModel.MODEL_LAYER
    );
    public static final ModelDefinition REAR_ATT_CHEST = ModelDefinition.of(
            ModelType.CHEST_REAR_ATTACHMENT, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            ChestRearAttachmentModel.MODEL_LAYER
    );
    public static final ModelDefinition REAR_ATT_BANNER_POST = ModelDefinition.of(
            ModelType.BANNER_REAR_ATTACHMENT, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            BannerPostRearAttachmentModel.MODEL_LAYER
    );
    public static final ModelDefinition REAR_ATT_PLOW = ModelDefinition.of(
            ModelType.PLOW_REAR_ATTACHMENT, ModelDefinition.RenderMaterial.CUTOUT_NO_CULL,
            PlowRearAttachmentModel.MODEL_LAYER
    );

    public static final ModelDefinition FRONT_ATT_MOB_CONTROLLER = ModelDefinition.of(
            ModelType.FRONT_ATTACHMENT, ModelDefinition.RenderMaterial.CUTOUT,
            new ModelLayerLocation(Automobility.rl("automobile/front_attachment/mob_controller"), "main")
    );
    public static final ModelDefinition FRONT_ATT_HARVESTER = ModelDefinition.of(
            ModelType.HARVESTER_FRONT_ATTACHMENT, ModelDefinition.RenderMaterial.CUTOUT,
            HarvesterFrontAttachmentModel.MODEL_LAYER
    );

    public static Model getSkidEffectModel() {
        return skidEffect;
    }

    public static Model getExhaustFumesModel() {
        return exhaustFumes;
    }
    
    public static void register(ResourceLocation location, ModelDefinition model) {
        modelProviders.put(location, model);
    }

    public static void init() {
        EntityRenderHelper.registerContextListener(ctx -> {
            models.clear();
            models.put(EMPTY, new EmptyModel());

            for (var entry : modelProviders.entrySet()) {
                models.put(entry.getKey(), entry.getValue().createModel(ctx));
            }

            skidEffect = new SkidEffectModel(ctx);
            exhaustFumes = new ExhaustFumesModel(ctx);
        });

        registerDefaults();
    }

    public static void registerDefaults() {
        register(Automobility.rl("frame/standard"), FRAME_STANDARD);
        register(Automobility.rl("frame/tractor"), FRAME_TRACTOR);
        register(Automobility.rl("frame/shopping_cart"), FRAME_SHOPPING_CART);
        register(Automobility.rl("frame/c_arr"), FRAME_C_ARR);
        register(Automobility.rl("frame/pineapple"), FRAME_PINEAPPLE);
        register(Automobility.rl("frame/motorcar"), FRAME_MOTORCAR);
        register(Automobility.rl("frame/rickshaw"), FRAME_RICKSHAW);

        register(Automobility.rl("wheel/standard"), WHEEL_STANDARD);
        register(Automobility.rl("wheel/off_road"), WHEEL_OFF_ROAD);
        register(Automobility.rl("wheel/steel"), WHEEL_STEEL);
        register(Automobility.rl("wheel/tractor"), WHEEL_TRACTOR);
        register(Automobility.rl("wheel/carriage"), WHEEL_CARRIAGE);

        register(Automobility.rl("engine/stone"), ENGINE_STONE);
        register(Automobility.rl("engine/iron"), ENGINE_IRON);
        register(Automobility.rl("engine/copper"), ENGINE_COPPER);
        register(Automobility.rl("engine/gold"), ENGINE_GOLD);
        register(Automobility.rl("engine/diamond"), ENGINE_DIAMOND);
        register(Automobility.rl("engine/creative"), ENGINE_CREATIVE);

        register(Automobility.rl("rear_attachment/passenger_seat"), REAR_ATT_PASSENGER_SEAT);
        register(Automobility.rl("rear_attachment/block"), REAR_ATT_BLOCK);
        register(Automobility.rl("rear_attachment/grindstone"), REAR_ATT_GRINDSTONE);
        register(Automobility.rl("rear_attachment/stonecutter"), REAR_ATT_STONECUTTER);
        register(Automobility.rl("rear_attachment/chest"), REAR_ATT_CHEST);
        register(Automobility.rl("rear_attachment/banner_post"), REAR_ATT_BANNER_POST);
        register(Automobility.rl("rear_attachment/plow"), REAR_ATT_PLOW);

        register(Automobility.rl("front_attachment/mob_controller"), FRONT_ATT_MOB_CONTROLLER);
        register(Automobility.rl("front_attachment/harvester"), FRONT_ATT_HARVESTER);
    }

    public static Model getModelOrNull(ResourceLocation location) {
        return models.get(location);
    }

    public static Model getModel(ResourceLocation location) {
        var result = getModelOrNull(location);
        if (result == null) {
            return getEmpty();
        }
        return result;
    }

    public static Model getEmpty() {
        return getModelOrNull(EMPTY);
    }

    public static Optional<ModelDefinition> readJson(InputStream data) {
        JsonElement json = GSON.fromJson(GSON.newJsonReader(new InputStreamReader(data)), JsonObject.class);

        return ModelDefinition.CODEC.decode(JsonOps.INSTANCE, json).result().map(Pair::getFirst);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        FileToIdConverter.json("automobile_models").listMatchingResources(resourceManager).forEach((rl, res) -> {
            models.clear();
            AutomobileModels.registerDefaults();

            try (var in = res.open()) {
                var data = AutomobileModels.readJson(in);
                data.ifPresent(model -> AutomobileModels.register(rl, model));
            } catch (IOException | ResourceLocationException e) {
                Automobility.LOG.error(e);
            }
        });
    }
}
