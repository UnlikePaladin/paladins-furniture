package com.unlikepaladin.pfm.entity.render;

import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.InnerTrashcanBlock;
import com.unlikepaladin.pfm.blocks.TrashcanBlock;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;

public class TrashcanBlockEntityRenderer<T extends TrashcanBlockEntity> implements BlockEntityRenderer<T> {
    public ItemStack itemStack;
    private final ItemRenderer itemRenderer;
    public TrashcanBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(T trashcanBlockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
        if (!(trashcanBlockEntity.getBlockState().getBlock() instanceof TrashcanBlock) && trashcanBlockEntity instanceof TrashcanBlockEntity) {
            for (int i = 0; i < 9; i++)
            {
                Direction direction = trashcanBlockEntity.getBlockState().getValue(InnerTrashcanBlock.FACING);
                matrices.pushPose();
                Direction direction2 = Direction.from2DDataValue((direction.get2DDataValue()) % 4);
                float g = -direction2.toYRot();
                itemStack = trashcanBlockEntity.getItem(i);
                switch (i) {
                    case 0: {
                        matrices.translate(0.5, 0.08, 0.3);
                        break;
                    }
                    case 1: {
                        matrices.translate(0.52, 0.2, 0.33);
                        break;
                    }
                    case 2: {
                        matrices.translate(0.5, 0.33, 0.3);
                        break;
                    }
                    case 3: {
                        matrices.translate(0.51, 0.31, 0.29);
                        break;
                    }
                    case 4: {
                        matrices.translate(0.48, 0.14, 0.31);
                        break;
                    }
                    case 5: {
                        matrices.translate(0.47, 0.44, 0.32);
                        break;
                    }
                    case 6: {
                        matrices.translate(0.47, 0.16, 0.28);
                        break;
                    }
                    case 7: {
                        matrices.translate(0.52, 0.43, 0.28);
                        break;
                    }
                    case 8: {
                        matrices.translate(0.49, 0.53, 0.31);
                        break;
                    }
                }
                if (!(itemStack.getItem() instanceof BlockItem)) {
                    matrices.translate(0.0, 0.0, 0.1);
                } else if (BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getNamespace().equals("pfm")) {
                    matrices.translate(0.0, 0.0, 0.15);
                }
                int rot = 90;
                matrices.mulPose(Axis.XP.rotationDegrees(rot));
                int lightAbove = LevelRenderer.getLightColor(trashcanBlockEntity.getLevel(), trashcanBlockEntity.getBlockPos().above());
                matrices.scale(0.8f, 0.8f, 0.8f);
                this.itemRenderer.renderItem(itemStack, ItemDisplayContext.GROUND, lightAbove, overlay, matrices, vertexConsumerProvider, trashcanBlockEntity.getWorld(), (int) (trashcanBlockEntity.getPos().asLong()+ i));
                matrices.popPose();
            }
        }
    }
}
