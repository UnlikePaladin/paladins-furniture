package com.unlikepaladin.pfm.blocks.models.logTable.fabric;

import com.unlikepaladin.pfm.blocks.LogTableBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricLogTableModel extends PFMFabricBakedModel {
    public FabricLogTableModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof LogTableBlock) {
            LogTableBlock block = (LogTableBlock) state.getBlock();
            Direction dir = state.getValue(LogTableBlock.FACING);
            boolean left = block.isTable(world, pos, dir.getCounterClockWise(), dir);
            boolean right = block.isTable(world, pos, dir.getClockWise(), dir);
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
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
    public void emitItemQuads(QuadEmitter context, RandomSource randomSupplier) {
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
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}