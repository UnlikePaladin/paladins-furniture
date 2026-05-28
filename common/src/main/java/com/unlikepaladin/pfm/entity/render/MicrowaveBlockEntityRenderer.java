package com.unlikepaladin.pfm.entity.render;


import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.phys.Vec3;

public class MicrowaveBlockEntityRenderer<T extends MicrowaveBlockEntity> implements BlockEntityRenderer<T> {
    public ItemStack itemStack;
    private final ItemRenderer itemRenderer;
    private RecipePropertySet recipePropertySet;

    public MicrowaveBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(T blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {
        if (blockEntity instanceof MicrowaveBlockEntity) {
            itemStack = blockEntity.getItem(0);
            matrices.pushPose();
            if (recipePropertySet == null)
                recipePropertySet = blockEntity.getLevel().recipeAccess().propertySet(RecipePropertySet.SMOKER_INPUT);

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
            matrices.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
            if (blockEntity.isActive && recipePropertySet.test(itemStack)) {
                matrices.mulPose(Axis.YP.rotationDegrees((blockEntity.getLevel().getDayTime() + tickDelta) * 4));}
            matrices.scale(0.5f, 0.5f, 0.5f);
            this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, lightAbove, overlay, matrices, vertexConsumers, blockEntity.getLevel(), 0);
            matrices.popPose();
        }
    }



}
