package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.PFMToasterBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

import java.util.Objects;

public class PFMToasterBlockEntityRenderer <T extends PFMToasterBlockEntity> extends BlockEntityRenderer<T> {

        public PFMToasterBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
            super(blockEntityRenderDispatcher);
        }
        @Override
        public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            matrices.push();
            DefaultedList<ItemStack> items = blockEntity.getItems();
            Direction dir = Direction.NORTH;
            if (blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock() instanceof PFMToasterBlock) {
                dir = Objects.requireNonNull(blockEntity.getToasterFacing());
                if (blockEntity.isToasting() || blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(PFMToasterBlock.ON)) {
                    matrices.translate(0.0D, -0.11D, 0.0D);
                }
            }

            matrices.translate(0.5D, 0.2D, 0.5D);
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

            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float)rot));
            matrices.scale(0.8f,0.8f,0.8f);
            matrices.translate(0.0D, 0.0D, -0.55D);
            matrices.translate(0.0D, 0.0D, 0.41D);
            MinecraftClient.getInstance().getItemRenderer().renderItem(items.get(0), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
            matrices.translate(0.0D, 0.0D, 0.29D);
            MinecraftClient.getInstance().getItemRenderer().renderItem(items.get(1), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
            matrices.pop();
        }

}
