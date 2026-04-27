package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFreezerBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ForgeIronFridgeModel extends PFMForgeBakedModel {
    private final List<String> modelParts;

    public ForgeIronFridgeModel(TextureAtlasSprite frame, ModelState settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(settings, new ArrayList<>(bakedModels.values()));
        this.modelParts = modelParts;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state != null && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            BitSet data = extraData.getData(CONNECTIONS).connections;
            boolean bottom = data.get(0);
            boolean top = data.get(1);
            boolean hasFreezer = data.get(2);
            int openOffset = state.getValue(FridgeBlock.OPEN) ? 5 : 0;
            if (top && bottom) {
                quads.addAll(getTemplateBakedModels().get(2+openOffset).getQuads(state, side, rand, extraData));
            } else if (bottom) {
                quads.addAll(getTemplateBakedModels().get(3+openOffset).getQuads(state, side, rand, extraData));
            } else if (top) {
                quads.addAll(getTemplateBakedModels().get(1+openOffset).getQuads(state, side, rand, extraData));
            } else if (hasFreezer) {
                quads.addAll(getTemplateBakedModels().get(4+openOffset).getQuads(state, side, rand, extraData));
            } else {
                quads.addAll(getTemplateBakedModels().get(openOffset).getQuads(state, side, rand, extraData));
            }
        }
        return quads;
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        BitSet set = new BitSet();
        set.set(0, state.is(world.getBlockState(pos.above()).getBlock()));
        set.set(1, state.is(world.getBlockState(pos.below()).getBlock()));
        set.set(2, world.getBlockState(pos.below()).getBlock() instanceof IronFreezerBlock);
        builder.withInitial(CONNECTIONS, new ModelBitSetProperty(set));
        return builder.build();
    }
}