package com.unlikepaladin.pfm.blocks.models.modernDinnerTable.fabric;

import com.unlikepaladin.pfm.blocks.ModernDinnerTableBlock;
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

public class FabricModernDinnerTableModel extends PFMFabricBakedModel {
    public FabricModernDinnerTableModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelList) {
        super(settings, modelSettings, modelList);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof ModernDinnerTableBlock) {
            ModernDinnerTableBlock block = (ModernDinnerTableBlock) state.getBlock();
            Direction.Axis dir = state.getValue(ModernDinnerTableBlock.AXIS);
            boolean left = block.isTable(world, pos, dir, -1);
            boolean right = block.isTable(world, pos, dir, 1);
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            getTemplateBakedModels().get((0)).emitQuads(context, cullTest);
            context.popTransform();

            pushTextureTransform(context, spriteList.get(1));
            if (left && right) {
                getTemplateBakedModels().get(4).emitQuads(context, cullTest);
            }
            if (!left && right) {
                getTemplateBakedModels().get((1)).emitQuads(context, cullTest);
            }
            if (!right && left) {
                getTemplateBakedModels().get((2)).emitQuads(context, cullTest);
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