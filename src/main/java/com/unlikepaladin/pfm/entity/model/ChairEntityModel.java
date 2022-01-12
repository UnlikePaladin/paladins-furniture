package com.unlikepaladin.pfm.entity.model;

import com.unlikepaladin.pfm.blockentities.PlayerChairBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class ChairEntityModel<T extends PlayerChairBlockEntity> extends CompositeEntityModel {
    private final ModelPart root;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart base;
    private final ModelPart backrest;

    public ChairEntityModel(ModelPart root) {
        this.root = root;
        this.leg1= root.getChild("leg1");
        this.leg2= root.getChild("leg2");

        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.base = root.getChild("base");
        this.backrest = root.getChild("backrest");

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild("backrest", ModelPartBuilder.create().uv(0, 0).cuboid(0.3282F, 10.4688F, 1.5939F, 2.30F, 14.0F, 13.0F), ModelTransform.pivot(0F, 0F, 0F));
        modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(0.3281F, 8.0F, 1.59F, 14.0F, 2.5F, 13.0F), ModelTransform.pivot(0F, 0F, 0F));

        modelPartData.addChild("leg3", ModelPartBuilder.create().uv(0, 0).cuboid(11.0F, 0.0F, 2.0F, 2.5F, 9.0F, 2.5F), ModelTransform.pivot(0F, 0F, 0F));
        modelPartData.addChild("leg4", ModelPartBuilder.create().uv(0, 0).cuboid(1.0F, 0.0F, 11.0938F, 2.5F, 9.0F, 2.5F), ModelTransform.pivot(0F, 0F, 0F));
        modelPartData.addChild("leg1", ModelPartBuilder.create().uv(0, 0).cuboid(1F, 0.0F, 2.0F, 2.5F, 9.0F, 2.5F), ModelTransform.pivot(0F, 0F, 0F));
        modelPartData.addChild("leg2", ModelPartBuilder.create().uv(0, 0).cuboid(11.0F, 0.0F, 11.0938F, 2.5F, 9.0F, 2.5F), ModelTransform.pivot(0F, 0F, 0F));

        return TexturedModelData.of(modelData, 16, 16);

    }



    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        leg1.render(matrices, vertices, light, overlay);
        leg2.render(matrices, vertices, light, overlay);
        leg3.render(matrices, vertices, light, overlay);
        leg4.render(matrices, vertices, light, overlay);
        base.render(matrices, vertices, light, overlay);
        backrest.render(matrices, vertices, light, overlay);

    }

    @Override
    public Iterable<ModelPart> getParts() {
return null;
    }




    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
