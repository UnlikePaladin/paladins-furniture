package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu;

import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.StoveBlockEntityBalm;
import net.blay09.mods.cookingforblockheads.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Quaternion;
import net.minecraft.world.level.Level;

public class StoveBlockEntityRendererBalm implements BlockEntityRenderer<StoveBlockEntityBalm> {

    public StoveBlockEntityRendererBalm(BlockEntityRendererProvider.Context context) {
    }

    public void render(StoveBlockEntityBalm blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            poseStack.pushPose();
            RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
            poseStack.translate(-0.5, 0.0, -0.5);
            poseStack.translate(0.0, 0.0, -1.0);
            poseStack.popPose();
            poseStack.pushPose();
            poseStack.translate(0.0, 1.05, 0.0);
            RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
            poseStack.scale(0.4F, 0.4F, 0.4F);

            ItemStack itemStack = blockEntity.getToolItem(0);
            if (!itemStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(-0.550000011920929, 0.0, 0.5);
                poseStack.mulPose(new Quaternion(45.0F, 0.0F, 0.0F, true));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                poseStack.popPose();
            }

            itemStack = blockEntity.getToolItem(1);
            if (!itemStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.550000011920929, 0.0, 0.5);
                poseStack.mulPose(new Quaternion(45.0F, 0.0F, 0.0F, true));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                poseStack.popPose();
            }

            itemStack = blockEntity.getToolItem(2);
            if (!itemStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(-0.550000011920929, 0.0, -0.5);
                poseStack.mulPose(new Quaternion(45.0F, 0.0F, 0.0F, true));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                poseStack.popPose();
            }

            itemStack = blockEntity.getToolItem(3);
            if (!itemStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.550000011920929, 0.0, -0.5);
                poseStack.mulPose(new Quaternion(45.0F, 0.0F, 0.0F, true));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                poseStack.popPose();
            }
            poseStack.popPose();
        }
    }
}