package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.PlateBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.MapRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class PlateBlockEntityRenderer<T extends PlateBlockEntity> implements BlockEntityRenderer<T, PlateBlockEntityRenderer.PlateBlockEntityRenderState> {

    private static final float SCALE = 0.375f;
    private final ItemModelManager itemModelManager;
    public PlateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        itemModelManager = ctx.itemModelManager();
    }

    @Override
    public void render(PlateBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        // literally lol
        if (state != null) {
            Direction direction = state.blockState.get(PlateBlock.FACING);
            matrices.push();
            Direction direction2 = Direction.fromHorizontalQuarterTurns((direction.getHorizontalQuarterTurns()) % 4);
            float g = -direction2.getPositiveHorizontalDegrees();
            switch (direction) {
                case NORTH -> matrices.translate(0.5, 0.08, 0.65);
                case SOUTH -> matrices.translate(0.5, 0.08, 0.35);
                case WEST -> matrices.translate(0.65, 0.08, 0.5);
                case EAST -> matrices.translate(0.35, 0.08, 0.5);
            }
            int rot = 90;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rot));
            if (state.itemId.toString().equals("sandwichable:sandwich")) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270.0f));
                matrices.translate(0.0, 0.11, 0.05);
            }
            state.itemRenderState.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
            matrices.pop();
        }
    }

    @Override
    public void updateRenderState(T blockEntity, PlateBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getCachedState();
        ItemStack itemStack = blockEntity.getItemInPlate();
        state.itemRenderState = new ItemRenderState();
        itemModelManager.clearAndUpdate(state.itemRenderState, itemStack, ItemDisplayContext.GROUND, blockEntity.getWorld(), null, 0);
        state.lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
        state.itemId = Registries.ITEM.getId(itemStack.getItem());
    }

    @Override
    public PlateBlockEntityRenderState createRenderState() {
        return new PlateBlockEntityRenderState();
    }

    public static class PlateBlockEntityRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        public ItemRenderState itemRenderState;
        public int lightAbove;
        public Identifier itemId;
    }
}
