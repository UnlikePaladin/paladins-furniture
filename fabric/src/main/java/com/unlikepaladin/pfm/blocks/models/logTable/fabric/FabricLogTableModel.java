package com.unlikepaladin.pfm.blocks.models.logTable.fabric;

import com.unlikepaladin.pfm.blocks.LogTableBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FabricLogTableModel extends PFMFabricBakedModel {
    public FabricLogTableModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof LogTableBlock) {
            LogTableBlock block = (LogTableBlock) state.getBlock();
            Direction dir = state.get(LogTableBlock.FACING);
            boolean left = block.isTable(world, pos, dir.rotateYCounterclockwise(), dir);
            boolean right = block.isTable(world, pos, dir.rotateYClockwise(), dir);
            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            ((FabricBakedModel) getTemplateBakedModels().get((0))).emitBlockQuads(world, state, pos, randomSupplier, context);
            context.popTransform();

            pushTextureTransform(context, spriteList.get(1));
            if (!left && right) {
                int index = dir == Direction.NORTH || dir == Direction.WEST ? 1 : 2;
                ((FabricBakedModel) getTemplateBakedModels().get((index))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (!right && left) {
                int index = dir == Direction.NORTH || dir == Direction.WEST ? 2 : 1;
                ((FabricBakedModel) getTemplateBakedModels().get((index))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (!right && !left) {
                ((FabricBakedModel) getTemplateBakedModels().get((3))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        if (stack.getItem() instanceof BlockItem) {
            pushTextureTransform(context, getSpriteList(stack).get(0));
            // base
            ((FabricBakedModel) getTemplateBakedModels().get(0)).emitItemQuads(stack, randomSupplier, context);
            context.popTransform();

            pushTextureTransform(context, getSpriteList(stack).get(1));
            // legs
            ((FabricBakedModel) getTemplateBakedModels().get(3)).emitItemQuads(stack, randomSupplier, context);
            context.popTransform();
        }
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}