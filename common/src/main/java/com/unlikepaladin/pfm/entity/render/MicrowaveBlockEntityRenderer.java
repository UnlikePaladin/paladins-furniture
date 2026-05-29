package com.unlikepaladin.pfm.entity.render;


import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MicrowaveBlockEntityRenderer<T extends MicrowaveBlockEntity> implements BlockEntityRenderer<T, MicrowaveBlockEntityRenderer.MicrowaveBlockEntityRenderState> {

    private final ItemModelResolver itemModelManager;
    public MicrowaveBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        itemModelManager = ctx.itemModelResolver();
    }

    @Override
    public void submit(MicrowaveBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (state != null) {
            matrices.pushPose();

            Direction facing = state.facing;
            float x,y,z;
            switch (facing) {
                case NORTH -> {
                    x = 0.625f;
                    y = 0.25f;
                    z = 0.57f;
                }
                case SOUTH -> {
                    x = 0.375f;
                    y = 0.25f;
                    z = 0.43f;
                }
                case WEST -> {
                    x = 0.57f;
                    y = 0.25f;
                    z = 0.375f;
                }
                case EAST -> {
                    x = 0.43f;
                    y = 0.25f;
                    z = 0.625f;
                }
                default -> throw new IllegalStateException("Unexpected value: " + facing);
            }
            matrices.translate(x, y ,z);
            matrices.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
            if (state.isActive && state.recipePropertySet.test(state.itemStack)) {
                matrices.mulPose(Axis.YP.rotationDegrees((state.rotationFactor)));
            }
            matrices.scale(0.5f, 0.5f, 0.5f);
            state.state0.submit(matrices, queue, state.lightAbove, OverlayTexture.NO_OVERLAY, 0);
            matrices.popPose();
        }
    }

    @Override
    public MicrowaveBlockEntityRenderState createRenderState() {
        return new MicrowaveBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, MicrowaveBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getBlockState();
        ItemStack itemStack = blockEntity.getItem(0);
        state.state0 = new ItemStackRenderState();
        this.itemModelManager.updateForTopItem(state.state0, itemStack, ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
        state.recipePropertySet = blockEntity.getLevel().recipeAccess().propertySet(RecipePropertySet.SMOKER_INPUT);
        state.lightAbove = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
        state.facing = blockEntity.getFacing();
        state.isActive = blockEntity.isActive;
        state.itemStack = itemStack;
        state.rotationFactor = blockEntity.getLevel().getGameTime() * 4f;
    }

    public static class MicrowaveBlockEntityRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        ItemStackRenderState state0;
        private RecipePropertySet recipePropertySet;
        int lightAbove;
        Direction facing;
        boolean isActive;
        public ItemStack itemStack;
        float rotationFactor;
    }

}
