package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Environment(value= EnvType.CLIENT)
public class StoveBlockEntityRenderer<T extends StoveBlockEntity>
        implements BlockEntityRenderer<T, StoveBlockEntityRenderer.StoveBlockEntityRenderState> {
    private static final float SCALE = 0.4f;
    private final ItemModelManager itemModelManager;
    public StoveBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemModelManager = ctx.itemModelManager();
    }

    @Override
    public void render(StoveBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state != null) {
            Direction direction = state.blockState.get(StoveBlock.FACING);
            List<ItemRenderState> itemList = state.itemRenderStates;
            for (int l = 0; l < itemList.size(); ++l) {
                matrices.push();
                Direction direction2 = Direction.fromHorizontalQuarterTurns((l + direction.getHorizontalQuarterTurns()) % 4);
                float g = -direction2.getPositiveHorizontalDegrees();
                int rot = 180;
                switch(direction) {
                    case NORTH:
                        matrices.translate(0.5, 1.02, 0.45);
                        break;
                    case SOUTH:
                        matrices.translate(0.5, 1.02, 0.55);
                        break;
                    case WEST:
                        matrices.translate(0.45, 1.02, 0.5);
                        break;
                    case EAST:
                        matrices.translate(0.5, 1.02, 0.5);
                }
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rot));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
                matrices.translate(-0.16, -0.16, 0.0);
                matrices.scale(SCALE, SCALE, SCALE);
                itemList.get(l).render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
                matrices.pop();
            }
        }
    }

    @Override
    public StoveBlockEntityRenderState createRenderState() {
        return new StoveBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(T blockEntity, StoveBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getCachedState();
        DefaultedList<ItemStack> itemList = blockEntity.getItemsBeingCooked();
        state.itemRenderStates = new ArrayList<>();
        for (ItemStack itemStack : itemList) {
            ItemRenderState itemRenderState = new ItemRenderState();
            itemModelManager.clearAndUpdate(itemRenderState, itemStack, ItemDisplayContext.FIXED, blockEntity.getWorld(), null, 0);
            state.itemRenderStates.add(itemRenderState);
        }
    }

    public static class StoveBlockEntityRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        public List<ItemRenderState> itemRenderStates;
    }
}

