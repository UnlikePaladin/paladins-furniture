package com.unlikepaladin.pfm.entity.render;

import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.PFMToasterBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PFMToasterBlockEntityRenderer <T extends PFMToasterBlockEntity> implements BlockEntityRenderer<T, PFMToasterBlockEntityRenderer.ToasterBlockEntityRenderState> {

    ItemModelResolver modelManager;
    public PFMToasterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.modelManager = context.itemModelResolver();
    }

    @Override
    public void submit(ToasterBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (state != null) {
            matrices.pushPose();
            Direction dir = Direction.NORTH;
            if (state.blockState.getBlock() instanceof PFMToasterBlock) {
                dir = Objects.requireNonNull(state.toasterFacing);
                if (state.isToasting || state.blockState.getValue(PFMToasterBlock.ON)) {
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

                matrices.mulPose(Axis.YP.rotationDegrees((float)rot));
                matrices.scale(0.8f,0.8f,0.8f);
                matrices.translate(0.0D, 0.0D, -0.55D);
                matrices.translate(0.0D, 0.0D, 0.41D);
                state.state0.submit(matrices, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                matrices.translate(0.0D, 0.0D, 0.29D);
                state.state1.submit(matrices, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                matrices.popPose();
            }
        }

    @Override
    public ToasterBlockEntityRenderState createRenderState() {
        return new ToasterBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, ToasterBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getBlockState();
        state.toasterFacing = blockEntity.getToasterFacing();
        state.isToasting = blockEntity.isToasting();
        state.state0 = new ItemStackRenderState();
        modelManager.updateForTopItem(state.state0, blockEntity.getItems().getFirst(), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);

        state.state1 = new ItemStackRenderState();
        modelManager.updateForTopItem(state.state1, blockEntity.getItems().get(1), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
    }

    public static class ToasterBlockEntityRenderState extends BlockEntityRenderState {
            public BlockState blockState;
            public Direction toasterFacing;
            boolean isToasting;
            ItemStackRenderState state0, state1;
    }

}
