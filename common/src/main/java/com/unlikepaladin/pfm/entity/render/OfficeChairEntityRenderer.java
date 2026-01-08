package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.entity.model.OfficeChairModelEmpty;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfficeChairEntityRenderer extends MobEntityRenderer<OfficeChairEntity, OfficeChairModelEmpty> {
    public static final Identifier[] MODEL_IDS = {new Identifier("pfm:block/office_chair/office_chair"), new Identifier("pfm:block/office_chair/office_chair_top"),
    new Identifier("pfm:block/office_chair/office_chair_bottom"), new Identifier("pfm:block/office_chair/office_chair_wheels")};

    // Wheel positions relative to center (x, z offsets)
    private static final float[][] WHEEL_OFFSETS = {
        {0f, 0.35f},   // front-right
        {-0.35f, 0f},  // front-left
        {0.35f, 0f},  // back-right
        {0f, -0.35f}  // back-left
    };

    public OfficeChairEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new OfficeChairModelEmpty(), 0f);
    }

    public static void renderItem(ItemStack itemStack, MatrixStack stack, ModelTransformation.Mode mode, VertexConsumerProvider provider,
                                  boolean leftHanded, int light, int overlay) {
        stack.push();

        BakedModel chairModel = ((PFMBakedModelManagerAccessor)MinecraftClient.getInstance().getBakedModelManager())
                .pfm$getModelFromNormalID(OfficeChairEntityRenderer.MODEL_IDS[0]);

        chairModel.getTransformation().getTransformation(mode).apply(
                mode == ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND ||
                        mode == ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND, stack);

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


            if (itemStack.getNbt().contains("Color") && quad.hasColor()) {
                int colorInt = DyeColor.byName(itemStack.getNbt().getString("Color"), DyeColor.WHITE).getFireworkColor();
                red = ((colorInt >> 16) & 0xFF) / 255.0f;
                green = ((colorInt >> 8) & 0xFF) / 255.0f;
                blue = (colorInt & 0xFF) / 255.0f;
            }


            provider.getBuffer(RenderLayers.getItemLayer(itemStack, true))
                    .quad(stack.peek(), quad, red, green, blue, light, overlay);
        }

        if (!chairModel.isSideLit()) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        stack.pop();
    }
    @Override
    public void render(OfficeChairEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        if (mobEntity.isRemoved() || !mobEntity.isAlive()) {
            return;
        }

        int damageStage = (int) Math.min(9, Math.max(mobEntity.getMaxHealth() - mobEntity.getHealth(), 0));
        VertexConsumer damageConsumer = MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers()
                .getBuffer(ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.get(damageStage));

        VertexConsumer solid =
                vertexConsumerProvider.getBuffer(RenderLayer.getSolid());

        // base
        matrixStack.push();

        matrixStack.translate(-0.45, 0, -0.5);

        BakedModel modelBase = ((PFMBakedModelManagerAccessor)MinecraftClient.getInstance().getBakedModelManager()).pfm$getModelFromNormalID(MODEL_IDS[2]);
        List<BakedQuad> baseQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> modelBase.getQuads(null, direction, mobEntity.world.random))
                .flatMap(List::stream).toList());
        baseQuads.addAll(modelBase.getQuads(null, null, mobEntity.world.random));
        for (BakedQuad quad : baseQuads) {
            float brightness = mobEntity.world.getBrightness(quad.getFace(), quad.hasShade());

            solid.quad(matrixStack.peek(), quad, brightness, brightness, brightness,  light, OverlayTexture.DEFAULT_UV);
            VertexConsumer damage = new OverlayVertexConsumer(
                    damageConsumer,
                    matrixStack.peek().getPositionMatrix(),
                    matrixStack.peek().getNormalMatrix(), 1.0f
            );
            if (mobEntity.timeUntilRegen > 0)
                damage.quad(matrixStack.peek(), quad, brightness, brightness, brightness, light, OverlayTexture.DEFAULT_UV);
        }

        matrixStack.pop();

        // wheels - render 4 times at different positions
        BakedModel wheelModel = ((PFMBakedModelManagerAccessor)MinecraftClient.getInstance().getBakedModelManager()).pfm$getModelFromNormalID(MODEL_IDS[3]);
        List<BakedQuad> wheelQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> wheelModel.getQuads(null, direction, mobEntity.world.random))
                .flatMap(List::stream).toList());
        wheelQuads.addAll(wheelModel.getQuads(null, null, mobEntity.world.random));

        // Calculate wheel rotation based on movement direction
        Vec3d velocity = mobEntity.getVelocity();
        float wheelYaw = 0.0F;
        double speed = velocity.horizontalLength();
        if (speed > 1.0E-7) {
            wheelYaw = (float) (MathHelper.atan2(velocity.z, velocity.x) * (180.0 / Math.PI)) - 90.0F;
        }

        // Get accumulated wheel spin from entity
        float wheelSpin = mobEntity.getWheelSpinAngle();

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
                float brightness = mobEntity.world.getBrightness(quad.getFace(), quad.hasShade());

                solid.quad(matrixStack.peek(), quad, brightness, brightness, brightness, light, OverlayTexture.DEFAULT_UV);
                VertexConsumer damage = new OverlayVertexConsumer(
                        damageConsumer,
                        matrixStack.peek().getPositionMatrix(),
                        matrixStack.peek().getNormalMatrix(), 1.0f
                );
                if (mobEntity.timeUntilRegen > 0)
                    damage.quad(matrixStack.peek(), quad, brightness, brightness, brightness, light, OverlayTexture.DEFAULT_UV);
            }

            matrixStack.pop();
        }

        // top
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - f));

        matrixStack.translate(-0.45, 0, -0.5);

        BakedModel model = ((PFMBakedModelManagerAccessor)MinecraftClient.getInstance().getBakedModelManager()).pfm$getModelFromNormalID(MODEL_IDS[1]);

        List<BakedQuad> chairQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> model.getQuads(null, direction, mobEntity.world.random))
                .flatMap(List::stream).toList());
        chairQuads.addAll(model.getQuads(null, null, mobEntity.world.random));


        for (BakedQuad quad : chairQuads) {
            float brightness = mobEntity.world.getBrightness(quad.getFace(), quad.hasShade());

            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;

            if (mobEntity.getPFMColor() != null && quad.hasColor()) {
                int colorInt = mobEntity.getPFMColor().getFireworkColor();
                red = ((colorInt >> 16) & 0xFF) / 255.0f;
                green = ((colorInt >> 8) & 0xFF) / 255.0f;
                blue = (colorInt & 0xFF) / 255.0f;
            }

            solid.quad(matrixStack.peek(), quad, red*brightness, green*brightness, blue*brightness, light, OverlayTexture.DEFAULT_UV);

            VertexConsumer damage = new OverlayVertexConsumer(
                    damageConsumer,
                    matrixStack.peek().getPositionMatrix(),
                    matrixStack.peek().getNormalMatrix(), 1.0f
            );
            if (mobEntity.timeUntilRegen > 0)
                damage.quad(matrixStack.peek(), quad, brightness, brightness, brightness, light, OverlayTexture.DEFAULT_UV);
        }

        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(OfficeChairEntity entity) {
        return null;
    }
}
