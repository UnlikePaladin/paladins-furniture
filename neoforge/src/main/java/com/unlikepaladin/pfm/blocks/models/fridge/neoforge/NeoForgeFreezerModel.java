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

import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;

public class NeoForgeFreezerModel extends PFMNeoForgeBakedModel {
    private final List<String> modelParts;
    public NeoForgeFreezerModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof FreezerBlock))
            return;


        boolean hasFridge = world.getBlockState(pos.below()).getBlock() instanceof FridgeBlock && !(world.getBlockState(pos.below()).getBlock() instanceof IronFridgeBlock);
        int openOffset = state.getValue(FreezerBlock.OPEN) ? 2 : 0;
        if (Boolean.TRUE.equals(hasFridge)) {
            parts.add(getTemplateBakedModels().get(1+openOffset));
        } else {
            parts.add(getTemplateBakedModels().get(openOffset));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        return List.of();
    }
}
