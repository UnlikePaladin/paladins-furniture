package com.unlikepaladin.pfm.entity.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.entity.model.OfficeChairModelEmpty;
import com.unlikepaladin.pfm.entity.render.state.OfficeChairEntityRenderState;
import com.unlikepaladin.pfm.items.PFMComponents;
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

public class OfficeChairEntityRenderer extends MobRenderer<OfficeChairEntity, OfficeChairEntityRenderState, OfficeChairModelEmpty> {
    public static final ResourceLocation[] MODEL_IDS = {ResourceLocation.parse("pfm:block/office_chair/office_chair"), ResourceLocation.parse("pfm:block/office_chair/office_chair_top"),
    ResourceLocation.parse("pfm:block/office_chair/office_chair_bottom"), ResourceLocation.parse("pfm:block/office_chair/office_chair_wheels")};
    // Wheel positions relative to center (x, z offsets)
    private static final float[][] WHEEL_OFFSETS = {
        {0f, 0.35f},   // front-right
        {-0.35f, 0f},  // front-left
        {0.35f, 0f},  // back-right
        {0f, -0.35f}  // back-left
    };

    public OfficeChairEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new OfficeChairModelEmpty(), 0.0f);
    }

    @Override
    public OfficeChairEntityRenderState createRenderState() {
        return new OfficeChairEntityRenderState();
    }

    public static void renderItem(DyeColor color, PoseStack stack, ItemDisplayContext mode, MultiBufferSource provider, int light, int overlay) {
        stack.pushPose();

        BakedModel chairModel = ModelHelper.getModelFromIdentifier(OfficeChairEntityRenderer.MODEL_IDS[0]);


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


            if (quad.isTinted()) {
                int colorInt = color.getFireworkColor();
                red = ((colorInt >> 16) & 0xFF) / 255.0f;
                green = ((colorInt >> 8) & 0xFF) / 255.0f;
                blue = (colorInt & 0xFF) / 255.0f;
            }


            provider.getBuffer(Sheets.itemEntityTranslucentCull())
                    .putBulkData(stack.last(), quad, red, green, blue, 1.0f, light, overlay);
        }

        if (!chairModel.usesBlockLight()) {
            Lighting.setupFor3DItems();
        }

        stack.popPose();
    }

    @Override
    public void render(OfficeChairEntityRenderState mobEntity, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light) {
        int damageStage = (int) Math.min(9, Math.max(mobEntity.maxHealth - mobEntity.health, 0));
        VertexConsumer damageConsumer = Minecraft.getInstance().renderBuffers().crumblingBufferSource()
                .getBuffer(ModelBakery.DESTROY_TYPES.get(damageStage));

        VertexConsumer solid =
                vertexConsumerProvider.getBuffer(RenderType.cutoutMipped());

        // base
        matrixStack.pushPose();

        matrixStack.translate(-0.45, 0, -0.5);

        BakedModel modelBase = ModelHelper.getModelFromIdentifier(MODEL_IDS[2]);
        List<BakedQuad> baseQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> modelBase.getQuads(null, direction, mobEntity.random))
                .flatMap(List::stream).toList());
        baseQuads.addAll(modelBase.getQuads(null, null, mobEntity.random));
        for (BakedQuad quad : baseQuads) {
            float brightness = ModelHelper.getBrightness(quad.getDirection(), quad.isShade(), mobEntity.isDarkenedDim);

            solid.putBulkData(matrixStack.last(), quad, brightness, brightness, brightness, 1.0f, light, OverlayTexture.NO_OVERLAY);
            VertexConsumer damage = new SheetedDecalTextureGenerator(
                    damageConsumer,
                    matrixStack.last(), 1.0f
            );
            if (mobEntity.invulnerableTime > 0)
                damage.putBulkData(matrixStack.last(), quad, brightness, brightness, brightness, 1.0f, light, OverlayTexture.NO_OVERLAY);
        }

        matrixStack.popPose();

        // wheels - render 4 times at different positions
        BakedModel wheelModel = ModelHelper.getModelFromIdentifier(MODEL_IDS[3]);
        List<BakedQuad> wheelQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> wheelModel.getQuads(null, direction, mobEntity.random))
                .flatMap(List::stream).toList());
        wheelQuads.addAll(wheelModel.getQuads(null, null, mobEntity.random));

        // Calculate wheel rotation based on movement direction
        Vec3 velocity = mobEntity.velocity;
        float wheelYaw = 0.0F;
        double speed = velocity.horizontalDistance();
        if (speed > 1.0E-7) {
            wheelYaw = (float) (Mth.atan2(velocity.z, velocity.x) * (180.0 / Math.PI)) - 90.0F;
        }

        // Get accumulated wheel spin from entity
        float wheelSpin = mobEntity.wheelSpinAngle;

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
                float brightness = ModelHelper.getBrightness(quad.getDirection(), quad.isShade(), mobEntity.isDarkenedDim);

                solid.putBulkData(matrixStack.last(), quad, brightness, brightness, brightness, 1.0f, light, OverlayTexture.NO_OVERLAY);
                VertexConsumer damage = new SheetedDecalTextureGenerator(
                        damageConsumer,
                        matrixStack.last(), 1.0f
                );
                if (mobEntity.invulnerableTime > 0)
                    damage.putBulkData(matrixStack.last(), quad, brightness, brightness, brightness,1.0f, light, OverlayTexture.NO_OVERLAY);
            }

            matrixStack.popPose();
        }

        // top
        matrixStack.pushPose();
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - mobEntity.bodyRot));

        matrixStack.translate(-0.45, 0, -0.5);

        BakedModel model = ModelHelper.getModelFromIdentifier(MODEL_IDS[1]);

        List<BakedQuad> chairQuads = new ArrayList<>(Arrays.stream(Direction.values()).map(direction -> model.getQuads(null, direction, mobEntity.random))
                .flatMap(List::stream).toList());
        chairQuads.addAll(model.getQuads(null, null, mobEntity.random));


        for (BakedQuad quad : chairQuads) {
            float brightness = ModelHelper.getBrightness(quad.getDirection(), quad.isShade(), mobEntity.isDarkenedDim);

            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;

            if (mobEntity.color != null && quad.isTinted()) {
                int colorInt = mobEntity.color.getFireworkColor();
                red = ((colorInt >> 16) & 0xFF) / 255.0f;
                green = ((colorInt >> 8) & 0xFF) / 255.0f;
                blue = (colorInt & 0xFF) / 255.0f;
            }

            solid.putBulkData(matrixStack.last(), quad, red*brightness, green*brightness, blue*brightness, 1.0f, light, OverlayTexture.NO_OVERLAY);

            VertexConsumer damage = new SheetedDecalTextureGenerator(
                    damageConsumer,
                    matrixStack.last(), 1.0f
            );
            if (mobEntity.invulnerableTime > 0)
                damage.putBulkData(matrixStack.last(), quad, 1.0f, 1.0f, 1.0f, 1.0f, light, OverlayTexture.NO_OVERLAY);
        }

        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(OfficeChairEntityRenderState state) {
        return null;
    }

    @Override
    public void extractRenderState(OfficeChairEntity livingEntity, OfficeChairEntityRenderState livingEntityRenderState, float f) {
        super.extractRenderState(livingEntity, livingEntityRenderState, f);
        livingEntityRenderState.color = livingEntity.getPFMColor();
        livingEntityRenderState.health = livingEntity.getHealth();
        livingEntityRenderState.maxHealth = livingEntity.getMaxHealth();
        livingEntityRenderState.velocity = livingEntity.getDeltaMovement();
        livingEntityRenderState.invulnerableTime = livingEntity.invulnerableTime;
        livingEntityRenderState.wheelSpinAngle = livingEntity.getWheelSpinAngle();
        livingEntityRenderState.world = livingEntity.level();
        livingEntityRenderState.pos = livingEntity.position();
        livingEntityRenderState.random = livingEntity.getRandom();
        livingEntityRenderState.isDarkenedDim = !livingEntity.level().dimensionType().bedWorks();
    }
}
