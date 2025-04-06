package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(value= EnvType.CLIENT)
public class StoveBlockEntityRenderer<T extends StoveBlockEntity>
        implements BlockEntityRenderer<T> {
    private static final float SCALE = 0.4f;
    public StoveBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(StoveBlockEntity stoveBlockEntity, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        if (stoveBlockEntity instanceof StoveBlockEntity) {
            Direction direction = stoveBlockEntity.getCachedState().get(StoveBlock.FACING);
            DefaultedList<ItemStack> itemList = stoveBlockEntity.getItemsBeingCooked();
            int k = (int)stoveBlockEntity.getPos().asLong();
            for (int l = 0; l < itemList.size(); ++l) {
                ItemStack itemStack = itemList.get(l);
                if (itemStack == ItemStack.EMPTY) continue;
                matrices.push();
                Direction direction2 = Direction.fromHorizontal((l + direction.getHorizontal()) % 4);
                float g = -direction2.asRotation();
                int rot = 180;
                switch(direction) {
                    case NORTH:
                        matrices.translate(0.5, 1.02, 0.45);
                        break;
                    case SOUTH:
                        matrices.translate(0.5, 1.02, 0.55);
                        break;
                    case WEST:
                        matrices.translate(0.45, 1.02, 0.5);
                        break;
                    case EAST:
                        matrices.translate(0.5, 1.02, 0.5);
                }
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rot));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
                matrices.translate(-0.16, -0.16, 0.0);
                matrices.scale(SCALE, SCALE, SCALE);
                MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, i, j, matrices, vertexConsumerProvider, k + l);
                matrices.pop();
            }
        }
    }
}

