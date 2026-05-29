package com.unlikepaladin.pfm.blocks.models.fridge.fabric;

import com.unlikepaladin.pfm.blocks.IronFreezerBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricIronFridgeModel extends PFMFabricBakedModel {
    public FabricIronFridgeModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource randomSupplier, Predicate<@Nullable Direction> cullTest) {
        boolean bottom = state.is(world.getBlockState(pos.above()).getBlock());
        boolean top = state.is(world.getBlockState(pos.below()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.below()).getBlock() instanceof IronFreezerBlock;
        int openOffset = state.getValue(IronFridgeBlock.OPEN) ? 5 : 0;
        if (top && bottom) {
            getTemplateBakedModels().get((2+openOffset)).emitQuads(context, cullTest);
        } else if (bottom) {
            getTemplateBakedModels().get((3+openOffset)).emitQuads(context, cullTest);
        } else if (top) {
            getTemplateBakedModels().get((1+openOffset)).emitQuads(context, cullTest);
        } else if (hasFreezer) {
            getTemplateBakedModels().get((4+openOffset)).emitQuads(context, cullTest);
        } else {
            getTemplateBakedModels().get((openOffset)).emitQuads(context, cullTest);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, RandomSource randomSupplier) {

    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return particleIcon();
    }
}
