package com.unlikepaladin.pfm.blocks.models.dinnerTable.fabric;

import com.unlikepaladin.pfm.blocks.DinnerTableBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;

import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricDinnerTableModel extends PFMFabricBakedModel {
    public FabricDinnerTableModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof DinnerTableBlock) {
            DinnerTableBlock block = (DinnerTableBlock) state.getBlock();
            Direction dir = state.get(DinnerTableBlock.FACING);
            boolean left = block.isTable(world, pos, dir.rotateYCounterclockwise(), dir);
            boolean right = block.isTable(world, pos, dir.rotateYClockwise(), dir);
            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            getTemplateBakedModels().get((0)).emitQuads(context, cullTest);
            context.popTransform();

            pushTextureTransform(context, spriteList.get(1));
            if (!left && right) {
                int index = dir == Direction.NORTH || dir == Direction.WEST ? 1 : 2;
                getTemplateBakedModels().get((index)).emitQuads(context, cullTest);
            }
            if (!right && left) {
                int index = dir == Direction.NORTH || dir == Direction.WEST ? 2 : 1;
                getTemplateBakedModels().get((index)).emitQuads(context, cullTest);
            }
            if (!right && !left) {
                getTemplateBakedModels().get((3)).emitQuads(context, cullTest);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Random randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        pushTextureTransform(context, getSpriteList(blockState).get(0));
        // base
        getTemplateBakedModels().get(0).emitQuads(context, anyPredicate);
        context.popTransform();

        pushTextureTransform(context, getSpriteList(blockState).get(1));
        // legs
        getTemplateBakedModels().get(3).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}