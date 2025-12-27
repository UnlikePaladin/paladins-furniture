package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.blocks.InnerTrashcanBlock;
import com.unlikepaladin.pfm.blocks.TrashcanBlock;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
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
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TrashcanBlockEntityRenderer<T extends TrashcanBlockEntity> implements BlockEntityRenderer<T, TrashcanBlockEntityRenderer.TrashcanBlockEntityRenderState> {

    private final ItemModelManager itemModelManager;
    public TrashcanBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemModelManager = ctx.itemModelManager();
    }

    @Override
    public void render(TrashcanBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {

        if (!(state.blockState.getBlock() instanceof TrashcanBlock)) {
            for (int i = 0; i < 9; i++)
            {
                Direction direction = state.blockState.get(InnerTrashcanBlock.FACING);
                matrices.push();
                Direction direction2 = Direction.fromHorizontalQuarterTurns((direction.getHorizontalQuarterTurns()) % 4);
                float g = -direction2.getPositiveHorizontalDegrees();
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
                } else if (Registries.ITEM.getId(item).getNamespace().equals("pfm")) {
                    matrices.translate(0.0, 0.0, 0.15);
                }
                int rot = 90;
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rot));
                matrices.scale(0.8f, 0.8f, 0.8f);
                state.itemRenderStates.get(i).render(matrices, queue, state.lightAbove, OverlayTexture.DEFAULT_UV, 0);
                matrices.pop();
            }
        }
    }

    @Override
    public TrashcanBlockEntityRenderState createRenderState() {
        return new TrashcanBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(T blockEntity, TrashcanBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.blockState = blockEntity.getCachedState();
        state.itemRenderStates = new ArrayList<>();
        state.items = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemRenderState itemState = new ItemRenderState();
            itemModelManager.clearAndUpdate(itemState, blockEntity.getStack(i), ItemDisplayContext.GROUND, blockEntity.getWorld(), null, 0);
            state.itemRenderStates.add(itemState);
            state.items.add(blockEntity.getStack(i).getItem());
        }
        state.lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
    }

    public static class TrashcanBlockEntityRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        public List<ItemRenderState> itemRenderStates;
        public List<Item> items;
        public int lightAbove;
    }
}
