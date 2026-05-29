package com.unlikepaladin.pfm.entity.render;

import com.mojang.math.Axis;
import com.unlikepaladin.pfm.blocks.InnerTrashcanBlock;
import com.unlikepaladin.pfm.blocks.TrashcanBlock;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TrashcanBlockEntityRenderer<T extends TrashcanBlockEntity> implements BlockEntityRenderer<T, TrashcanBlockEntityRenderer.TrashcanBlockEntityRenderState> {

    private final ItemModelResolver itemModelResolver;
    public TrashcanBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemModelResolver = ctx.itemModelResolver();
    }

    @Override
    public void submit(TrashcanBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (!(state.blockState.getBlock() instanceof TrashcanBlock)) {
            for (int i = 0; i < 9; i++)
            {
                Direction direction = state.blockState.getValue(InnerTrashcanBlock.FACING);
                matrices.pushPose();
                Direction direction2 = Direction.from2DDataValue((direction.get2DDataValue()) % 4);
                float g = -direction2.toYRot();
                switch (i) {
                    case 0: {
                        matrices.translate(0.5, 0.08, 0.3);
                        break;
                    }
                    case 1: {
                        matrices.translate(0.52, 0.2, 0.33);
                        break;
                    }
                    case 2: {
                        matrices.translate(0.5, 0.33, 0.3);
                        break;
                    }
                    case 3: {
                        matrices.translate(0.51, 0.31, 0.29);
                        break;
                    }
                    case 4: {
                        matrices.translate(0.48, 0.14, 0.31);
                        break;
                    }
                    case 5: {
                        matrices.translate(0.47, 0.44, 0.32);
                        break;
                    }
                    case 6: {
                        matrices.translate(0.47, 0.16, 0.28);
                        break;
                    }
                    case 7: {
                        matrices.translate(0.52, 0.43, 0.28);
                        break;
                    }
                    case 8: {
                        matrices.translate(0.49, 0.53, 0.31);
                        break;
                    }
                }
                Item item = state.items.get(i);
                if (!(item instanceof BlockItem)) {
                    matrices.translate(0.0, 0.0, 0.1);
                } else if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("pfm")) {
                    matrices.translate(0.0, 0.0, 0.15);
                }
                int rot = 90;
                matrices.mulPose(Axis.XP.rotationDegrees(rot));
                matrices.scale(0.8f, 0.8f, 0.8f);
                state.itemRenderStates.get(i).submit(matrices, queue, state.lightAbove, OverlayTexture.NO_OVERLAY, 0);
                matrices.popPose();
            }
        }
    }

    @Override
    public TrashcanBlockEntityRenderState createRenderState() {
        return new TrashcanBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, TrashcanBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getBlockState();
        state.itemRenderStates = new ArrayList<>();
        state.items = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemState, blockEntity.getItem(i), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
            state.itemRenderStates.add(itemState);
            state.items.add(blockEntity.getItem(i).getItem());
        }
        state.lightAbove = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
    }

    public static class TrashcanBlockEntityRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        public List<ItemStackRenderState> itemRenderStates;
        public List<Item> items;
        public int lightAbove;
    }
}
