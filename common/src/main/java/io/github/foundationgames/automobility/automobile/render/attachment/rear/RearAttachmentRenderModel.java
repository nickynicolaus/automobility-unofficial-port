package io.github.foundationgames.automobility.automobile.render.attachment.rear;

import io.github.foundationgames.automobility.automobile.attachment.rear.RearAttachment;
import io.github.foundationgames.automobility.automobile.model.ModelDefinition;
import io.github.foundationgames.automobility.automobile.render.BaseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.NoSuchElementException;

public class RearAttachmentRenderModel extends BaseModel {
    private final @Nullable ModelPart wheels;

    public RearAttachmentRenderModel(EntityRendererProvider.Context ctx,
                                     ModelDefinition.RenderMaterial material,
                                     ModelLayerLocation layer,
                                     Vector3f translation, Vector3f rotation, Vector3f scale) {
        super(ctx, material, layer, translation, rotation, scale);
        ModelPart wheels;
        try {
            wheels = this.root.getChild("wheels");
        } catch (NoSuchElementException ignored) {
            wheels = null;
        }
        this.wheels = wheels;
    }

    public void setRenderState(@Nullable RearAttachment attachment, float wheelAngle, float tickDelta) {
        if (this.wheels != null) {
            this.wheels.setRotation(wheelAngle, 0, 0);
        }
    }

    public void resetModel() {
        this.setRenderState(null, 0, 0);
    }
}
