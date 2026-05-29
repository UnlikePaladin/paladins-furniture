package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu;


import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.StoveBlockEntityBalm;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class StoveBlockEntityRendererBalm implements BlockEntityRenderer<StoveBlockEntityBalm, StoveBlockEntityRendererBalm.StoveBlockEntityRendererBalmRenderState> {
    public ItemModelResolver itemModelResolver;
    public StoveBlockEntityRendererBalm(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public void submit(StoveBlockEntityRendererBalmRenderState renderState, PoseStack poseStack, SubmitNodeCollector queue, CameraRenderState cameraState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180.0F));
        poseStack.translate(-0.5, 0.0, -0.5);
        poseStack.translate(0.0, 0.0, -1.0);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.0, 1.05, 0.0);
        poseStack.scale(0.4F, 0.4F, 0.4F);

        if (!renderState.firstTool.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.55F, 0.0F, 0.5F);
            poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
            renderState.firstTool.submit(poseStack, queue, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (!renderState.secondTool.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.55F, 0.0F, 0.5F);
            poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
            renderState.secondTool.submit(poseStack, queue, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (!renderState.thirdTool.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.55F, 0.0F, -0.5F);
            poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
            renderState.thirdTool.submit(poseStack, queue, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (!renderState.fourthTool.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.55F, 0.0F, -0.5F);
            poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
            renderState.fourthTool.submit(poseStack, queue, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    @Override
    public void extractRenderState(StoveBlockEntityBalm blockEntity, StoveBlockEntityRendererBalmRenderState renderState, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, tickProgress, cameraPos, crumblingOverlay);
        this.itemModelResolver.updateForTopItem(renderState.firstTool, blockEntity.getToolItem(0), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        this.itemModelResolver.updateForTopItem(renderState.secondTool, blockEntity.getToolItem(1), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        this.itemModelResolver.updateForTopItem(renderState.thirdTool, blockEntity.getToolItem(2), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        this.itemModelResolver.updateForTopItem(renderState.fourthTool, blockEntity.getToolItem(3), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        renderState.facing = blockEntity.getFacing();
    }

    @Override
    public StoveBlockEntityRendererBalmRenderState createRenderState() {
        return new StoveBlockEntityRendererBalmRenderState();
    }

    public static class StoveBlockEntityRendererBalmRenderState extends BlockEntityRenderState {
        public final ItemStackRenderState firstTool = new ItemStackRenderState();
        public final ItemStackRenderState secondTool = new ItemStackRenderState();
        public final ItemStackRenderState thirdTool = new ItemStackRenderState();
        public final ItemStackRenderState fourthTool = new ItemStackRenderState();
        public Direction facing = Direction.NORTH;
    }
}