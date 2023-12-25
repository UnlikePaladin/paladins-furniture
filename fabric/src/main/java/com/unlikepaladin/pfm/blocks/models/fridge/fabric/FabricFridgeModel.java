package com.unlikepaladin.pfm.blocks.models.fridge.fabric;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FabricFridgeModel extends PFMFabricBakedModel {
    public FabricFridgeModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(settings, new ArrayList<>(bakedModels.values()));
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
        boolean hasFreezer = world.getBlockState(pos.up()).getBlock() instanceof FreezerBlock && !(world.getBlockState(pos.up()).getBlock() instanceof IronFridgeBlock);
        int openOffset = state.get(FridgeBlock.OPEN) ? 6 : 0;
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
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSprite();
    }
}
