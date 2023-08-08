package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu;

import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.StoveBlockEntityBalm;
import net.blay09.mods.cookingforblockheads.client.render.RenderUtils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Quaternion;
import net.minecraft.world.World;

import java.util.Random;

public class StoveBlockEntityRendererBalm extends BlockEntityRenderer<StoveBlockEntityBalm> {
    private static final Random random = new Random();

    public StoveBlockEntityRendererBalm(BlockEntityRenderDispatcher context) {
        super(context);
    }

    public void render(StoveBlockEntityBalm blockEntity, float partialTicks, MatrixStack poseStack, VertexConsumerProvider buffer, int combinedLight, int combinedOverlay) {
        World level = blockEntity.getWorld();
        if (level != null) {
            poseStack.push();
            RenderUtils.applyBlockAngle(poseStack, blockEntity.getCachedState());
            poseStack.translate(-0.5, 0.0, -0.5);
            poseStack.translate(0.0, 0.0, -1.0);
            poseStack.pop();
            poseStack.push();
            poseStack.translate(0.0, 1.05, 0.0);
            RenderUtils.applyBlockAngle(poseStack, blockEntity.getCachedState());
            poseStack.scale(0.4F, 0.4F, 0.4F);

            ItemStack itemStack = blockEntity.getToolItem(0);
            if (!itemStack.isEmpty()) {
                poseStack.push();
                poseStack.translate(-0.550000011920929, 0.0, 0.5);
                poseStack.multiply(new Quaternion(45.0F, 0.0F, 0.0F, true));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                poseStack.pop();
            }

            itemStack = blockEntity.getToolItem(1);
            if (!itemStack.isEmpty()) {
                poseStack.push();
                poseStack.translate(0.550000011920929, 0.0, 0.5);
                poseStack.multiply(new Quaternion(45.0F, 0.0F, 0.0F, true));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                poseStack.pop();
            }

            itemStack = blockEntity.getToolItem(2);
            if (!itemStack.isEmpty()) {
                poseStack.push();
                poseStack.translate(-0.550000011920929, 0.0, -0.5);
                poseStack.multiply(new Quaternion(45.0F, 0.0F, 0.0F, true));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                poseStack.pop();
            }

            itemStack = blockEntity.getToolItem(3);
            if (!itemStack.isEmpty()) {
                poseStack.push();
                poseStack.translate(0.550000011920929, 0.0, -0.5);
                poseStack.multiply(new Quaternion(45.0F, 0.0F, 0.0F, true));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                poseStack.pop();
            }
            poseStack.pop();
        }
    }
}