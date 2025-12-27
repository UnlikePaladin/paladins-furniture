package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu;


import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.StoveBlockEntityBalm;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;


public class StoveBlockEntityRendererBalm implements BlockEntityRenderer<StoveBlockEntityBalm, StoveBlockEntityRendererBalm.StoveBlockEntityRendererBalmRenderState> {
    public ItemModelManager itemModelResolver;
    public StoveBlockEntityRendererBalm(BlockEntityRendererFactory.Context context) {
        this.itemModelResolver = context.itemModelManager();
    }

    @Override
    public void render(StoveBlockEntityRendererBalmRenderState renderState, MatrixStack poseStack, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        poseStack.push();
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-renderState.facing.getPositiveHorizontalDegrees() + 180.0F));
        poseStack.translate(-0.5, 0.0, -0.5);
        poseStack.translate(0.0, 0.0, -1.0);
        poseStack.pop();
        poseStack.push();
        poseStack.translate(0.0, 1.05, 0.0);
        poseStack.scale(0.4F, 0.4F, 0.4F);

        if (!renderState.firstTool.isEmpty()) {
            poseStack.push();
            poseStack.translate(-0.55F, 0.0F, 0.5F);
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
            renderState.firstTool.render(poseStack, queue, renderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            poseStack.pop();
        }

        if (!renderState.secondTool.isEmpty()) {
            poseStack.push();
            poseStack.translate(0.55F, 0.0F, 0.5F);
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
            renderState.secondTool.render(poseStack, queue, renderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            poseStack.pop();
        }

        if (!renderState.thirdTool.isEmpty()) {
            poseStack.push();
            poseStack.translate(-0.55F, 0.0F, -0.5F);
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
            renderState.thirdTool.render(poseStack, queue, renderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            poseStack.pop();
        }

        if (!renderState.fourthTool.isEmpty()) {
            poseStack.push();
            poseStack.translate(0.55F, 0.0F, -0.5F);
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
            renderState.fourthTool.render(poseStack, queue, renderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            poseStack.pop();
        }
        poseStack.pop();
    }

    @Override
    public void updateRenderState(StoveBlockEntityBalm blockEntity, StoveBlockEntityRendererBalmRenderState renderState, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, renderState, tickProgress, cameraPos, crumblingOverlay);
        this.itemModelResolver.clearAndUpdate(renderState.firstTool, blockEntity.getToolItem(0), ItemDisplayContext.FIXED, blockEntity.getWorld(), null, 0);
        this.itemModelResolver.clearAndUpdate(renderState.secondTool, blockEntity.getToolItem(1), ItemDisplayContext.FIXED, blockEntity.getWorld(), null, 0);
        this.itemModelResolver.clearAndUpdate(renderState.thirdTool, blockEntity.getToolItem(2), ItemDisplayContext.FIXED, blockEntity.getWorld(), null, 0);
        this.itemModelResolver.clearAndUpdate(renderState.fourthTool, blockEntity.getToolItem(3), ItemDisplayContext.FIXED, blockEntity.getWorld(), null, 0);
        renderState.facing = blockEntity.getFacing();
    }

    @Override
    public StoveBlockEntityRendererBalmRenderState createRenderState() {
        return new StoveBlockEntityRendererBalmRenderState();
    }

    public static class StoveBlockEntityRendererBalmRenderState extends BlockEntityRenderState {
        public final ItemRenderState firstTool = new ItemRenderState();
        public final ItemRenderState secondTool = new ItemRenderState();
        public final ItemRenderState thirdTool = new ItemRenderState();
        public final ItemRenderState fourthTool = new ItemRenderState();
        public Direction facing = Direction.NORTH;
    }
}