package com.unlikepaladin.pfm.compat.fabric.sandwichable.client.render;

import com.unlikepaladin.pfm.compat.fabric.sandwichable.blocks.blockentities.PFMToasterBlockEntity;
import io.github.foundationgames.sandwichable.blocks.ToasterBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

import java.util.Objects;

public class PFMToasterBlockEntityRenderer implements BlockEntityRenderer<PFMToasterBlockEntity> {

        public PFMToasterBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        }
        @Override
        public void render(PFMToasterBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            matrices.push();
            DefaultedList<ItemStack> items = blockEntity.getItems();
            Direction dir = Direction.NORTH;
            if (blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock() instanceof ToasterBlock) {
                dir = Objects.requireNonNull(blockEntity.getToasterFacing());
                if (blockEntity.isToasting() || blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(ToasterBlock.ON)) {
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

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)rot));
            matrices.scale(0.8f,0.8f,0.8f);
            matrices.translate(0.0D, 0.0D, -0.55D);
            matrices.translate(0.0D, 0.0D, 0.41D);
            MinecraftClient.getInstance().getItemRenderer().renderItem(items.get(0), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 346746554);
            matrices.translate(0.0D, 0.0D, 0.29D);
            MinecraftClient.getInstance().getItemRenderer().renderItem(items.get(1), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 834871346);
            matrices.pop();
        }

}
