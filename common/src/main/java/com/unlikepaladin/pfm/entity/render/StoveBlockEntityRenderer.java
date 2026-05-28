package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;

@Environment(value= EnvType.CLIENT)
public class StoveBlockEntityRenderer<T extends StoveBlockEntity>
        implements BlockEntityRenderer<T> {
    private static final float SCALE = 0.4f;
    private final ItemRenderer itemRenderer;
    public StoveBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(StoveBlockEntity stoveBlockEntity, float f, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i, int j, Vec3 cameraPos) {
        if (stoveBlockEntity instanceof StoveBlockEntity) {
            Direction direction = stoveBlockEntity.getBlockState().getValue(StoveBlock.FACING);
            NonNullList<ItemStack> itemList = stoveBlockEntity.getItemsBeingCooked();
            int k = (int)stoveBlockEntity.getBlockPos().asLong();
            for (int l = 0; l < itemList.size(); ++l) {
                ItemStack itemStack = itemList.get(l);
                if (itemStack == ItemStack.EMPTY) continue;
                matrices.pushPose();
                Direction direction2 = Direction.from2DDataValue((l + direction.get2DDataValue()) % 4);
                float g = -direction2.toYRot();
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
                matrices.mulPose(Axis.YP.rotationDegrees(g));
                matrices.mulPose(Axis.YP.rotationDegrees(rot));
                matrices.mulPose(Axis.XP.rotationDegrees(90.0f));
                matrices.translate(-0.16, -0.16, 0.0);
                matrices.scale(SCALE, SCALE, SCALE);
                itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, i, j, matrices, vertexConsumerProvider, stoveBlockEntity.getLevel(),k + l);
                matrices.popPose();
            }
        }
    }
}

