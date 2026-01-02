package com.unlikepaladin.pfm.entity.render;


import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class MicrowaveBlockEntityRenderer<T extends MicrowaveBlockEntity> implements BlockEntityRenderer<T, MicrowaveBlockEntityRenderer.MicrowaveBlockEntityRenderState> {

    private final ItemModelManager itemModelManager;
    public MicrowaveBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        itemModelManager = ctx.itemModelManager();
    }

    @Override
    public void render(MicrowaveBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state != null) {
            matrices.push();

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
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.getPositiveHorizontalDegrees()));
            if (state.isActive && state.recipePropertySet.canUse(state.itemStack)) {
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.rotationFactor));
            }
            matrices.scale(0.5f, 0.5f, 0.5f);
            state.state0.render(matrices, queue, state.lightAbove, OverlayTexture.DEFAULT_UV, 0);
            matrices.pop();
        }
    }

    @Override
    public MicrowaveBlockEntityRenderState createRenderState() {
        return new MicrowaveBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(T blockEntity, MicrowaveBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getCachedState();
        ItemStack itemStack = blockEntity.getStack(0);
        state.state0 = new ItemRenderState();
        this.itemModelManager.clearAndUpdate(state.state0, itemStack, ItemDisplayContext.GROUND, blockEntity.getWorld(), null, 0);
        state.recipePropertySet = blockEntity.getWorld().getRecipeManager().getPropertySet(RecipePropertySet.SMOKER_INPUT);
        state.lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
        state.facing = blockEntity.getFacing();
        state.isActive = blockEntity.isActive;
        state.itemStack = itemStack;
        state.rotationFactor = blockEntity.getWorld().getTime() * 4f;
    }

    public static class MicrowaveBlockEntityRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        ItemRenderState state0;
        private RecipePropertySet recipePropertySet;
        int lightAbove;
        Direction facing;
        boolean isActive;
        public ItemStack itemStack;
        float rotationFactor;
    }

}
