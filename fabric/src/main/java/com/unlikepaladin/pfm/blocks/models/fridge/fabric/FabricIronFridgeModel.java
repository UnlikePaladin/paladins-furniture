package com.unlikepaladin.pfm.blocks.models.fridge.fabric;

import com.unlikepaladin.pfm.blocks.IronFreezerBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FabricIronFridgeModel extends PFMFabricBakedModel {
    public FabricIronFridgeModel(TextureAtlasSprite frame, ModelState settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(settings, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }
    private final List<String> modelParts;

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        boolean bottom = state.is(world.getBlockState(pos.above()).getBlock());
        boolean top = state.is(world.getBlockState(pos.below()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.below()).getBlock() instanceof IronFreezerBlock;
        int openOffset = state.getValue(IronFridgeBlock.OPEN) ? 5 : 0;
        if (top && bottom) {
            ((FabricBakedModel) getTemplateBakedModels().get((2+openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (bottom) {
            ((FabricBakedModel) getTemplateBakedModels().get((3+openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (top) {
            ((FabricBakedModel) getTemplateBakedModels().get((1+openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (hasFreezer) {
            ((FabricBakedModel) getTemplateBakedModels().get((4+openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else {
            ((FabricBakedModel) getTemplateBakedModels().get((openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
    }
    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getParticleIcon();
    }
}
