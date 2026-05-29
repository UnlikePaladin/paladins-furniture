package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;
import com.mojang.math.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Environment(value= EnvType.CLIENT)
public class StoveBlockEntityRenderer<T extends StoveBlockEntity>
        implements BlockEntityRenderer<T, StoveBlockEntityRenderer.StoveBlockEntityRenderState> {
    private static final float SCALE = 0.4f;
    private final ItemModelResolver itemModelManager;
    public StoveBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemModelManager = ctx.itemModelResolver();
    }

    @Override
    public void submit(StoveBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (state != null) {
            Direction direction = state.blockState.getValue(StoveBlock.FACING);
            List<ItemStackRenderState> itemList = state.itemRenderStates;
            for (int l = 0; l < itemList.size(); ++l) {
                matrices.pushPose();
                Direction direction2 = Direction.from2DDataValue((l + direction.get2DDataValue()) % 4);
                float g = -direction2.toYRot();
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
                matrices.mulPose(Axis.YP.rotationDegrees(g));
                matrices.mulPose(Axis.YP.rotationDegrees(rot));
                matrices.mulPose(Axis.XP.rotationDegrees(90.0f));
                matrices.translate(-0.16, -0.16, 0.0);
                matrices.scale(SCALE, SCALE, SCALE);
                itemList.get(l).submit(matrices, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                matrices.popPose();
            }
        }
    }

    @Override
    public StoveBlockEntityRenderState createRenderState() {
        return new StoveBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, StoveBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getBlockState();
        NonNullList<ItemStack> itemList = blockEntity.getItemsBeingCooked();
        state.itemRenderStates = new ArrayList<>();
        for (ItemStack itemStack : itemList) {
            ItemStackRenderState itemRenderState = new ItemStackRenderState();
            itemModelManager.updateForTopItem(itemRenderState, itemStack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
            state.itemRenderStates.add(itemRenderState);
        }
    }

    public static class StoveBlockEntityRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        public List<ItemStackRenderState> itemRenderStates;
    }
}

