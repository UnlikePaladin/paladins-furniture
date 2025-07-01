package com.unlikepaladin.pfm.blocks.models.fridge.fabric;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
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
import java.util.Map;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricFreezerModel extends PFMFabricBakedModel {
    public FabricFreezerModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }
    private final List<String> modelParts;

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
        boolean hasFridge = world.getBlockState(pos.down()).getBlock() instanceof FridgeBlock && !(world.getBlockState(pos.down()).getBlock() instanceof IronFridgeBlock);
        int openOffset = state.get(FreezerBlock.OPEN) ? 2 : 0;
        if (hasFridge) {
            getTemplateBakedModels().get((1+openOffset)).emitQuads(context, cullTest);
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
