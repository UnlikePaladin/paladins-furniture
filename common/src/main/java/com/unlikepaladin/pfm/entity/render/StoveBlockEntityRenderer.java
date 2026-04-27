package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;
import com.mojang.math.Vector3f;

@Environment(value= EnvType.CLIENT)
public class StoveBlockEntityRenderer<T extends BlockEntity>
        extends BlockEntityRenderer<T> {
    private static final float SCALE = 0.4f;
    public StoveBlockEntityRenderer(BlockEntityRenderDispatcher ctx) {
        super(ctx);
    }

    @Override
    public void render(BlockEntity blockEntity, float f, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i, int j) {
        if (blockEntity instanceof StoveBlockEntity) {
            StoveBlockEntity stoveBlockEntity = (StoveBlockEntity) blockEntity;
            Direction direction = stoveBlockEntity.getCachedState().get(KitchenStovetopBlock.FACING);
            DefaultedList<ItemStack> itemList = stoveBlockEntity.getItemsBeingCooked();
            for (int l = 0; l < itemList.size(); ++l) {
                ItemStack itemStack = itemList.get(l);
                if (itemStack == ItemStack.EMPTY) continue;
                matrices.pushPose();
                Direction direction2 = Direction.from2DDataValue((l + direction.get2DDataValue()) % 4);
                float g = -direction2.toYRot();
                int rot = 45;
                Direction dir = stoveBlockEntity.getCachedState().get(KitchenStovetopBlock.FACING);
                switch(dir) {
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
                        break;
                }
                rot = 180;
                matrices.mulPose(Vector3f.YP.rotationDegrees(g));
                matrices.mulPose(Vector3f.YP.rotationDegrees(rot));
                matrices.mulPose(Vector3f.XP.rotationDegrees(90.0f));
                matrices.translate(-0.16, -0.16, 0.0);
                matrices.scale(SCALE, SCALE, SCALE);
                Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, i, j, matrices, vertexConsumerProvider);
                matrices.popPose();
            }
        }
    }
}

