package com.unlikepaladin.pfm.blocks.models.fridge.fabric;

import com.unlikepaladin.pfm.blocks.IronFreezerBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricIronFridgeModel extends PFMFabricBakedModel {
    public FabricIronFridgeModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random randomSupplier, Predicate<@Nullable Direction> cullTest) {
        boolean bottom = state.isOf(world.getBlockState(pos.up()).getBlock());
        boolean top = state.isOf(world.getBlockState(pos.down()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.down()).getBlock() instanceof IronFreezerBlock;
        int openOffset = state.get(IronFridgeBlock.OPEN) ? 5 : 0;
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
    public void emitItemQuads(QuadEmitter context, Random randomSupplier) {

    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return particleSprite();
    }
}
