package com.unlikepaladin.pfm.entity.render;

import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.PFMToasterBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;

import java.util.Objects;

public class PFMToasterBlockEntityRenderer <T extends PFMToasterBlockEntity> implements BlockEntityRenderer<T> {

        public PFMToasterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        }
        @Override
        public void render(T blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
            if (blockEntity instanceof PFMToasterBlockEntity) {
                matrices.pushPose();
                NonNullList<ItemStack> items = blockEntity.getItems();
                Direction dir = Direction.NORTH;
                if (blockEntity.getBlockState().getBlock() instanceof PFMToasterBlock) {
                    dir = Objects.requireNonNull(blockEntity.getToasterFacing());
                    if (blockEntity.isToasting() || blockEntity.getBlockState().getValue(PFMToasterBlock.ON)) {
                        matrices.translate(0.0D, -0.11D, 0.0D);
                    }
                }

                matrices.translate(0.5D, 0.3D, 0.5D);
                int rot = 45;
                switch(dir) {
                    case NORTH:
                        rot = 270;
                        break;
                    case SOUTH:
                        rot = 90;
                        break;
                    case WEST:
                        rot = 0;
                        break;
                    case EAST:
                        rot = 180;
                }

                matrices.mulPose(Axis.YP.rotationDegrees((float)rot));
                matrices.scale(0.8f,0.8f,0.8f);
                matrices.translate(0.0D, 0.0D, -0.55D);
                matrices.translate(0.0D, 0.0D, 0.41D);
                Minecraft.getInstance().getItemRenderer().renderStatic(items.get(0), ItemTransforms.TransformType.GROUND, light, overlay, matrices, vertexConsumers, 346746554);
                matrices.translate(0.0D, 0.0D, 0.29D);
                Minecraft.getInstance().getItemRenderer().renderStatic(items.get(1), ItemTransforms.TransformType.GROUND, light, overlay, matrices, vertexConsumers, 834871346);
                matrices.popPose();
            }
        }

}
