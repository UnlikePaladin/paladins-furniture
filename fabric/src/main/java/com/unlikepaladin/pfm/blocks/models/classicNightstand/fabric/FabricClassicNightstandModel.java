package com.unlikepaladin.pfm.blocks.models.classicNightstand.fabric;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FabricClassicNightstandModel extends AbstractBakedModel implements FabricBakedModel {
    public FabricClassicNightstandModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
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
        if (state.getBlock() instanceof ClassicNightstandBlock) {
            ClassicNightstandBlock block = (ClassicNightstandBlock) state.getBlock();
            Direction dir = state.get(ClassicNightstandBlock.FACING);
            boolean left = block.isStand(world, pos, dir.rotateYCounterclockwise(), dir);
            boolean right = block.isStand(world, pos, dir.rotateYClockwise(), dir);
            int openIndexOffset = state.get(ClassicNightstandBlock.OPEN) ? 4 : 0;
            if (left && right) {
                ((FabricBakedModel) getBakedModels().get(modelParts.get(openIndexOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
            } else if (!left && right) {
                ((FabricBakedModel) getBakedModels().get(modelParts.get(1+openIndexOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
            } else if (left) {
                ((FabricBakedModel) getBakedModels().get(modelParts.get(2+openIndexOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
            } else {
                ((FabricBakedModel) getBakedModels().get(modelParts.get(3+openIndexOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }
}
