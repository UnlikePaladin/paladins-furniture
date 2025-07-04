package com.unlikepaladin.pfm.blocks.models.fridge.neoforge;

import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFreezerBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.*;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class NeoForgeIronFridgeModel extends PFMNeoForgeBakedModel {
    private final List<String> modelParts;

    public NeoForgeIronFridgeModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof IronFridgeBlock))
            return;

        boolean bottom = state.isOf(world.getBlockState(pos.up()).getBlock());
        boolean top = state.isOf(world.getBlockState(pos.down()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.down()).getBlock() instanceof IronFreezerBlock;
        int openOffset = state.get(FridgeBlock.OPEN) ? 5 : 0;
        if (top && bottom) {
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
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        return List.of();
    }
}