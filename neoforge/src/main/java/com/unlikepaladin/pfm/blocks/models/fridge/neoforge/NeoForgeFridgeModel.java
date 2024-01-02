package com.unlikepaladin.pfm.blocks.models.fridge.neoforge;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.neoforge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class NeoForgeFridgeModel extends PFMNeoForgeBakedModel {
    private final List<String> modelParts;
    public NeoForgeFridgeModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(settings, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer renderLayer) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            BitSet data = extraData.get(CONNECTIONS).connections;
            boolean bottom = data.get(0);
            boolean top = data.get(1);
            boolean hasFreezer = data.get(2);
            int openOffset = state.get(FridgeBlock.OPEN) ? 6 : 0;
            if (top && hasFreezer) {
                quads.addAll(getTemplateBakedModels().get(5+openOffset).getQuads(state, side, rand, extraData, renderLayer));
            }
            else if (top && bottom) {
                quads.addAll(getTemplateBakedModels().get(2+openOffset).getQuads(state, side, rand, extraData, renderLayer));
            } else if (bottom) {
                quads.addAll(getTemplateBakedModels().get(3+openOffset).getQuads(state, side, rand, extraData, renderLayer));
            } else if (top) {
                quads.addAll(getTemplateBakedModels().get(1+openOffset).getQuads(state, side, rand, extraData, renderLayer));
            } else if (hasFreezer) {
                quads.addAll(getTemplateBakedModels().get(4+openOffset).getQuads(state, side, rand, extraData, renderLayer));
            } else {
                quads.addAll(getTemplateBakedModels().get(openOffset).getQuads(state, side, rand, extraData, renderLayer));
            }
        }
        return quads;
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
        BitSet set = new BitSet();
        set.set(0, state.isOf(world.getBlockState(pos.up()).getBlock()));
        set.set(1, state.isOf(world.getBlockState(pos.down()).getBlock()));
        set.set(2, world.getBlockState(pos.up()).getBlock() instanceof FreezerBlock && !(world.getBlockState(pos.up()).getBlock() instanceof IronFridgeBlock));
        builder.with(CONNECTIONS, new ModelBitSetProperty(set));
        return builder.build();
    }
}
