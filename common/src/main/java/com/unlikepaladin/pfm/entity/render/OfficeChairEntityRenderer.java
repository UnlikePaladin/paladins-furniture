package com.unlikepaladin.pfm.entity.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.entity.model.OfficeChairModelEmpty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Axis;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfficeChairEntityRenderer extends MobRenderer<OfficeChairEntity, OfficeChairModelEmpty> {
    public static final ResourceLocation[] MODEL_IDS = {new ResourceLocation("pfm:block/office_chair/office_chair"), new ResourceLocation("pfm:block/office_chair/office_chair_top"),
    new ResourceLocation("pfm:block/office_chair/office_chair_bottom"), new ResourceLocation("pfm:block/office_chair/office_chair_wheels")};

    // Wheel positions relative to center (x, z offsets)
    private static final float[][] WHEEL_OFFSETS = {
        {0f, 0.35f},   // front-right
        {-0.35f, 0f},  // front-left
        {0.35f, 0f},  // back-right
        {0f, -0.35f}  // back-left
    };

    public OfficeChairEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new OfficeChairModelEmpty(), 0f);
    }

    public static void renderItem(ItemStack itemStack, PoseStack stack, ItemDisplayContext mode, MultiBufferSource provider,
                                  boolean leftHanded, int light, int overlay) {
        stack.pushPose();

        BakedModel chairModel = ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager())
                .pfm$getModelFromNormalID(OfficeChairEntityRenderer.MODEL_IDS[0]);


        RandomSource random = new SingleThreadedRandomSource(42L);
        List<BakedQuad> quads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> chairModel.getQuads(null, direction, random))
                .flatMap(List::stream).toList());

        quads.addAll(chairModel.getQuads(null, null, random));

        if (!chairModel.usesBlockLight()) {
            Lighting.setupForFlatItems();
        }

        for (BakedQuad quad : quads) {

            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;


            if (itemStack.getTag().contains("Color") && quad.isTinted()) {
                int colorInt = DyeColor.byName(itemStack.getTag().getString("Color"), DyeColor.WHITE).getFireworkColor();
                red = ((colorInt >> 16) & 0xFF) / 255.0f;
                green = ((colorInt >> 8) & 0xFF) / 255.0f;
                blue = (colorInt & 0xFF) / 255.0f;
            }


            provider.getBuffer(ItemBlockRenderTypes.getRenderType(itemStack, true))
                    .putBulkData(stack.last(), quad, red, green, blue, light, overlay);
        }

        if (!chairModel.usesBlockLight()) {
            Lighting.setupFor3DItems();
        }

        stack.popPose();
    }
    @Override
    public void render(OfficeChairEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light) {
        if (mobEntity.isRemoved() || !mobEntity.isAlive()) {
            return;
        }

        int damageStage = (int) Math.min(9, Math.max(mobEntity.getMaxHealth() - mobEntity.getHealth(), 0));
        VertexConsumer damageConsumer = Minecraft.getInstance().renderBuffers().crumblingBufferSource()
                .getBuffer(ModelBakery.DESTROY_TYPES.get(damageStage));

        VertexConsumer solid =
                vertexConsumerProvider.getBuffer(RenderType.solid());

        // base
        matrixStack.pushPose();

        matrixStack.translate(-0.45, 0, -0.5);

        BakedModel modelBase = ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager()).pfm$getModelFromNormalID(MODEL_IDS[2]);
        List<BakedQuad> baseQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> modelBase.getQuads(null, direction, mobEntity.level.random))
                .flatMap(List::stream).toList());
        baseQuads.addAll(modelBase.getQuads(null, null, mobEntity.level.random));
        for (BakedQuad quad : baseQuads) {
            float brightness = mobEntity.level.getShade(quad.getDirection(), quad.isShade());

            solid.putBulkData(matrixStack.last(), quad, brightness, brightness, brightness,  light, OverlayTexture.NO_OVERLAY);
            VertexConsumer damage = new SheetedDecalTextureGenerator(
                    damageConsumer,
                    matrixStack.last().pose(),
                    matrixStack.last().normal(), 1.0f
            );
            if (mobEntity.invulnerableTime > 0)
                damage.putBulkData(matrixStack.last(), quad, brightness, brightness, brightness, light, OverlayTexture.NO_OVERLAY);
        }

        matrixStack.popPose();

        // wheels - render 4 times at different positions
        BakedModel wheelModel = ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager()).pfm$getModelFromNormalID(MODEL_IDS[3]);
        List<BakedQuad> wheelQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> wheelModel.getQuads(null, direction, mobEntity.level.random))
                .flatMap(List::stream).toList());
        wheelQuads.addAll(wheelModel.getQuads(null, null, mobEntity.level.random));

        // Calculate wheel rotation based on movement direction
        Vec3 velocity = mobEntity.getDeltaMovement();
        float wheelYaw = 0.0F;
        double speed = velocity.horizontalDistance();
        if (speed > 1.0E-7) {
            wheelYaw = (float) (Mth.atan2(velocity.z, velocity.x) * (180.0 / Math.PI)) - 90.0F;
        }

        // Get accumulated wheel spin from entity
        float wheelSpin = mobEntity.getWheelSpinAngle();

        for (float[] wheelOffset : WHEEL_OFFSETS) {
            matrixStack.pushPose();

            // Move to wheel position (center of wheel)
            matrixStack.translate(wheelOffset[0], 0, wheelOffset[1]);

            // Rotate wheel to face movement direction around its own Y axis
            matrixStack.mulPose(Axis.YP.rotationDegrees(-wheelYaw));

            // Offset for the model first
            matrixStack.translate(-0.45, 0, -0.5);

            // Move to wheel's center, spin, then move back
            // Assuming wheel center is roughly at (0.5, 0.05, 0.5) in model space
            matrixStack.translate(0.5, 0.08, 0.5);
            matrixStack.mulPose(Axis.XP.rotationDegrees(wheelSpin));
            matrixStack.translate(-0.5, -0.08, -0.5);

            for (BakedQuad quad : wheelQuads) {
                float brightness = mobEntity.level.getShade(quad.getDirection(), quad.isShade());

                solid.putBulkData(matrixStack.last(), quad, brightness, brightness, brightness, light, OverlayTexture.NO_OVERLAY);
                VertexConsumer damage = new SheetedDecalTextureGenerator(
                        damageConsumer,
                        matrixStack.last().pose(),
                        matrixStack.last().normal(), 1.0f
                );
                if (mobEntity.invulnerableTime > 0)
                    damage.putBulkData(matrixStack.last(), quad, brightness, brightness, brightness, light, OverlayTexture.NO_OVERLAY);
            }

            matrixStack.popPose();
        }

        // top
        matrixStack.pushPose();
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - f));

        matrixStack.translate(-0.45, 0, -0.5);

        BakedModel model = ((PFMBakedModelManagerAccessor) Minecraft.getInstance().getModelManager()).pfm$getModelFromNormalID(MODEL_IDS[1]);

        List<BakedQuad> chairQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> model.getQuads(null, direction, mobEntity.level.random))
                .flatMap(List::stream).toList());
        chairQuads.addAll(model.getQuads(null, null, mobEntity.level.random));


        for (BakedQuad quad : chairQuads) {
            float brightness = mobEntity.level.getShade(quad.getDirection(), quad.isShade());

            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;

            if (mobEntity.getPFMColor() != null && quad.isTinted()) {
                int colorInt = mobEntity.getPFMColor().getFireworkColor();
                red = ((colorInt >> 16) & 0xFF) / 255.0f;
                green = ((colorInt >> 8) & 0xFF) / 255.0f;
                blue = (colorInt & 0xFF) / 255.0f;
            }

            solid.putBulkData(matrixStack.last(), quad, red*brightness, green*brightness, blue*brightness, light, OverlayTexture.NO_OVERLAY);

            VertexConsumer damage = new SheetedDecalTextureGenerator(
                    damageConsumer,
                    matrixStack.last().pose(),
                    matrixStack.last().normal(), 1.0f
            );
            if (mobEntity.invulnerableTime > 0)
                damage.putBulkData(matrixStack.last(), quad, brightness, brightness, brightness, light, OverlayTexture.NO_OVERLAY);
        }

        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(OfficeChairEntity entity) {
        return null;
    }
}
