package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.blocks.blockentities.StovetopBlockEntity;
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
public class StovetopBlockEntityRenderer<T extends StovetopBlockEntity>
        implements BlockEntityRenderer<T> {
    private static final float SCALE = 0.375f;
    private final ItemRenderer itemRenderer;

    public StovetopBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(StovetopBlockEntity stovetopBlockEntity, float f, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i, int j, Vec3 cameraPos) {
        if (stovetopBlockEntity instanceof StovetopBlockEntity) {
            Direction direction = stovetopBlockEntity.getBlockState().getValue(KitchenStovetopBlock.FACING);
            NonNullList<ItemStack> itemList = stovetopBlockEntity.getItemsBeingCooked();
            int k = (int)stovetopBlockEntity.getBlockPos().asLong();
            for (int l = 0; l < itemList.size(); ++l) {
                ItemStack itemStack = itemList.get(l);
                if (itemStack == ItemStack.EMPTY) continue;
                matrices.pushPose();
                Direction direction2 = Direction.from2DDataValue((l + direction.get2DDataValue()) % 4);
                float g = -direction2.toYRot();
                int rot = 180;
                switch (direction) {
                    case NORTH:
                        matrices.translate(0.5, 0.08, 0.45);
                        break;
                    case SOUTH:
                        matrices.translate(0.5, 0.08, 0.55);
                        break;
                    case WEST:
                        matrices.translate(0.45, 0.08, 0.5);
                        break;
                    case EAST:
                        matrices.translate(0.55, 0.08, 0.5);
                }
                matrices.mulPose(Axis.YP.rotationDegrees(g));
                matrices.mulPose(Axis.YP.rotationDegrees(rot));
                matrices.mulPose(Axis.XP.rotationDegrees(90.0f));
                matrices.translate(-0.1625, -0.1625, 0.0);
                matrices.scale(0.355f, 0.355f, 0.355f);
                this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, i, j, matrices, vertexConsumerProvider,stovetopBlockEntity.getLevel(), k + l);
                matrices.popPose();
            }
        }
    }
}

