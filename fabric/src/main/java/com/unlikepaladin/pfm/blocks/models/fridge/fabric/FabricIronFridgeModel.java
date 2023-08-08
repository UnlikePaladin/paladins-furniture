package com.unlikepaladin.pfm.blocks.models.fridge.fabric;

import com.unlikepaladin.pfm.blocks.IronFreezerBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FabricIronFridgeModel extends AbstractBakedModel implements FabricBakedModel {
    public FabricIronFridgeModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(frame, settings, bakedModels);
        this.modelParts = modelParts;
    }
    private final List<String> modelParts;

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        boolean bottom = state.isOf(world.getBlockState(pos.up()).getBlock());
        boolean top = state.isOf(world.getBlockState(pos.down()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.down()).getBlock() instanceof IronFreezerBlock;
        int openOffset = state.get(IronFridgeBlock.OPEN) ? 5 : 0;
        if (top && bottom) {
            ((FabricBakedModel) getBakedModels().get(modelParts.get(2+openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (bottom) {
            ((FabricBakedModel) getBakedModels().get(modelParts.get(3+openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (top) {
            ((FabricBakedModel) getBakedModels().get(modelParts.get(1+openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (hasFreezer) {
            ((FabricBakedModel) getBakedModels().get(modelParts.get(4+openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else {
            ((FabricBakedModel) getBakedModels().get(modelParts.get(openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
    }
    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }
}
