package com.unlikepaladin.pfm.entity.render;


import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import com.mojang.math.Vector3f;

public class MicrowaveBlockEntityRenderer<T extends MicrowaveBlockEntity> implements BlockEntityRenderer<T> {
    public ItemStack itemStack;
    public MicrowaveBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(T blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (blockEntity instanceof MicrowaveBlockEntity) {
            itemStack = blockEntity.getItem(0);
            matrices.pushPose();
            int lightAbove = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
            Direction facing = blockEntity.getFacing();
            float x,y,z;
            switch (facing) {
                case NORTH -> {
                    x = 0.625f;
                    y = 0.25f;
                    z = 0.57f;
                }
                case SOUTH -> {
                    x = 0.375f;
                    y = 0.25f;
                    z = 0.43f;
                }
                case WEST -> {
                    x = 0.57f;
                    y = 0.25f;
                    z = 0.375f;
                }
                case EAST -> {
                    x = 0.43f;
                    y = 0.25f;
                    z = 0.625f;
                }
                default -> throw new IllegalStateException("Unexpected value: " + facing);
            }
            matrices.translate(x, y ,z);
            matrices.multiply(Axis.POSITIVE_Y.rotationDegrees(-facing.toYRot()));
            if (blockEntity.isActive && MicrowaveBlockEntity.canAcceptRecipeOutput(blockEntity.getRecipe(), blockEntity.container, blockEntity.getMaxStackSize())) {
                matrices.multiply(Axis.POSITIVE_Y.rotationDegrees((blockEntity.getWorld().getTime() + tickDelta) * 4));}
            matrices.scale(0.5f, 0.5f, 0.5f);
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.GROUND, lightAbove, overlay, matrices, vertexConsumers, 0);
            matrices.popPose();
        }
    }



}
