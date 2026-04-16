package com.unlikepaladin.pfm.blocks.models.fridge.fabric;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
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
import java.util.function.Supplier;

public class FabricFridgeModel extends PFMFabricBakedModel {
    public FabricFridgeModel(TextureAtlasSprite frame, ModelState settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(settings, bakedModels.values().stream().toList());
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(QuadEmitter context, BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        boolean bottom = state.is(world.getBlockState(pos.above()).getBlock());
        boolean top = state.is(world.getBlockState(pos.below()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.above()).getBlock() instanceof FreezerBlock && !(world.getBlockState(pos.above()).getBlock() instanceof IronFridgeBlock);
        int openOffset = state.getValue(FridgeBlock.OPEN) ? 6 : 0;
        if (top && hasFreezer) {
            getTemplateBakedModels().get((5+openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }
        else if (top && bottom) {
            getTemplateBakedModels().get((2+openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else if (bottom) {
            getTemplateBakedModels().get((3+openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else if (top) {
            getTemplateBakedModels().get((1+openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else if (hasFreezer) {
            getTemplateBakedModels().get((4+openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else {
            getTemplateBakedModels().get((openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Supplier<RandomSource> randomSupplier) {

    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getParticleIcon();
    }
}
