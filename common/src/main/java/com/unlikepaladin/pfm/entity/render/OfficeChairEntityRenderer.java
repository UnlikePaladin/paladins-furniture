package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.entity.model.OfficeChairModelEmpty;
import com.unlikepaladin.pfm.entity.render.state.OfficeChairEntityRenderState;
import com.unlikepaladin.pfm.items.PFMComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
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

    public static void renderItem(DyeColor color, MatrixStack stack, ModelTransformationMode mode, VertexConsumerProvider provider, int light, int overlay) {
        stack.push();

        BakedModel chairModel = ModelHelper.getModelFromIdentifier(OfficeChairEntityRenderer.MODEL_IDS[0]);

        chairModel.getTransformation().getTransformation(mode).apply(
                mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND ||
                        mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND, stack);

        stack.translate(-.5, -.5, -.5); // Replicate ItemRenderer's translation

        Random random = new LocalRandom(42L);
        List<BakedQuad> quads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> chairModel.getQuads(null, direction, random))
                .flatMap(List::stream).toList());

        quads.addAll(chairModel.getQuads(null, null, random));

        if (!chairModel.isSideLit()) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        for (BakedQuad quad : quads) {

            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;


            if (quad.hasTint()) {
                int colorInt = color.getFireworkColor();
                red = ((colorInt >> 16) & 0xFF) / 255.0f;
                green = ((colorInt >> 8) & 0xFF) / 255.0f;
                blue = (colorInt & 0xFF) / 255.0f;
            }


            provider.getBuffer(TexturedRenderLayers.getItemEntityTranslucentCull())
                    .quad(stack.peek(), quad, red, green, blue, 1.0f, light, overlay);
        }

        if (!chairModel.isSideLit()) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        stack.pop();
    }

    @Override
    public void render(OfficeChairEntityRenderState mobEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        int damageStage = (int) Math.min(9, Math.max(mobEntity.maxHealth - mobEntity.health, 0));
        VertexConsumer damageConsumer = MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers()
                .getBuffer(ModelBaker.BLOCK_DESTRUCTION_RENDER_LAYERS.get(damageStage));

        VertexConsumer solid =
                vertexConsumerProvider.getBuffer(RenderLayer.getCutoutMipped());

        // base
        matrixStack.push();

        matrixStack.translate(-0.45, 0, -0.5);

        BakedModel modelBase = ModelHelper.getModelFromIdentifier(MODEL_IDS[2]);
        List<BakedQuad> baseQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> modelBase.getQuads(null, direction, mobEntity.random))
                .flatMap(List::stream).toList());
        baseQuads.addAll(modelBase.getQuads(null, null, mobEntity.random));
        for (BakedQuad quad : baseQuads) {
            float brightness = ModelHelper.getBrightness(quad.getFace(), quad.hasShade(), mobEntity.isDarkenedDim);

            solid.quad(matrixStack.peek(), quad, brightness, brightness, brightness, 1.0f, light, OverlayTexture.DEFAULT_UV);
            VertexConsumer damage = new OverlayVertexConsumer(
                    damageConsumer,
                    matrixStack.peek(), 1.0f
            );
            if (mobEntity.timeUntilRegen > 0)
                damage.quad(matrixStack.peek(), quad, brightness, brightness, brightness, 1.0f, light, OverlayTexture.DEFAULT_UV);
        }

        matrixStack.pop();

        // wheels - render 4 times at different positions
        BakedModel wheelModel = ModelHelper.getModelFromIdentifier(MODEL_IDS[3]);
        List<BakedQuad> wheelQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> wheelModel.getQuads(null, direction, mobEntity.random))
                .flatMap(List::stream).toList());
        wheelQuads.addAll(wheelModel.getQuads(null, null, mobEntity.random));

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

            for (BakedQuad quad : wheelQuads) {
                float brightness = ModelHelper.getBrightness(quad.getFace(), quad.hasShade(), mobEntity.isDarkenedDim);

                solid.quad(matrixStack.peek(), quad, brightness, brightness, brightness, 1.0f, light, OverlayTexture.DEFAULT_UV);
                VertexConsumer damage = new OverlayVertexConsumer(
                        damageConsumer,
                        matrixStack.peek(), 1.0f
                );
                if (mobEntity.timeUntilRegen > 0)
                    damage.quad(matrixStack.peek(), quad, brightness, brightness, brightness,1.0f, light, OverlayTexture.DEFAULT_UV);
            }

            matrixStack.pop();
        }

        // top
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - mobEntity.bodyYaw));

        matrixStack.translate(-0.45, 0, -0.5);

        BakedModel model = ModelHelper.getModelFromIdentifier(MODEL_IDS[1]);

        List<BakedQuad> chairQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> model.getQuads(null, direction, mobEntity.random))
                .flatMap(List::stream).toList());
        chairQuads.addAll(model.getQuads(null, null, mobEntity.random));


        for (BakedQuad quad : chairQuads) {
            float brightness = ModelHelper.getBrightness(quad.getFace(), quad.hasShade(), mobEntity.isDarkenedDim);

            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;

            if (mobEntity.color != null && quad.hasTint()) {
                int colorInt = mobEntity.color.getFireworkColor();
                red = ((colorInt >> 16) & 0xFF) / 255.0f;
                green = ((colorInt >> 8) & 0xFF) / 255.0f;
                blue = (colorInt & 0xFF) / 255.0f;
            }

            solid.quad(matrixStack.peek(), quad, red*brightness, green*brightness, blue*brightness, 1.0f, light, OverlayTexture.DEFAULT_UV);

            VertexConsumer damage = new OverlayVertexConsumer(
                    damageConsumer,
                    matrixStack.peek(), 1.0f
            );
            if (mobEntity.timeUntilRegen > 0)
                damage.quad(matrixStack.peek(), quad, 1.0f, 1.0f, 1.0f, 1.0f, light, OverlayTexture.DEFAULT_UV);
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
        livingEntityRenderState.world = livingEntity.getWorld();
        livingEntityRenderState.pos = livingEntity.getPos();
        livingEntityRenderState.random = livingEntity.getRandom();
        livingEntityRenderState.isDarkenedDim = !livingEntity.getWorld().getDimension().bedWorks();
    }
}
