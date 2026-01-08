package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.entity.model.OfficeChairModelEmpty;
import com.unlikepaladin.pfm.entity.render.state.OfficeChairEntityRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class OfficeChairEntityRenderer extends MobEntityRenderer<OfficeChairEntity, OfficeChairEntityRenderState, OfficeChairModelEmpty> {
    public static final Identifier[] MODEL_IDS = {Identifier.of("pfm:block/office_chair/office_chair"), Identifier.of("pfm:block/office_chair/office_chair_top"),
    Identifier.of("pfm:block/office_chair/office_chair_bottom"), Identifier.of("pfm:block/office_chair/office_chair_wheels")};
    // Wheel positions relative to center (x, z offsets)
    private static final float[][] WHEEL_OFFSETS = {
        {0f, 0.35f},   // front-right
        {-0.35f, 0f},  // front-left
        {0.35f, 0f},  // back-right
        {0f, -0.35f}  // back-left
    };

    public OfficeChairEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new OfficeChairModelEmpty(), 0.0f);
    }

    @Override
    public OfficeChairEntityRenderState createRenderState() {
        return new OfficeChairEntityRenderState();
    }

    @Override
    public void render(OfficeChairEntityRenderState mobEntity, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        int damageStage = (int) Math.min(9, Math.max(mobEntity.maxHealth - mobEntity.health, 0));

        RenderLayer damagedLayer = ModelBaker.BLOCK_DESTRUCTION_RENDER_LAYERS.get(damageStage);
        // base
        matrixStack.push();

        matrixStack.translate(-0.45, 0, -0.5);

        BlockStateModel modelBase = ModelHelper.getModelFromIdentifier(MODEL_IDS[2]);
        submitBlockPart(mobEntity, matrixStack, orderedRenderCommandQueue, damagedLayer, modelBase, 1.0f, 1.0f, 1.0f);

        // wheels - render 4 times at different positions
        BlockStateModel wheelModel = ModelHelper.getModelFromIdentifier(MODEL_IDS[3]);

        // Calculate wheel rotation based on movement direction
        Vec3d velocity = mobEntity.velocity;
        float wheelYaw = 0.0F;
        double speed = velocity.horizontalLength();
        if (speed > 1.0E-7) {
            wheelYaw = (float) (MathHelper.atan2(velocity.z, velocity.x) * (180.0 / Math.PI)) - 90.0F;
        }

        // Get accumulated wheel spin from entity
        float wheelSpin = mobEntity.wheelSpinAngle;

        for (float[] wheelOffset : WHEEL_OFFSETS) {
            matrixStack.push();

            // Move to wheel position (center of wheel)
            matrixStack.translate(wheelOffset[0], 0, wheelOffset[1]);

            // Rotate wheel to face movement direction around its own Y axis
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-wheelYaw));

            // Offset for the model first
            matrixStack.translate(-0.45, 0, -0.5);

            // Move to wheel's center, spin, then move back
            // Assuming wheel center is roughly at (0.5, 0.05, 0.5) in model space
            matrixStack.translate(0.5, 0.08, 0.5);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(wheelSpin));
            matrixStack.translate(-0.5, -0.08, -0.5);

            submitBlockPart(mobEntity, matrixStack, orderedRenderCommandQueue, damagedLayer, wheelModel, 1.0f, 1.0f, 1.0f);
        }

        // top
        float red = 1.0f;
        float green = 1.0f;
        float blue = 1.0f;

        if (mobEntity.color != null) {
            int colorInt = mobEntity.color.getFireworkColor();
            red = ((colorInt >> 16) & 0xFF) / 255.0f;
            green = ((colorInt >> 8) & 0xFF) / 255.0f;
            blue = (colorInt & 0xFF) / 255.0f;
        }

        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - mobEntity.bodyYaw));

        matrixStack.translate(-0.45, 0, -0.5);

        BlockStateModel model = ModelHelper.getModelFromIdentifier(MODEL_IDS[1]);
        submitBlockPart(mobEntity, matrixStack, orderedRenderCommandQueue, damagedLayer, model, red, green, blue);
    }

    private void submitBlockPart(OfficeChairEntityRenderState mobEntity, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, RenderLayer damagedLayer, BlockStateModel model, float red, float green, float blue) {
        orderedRenderCommandQueue.submitBlockStateModel(matrixStack, RenderLayers.cutout(), model, red, green, blue, mobEntity.light,
                OverlayTexture.DEFAULT_UV,
                mobEntity.outlineColor);
        if (mobEntity.timeUntilRegen > 0) {
            orderedRenderCommandQueue.submitCustom(matrixStack, damagedLayer, (matricesEntry, vertexConsumer) -> {
                BlockModelRenderer.render(
                        matricesEntry,
                        new OverlayVertexConsumer(vertexConsumer, matricesEntry, 1.0f),
                        model,
                        red,
                        green,
                        blue,
                        mobEntity.light,
                        OverlayTexture.DEFAULT_UV
                );
            });
        }

        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(OfficeChairEntityRenderState state) {
        return null;
    }

    @Override
    public void updateRenderState(OfficeChairEntity livingEntity, OfficeChairEntityRenderState livingEntityRenderState, float f) {
        super.updateRenderState(livingEntity, livingEntityRenderState, f);
        livingEntityRenderState.color = livingEntity.getPFMColor();
        livingEntityRenderState.health = livingEntity.getHealth();
        livingEntityRenderState.maxHealth = livingEntity.getMaxHealth();
        livingEntityRenderState.velocity = livingEntity.getVelocity();
        livingEntityRenderState.timeUntilRegen = livingEntity.timeUntilRegen;
        livingEntityRenderState.wheelSpinAngle = livingEntity.getWheelSpinAngle();
        livingEntityRenderState.world = livingEntity.getEntityWorld();
        livingEntityRenderState.pos = livingEntity.getEntityPos();
        livingEntityRenderState.random = livingEntity.getRandom();
    }
}
