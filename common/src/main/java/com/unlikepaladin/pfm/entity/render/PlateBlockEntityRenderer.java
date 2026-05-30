package com.unlikepaladin.pfm.entity.render;

import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.PlateBlock;
import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PlateBlockEntityRenderer<T extends PlateBlockEntity> implements BlockEntityRenderer<T, PlateBlockEntityRenderer.PlateBlockEntityRenderState> {

    private static final float SCALE = 0.375f;
    private final ItemModelResolver itemModelManager;
    public PlateBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        itemModelManager = ctx.itemModelResolver();
    }

    @Override
    public void submit(PlateBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        // literally lol
        if (state != null) {
            Direction direction = state.blockState.getValue(PlateBlock.FACING);
            matrices.pushPose();
            Direction direction2 = Direction.from2DDataValue((direction.get2DDataValue()) % 4);
            float g = -direction2.toYRot();
            switch (direction) {
                case NORTH -> matrices.translate(0.5, 0.08, 0.65);
                case SOUTH -> matrices.translate(0.5, 0.08, 0.35);
                case WEST -> matrices.translate(0.65, 0.08, 0.5);
                case EAST -> matrices.translate(0.35, 0.08, 0.5);
            }
            int rot = 90;
            matrices.mulPose(Axis.YP.rotationDegrees(g));
            matrices.mulPose(Axis.XP.rotationDegrees(rot));
            if (state.itemId.toString().equals("sandwichable:sandwich")) {
                matrices.mulPose(Axis.XP.rotationDegrees(270.0f));
                matrices.translate(0.0, 0.11, 0.05);
            }
            state.itemRenderState.submit(matrices, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            matrices.popPose();
        }
    }

    @Override
    public void extractRenderState(T blockEntity, PlateBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getBlockState();
        ItemStack itemStack = blockEntity.getItemInPlate();
        state.itemRenderState = new ItemStackRenderState();
        itemModelManager.updateForTopItem(state.itemRenderState, itemStack, ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
        state.lightAbove = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
        state.itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
    }

    @Override
    public PlateBlockEntityRenderState createRenderState() {
        return new PlateBlockEntityRenderState();
    }

    public static class PlateBlockEntityRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        public ItemStackRenderState itemRenderState;
        public int lightAbove;
        public Identifier itemId;
    }
}
