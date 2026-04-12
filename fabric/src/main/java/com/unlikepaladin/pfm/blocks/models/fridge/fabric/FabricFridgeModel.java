package com.unlikepaladin.pfm.blocks.models.fridge.fabric;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
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
import net.minecraft.util.RandomSource;
import java.util.function.Supplier;

public class FabricFridgeModel extends PFMFabricBakedModel {
    public FabricFridgeModel(TextureAtlasSprite frame, ModelState settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(settings, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }
    private final List<String> modelParts;

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        boolean bottom = state.is(world.getBlockState(pos.above()).getBlock());
        boolean top = state.is(world.getBlockState(pos.below()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.above()).getBlock() instanceof FreezerBlock && !(world.getBlockState(pos.above()).getBlock() instanceof IronFridgeBlock);
        int openOffset = state.getValue(FridgeBlock.OPEN) ? 6 : 0;
        if (top && hasFreezer) {
            ((FabricBakedModel) getTemplateBakedModels().get((5+openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
        else if (top && bottom) {
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
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {

    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getParticleIcon();
    }
}
