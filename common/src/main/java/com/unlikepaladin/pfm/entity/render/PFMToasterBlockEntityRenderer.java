package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.PFMToasterBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PFMToasterBlockEntityRenderer <T extends PFMToasterBlockEntity> implements BlockEntityRenderer<T, PFMToasterBlockEntityRenderer.ToasterBlockEntityRenderState> {

    ItemModelManager modelManager;
    public PFMToasterBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.modelManager = context.itemModelManager();
    }

    @Override
    public void render(ToasterBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state != null) {
            matrices.push();
            Direction dir = Direction.NORTH;
            if (state.blockState.getBlock() instanceof PFMToasterBlock) {
                dir = Objects.requireNonNull(state.toasterFacing);
                if (state.isToasting || state.blockState.get(PFMToasterBlock.ON)) {
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

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)rot));
            matrices.scale(0.8f,0.8f,0.8f);
            matrices.translate(0.0D, 0.0D, -0.55D);
            matrices.translate(0.0D, 0.0D, 0.41D);
            state.state0.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            matrices.translate(0.0D, 0.0D, 0.29D);
            state.state1.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            matrices.pop();
        }
    }

    @Override
    public ToasterBlockEntityRenderState createRenderState() {
        return new ToasterBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(T blockEntity, ToasterBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getCachedState();
        state.toasterFacing = blockEntity.getToasterFacing();
        state.isToasting = blockEntity.isToasting();
        state.state0 = new ItemRenderState();
        modelManager.clearAndUpdate(state.state0, blockEntity.getItems().getFirst(), ItemDisplayContext.GROUND, blockEntity.getWorld(), null, 0);

        state.state1 = new ItemRenderState();
        modelManager.clearAndUpdate(state.state1, blockEntity.getItems().get(1), ItemDisplayContext.GROUND, blockEntity.getWorld(), null, 0);
    }

    public static class ToasterBlockEntityRenderState extends BlockEntityRenderState {
            public BlockState blockState;
            public Direction toasterFacing;
            boolean isToasting;
            ItemRenderState state0, state1;
    }

}
