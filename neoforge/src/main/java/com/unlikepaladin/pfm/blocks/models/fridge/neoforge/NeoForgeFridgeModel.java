package com.unlikepaladin.pfm.blocks.models.fridge.neoforge;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
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
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class NeoForgeFridgeModel extends PFMNeoForgeBakedModel {
    private final List<String> modelParts;
    public NeoForgeFridgeModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof FridgeBlock))
            return;

        boolean bottom = state.isOf(world.getBlockState(pos.up()).getBlock());
        boolean top = state.isOf(world.getBlockState(pos.down()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.up()).getBlock() instanceof FreezerBlock && !(world.getBlockState(pos.up()).getBlock() instanceof IronFridgeBlock);
        int openOffset = state.get(FridgeBlock.OPEN) ? 6 : 0;
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
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        return List.of();
    }
}
