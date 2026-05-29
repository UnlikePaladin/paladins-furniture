package com.unlikepaladin.pfm.blocks.models.fridge.neoforge;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.RandomSource;

public class NeoForgeFridgeModel extends PFMNeoForgeBakedModel {
    private final List<String> modelParts;
    public NeoForgeFridgeModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof FridgeBlock))
            return;

        boolean bottom = state.is(world.getBlockState(pos.above()).getBlock());
        boolean top = state.is(world.getBlockState(pos.below()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.above()).getBlock() instanceof FreezerBlock && !(world.getBlockState(pos.above()).getBlock() instanceof IronFridgeBlock);
        int openOffset = state.getValue(FridgeBlock.OPEN) ? 6 : 0;
        if (top && hasFreezer) {
            parts.add(getTemplateBakedModels().get(5+openOffset));
        }
        else if (top && bottom) {
            parts.add(getTemplateBakedModels().get(2+openOffset));
        } else if (bottom) {
            parts.add(getTemplateBakedModels().get(3+openOffset));
        } else if (top) {
            parts.add(getTemplateBakedModels().get(1+openOffset));
        } else if (hasFreezer) {
            parts.add(getTemplateBakedModels().get(4+openOffset));
        } else {
            parts.add(getTemplateBakedModels().get(openOffset));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        return List.of();
    }
}
