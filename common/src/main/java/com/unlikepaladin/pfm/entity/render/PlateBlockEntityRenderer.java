package com.unlikepaladin.pfm.entity.render;

import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.PlateBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;

public class PlateBlockEntityRenderer<T extends PlateBlockEntity> implements BlockEntityRenderer<T> {
    public ItemStack itemStack;
    private static final float SCALE = 0.375f;
    public PlateBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }
    @Override
    public void render(PlateBlockEntity plateBlockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
        if (plateBlockEntity instanceof PlateBlockEntity) {
            Direction direction = plateBlockEntity.getBlockState().getValue(PlateBlock.FACING);
            itemStack = plateBlockEntity.getItemInPlate();
            matrices.pushPose();
            Direction direction2 = Direction.from2DDataValue((direction.get2DDataValue()) % 4);
            float g = -direction2.toYRot();
            Direction dir = plateBlockEntity.getBlockState().getValue(PlateBlock.FACING);
            switch (dir) {
                case NORTH -> matrices.translate(0.5, 0.08, 0.65);
                case SOUTH -> matrices.translate(0.5, 0.08, 0.35);
                case WEST -> matrices.translate(0.65, 0.08, 0.5);
                case EAST -> matrices.translate(0.35, 0.08, 0.5);
            }
            int rot = 90;
            matrices.mulPose(Axis.YP.rotationDegrees(g));
            matrices.mulPose(Axis.XP.rotationDegrees(rot));
            if (BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString().equals("sandwichable:sandwich")) {
                matrices.mulPose(Axis.XP.rotationDegrees(270.0f));
                matrices.translate(0.0, 0.11, 0.05);
            }
            int lightAbove = LevelRenderer.getLightColor(plateBlockEntity.getLevel(), plateBlockEntity.getBlockPos().above());
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.GROUND, lightAbove, OverlayTexture.NO_OVERLAY, matrices, vertexConsumerProvider, 0);
            matrices.popPose();
        }
    }
}
