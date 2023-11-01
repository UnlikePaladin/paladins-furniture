package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFreezerBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeIronFridgeModel extends AbstractBakedModel {
    private final List<String> modelParts;

    public ForgeIronFridgeModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(frame, settings, bakedModels);
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
            int openOffset = state.get(FridgeBlock.OPEN) ? 5 : 0;
            if (top && bottom) {
                quads.addAll(getBakedModels().get(modelParts.get(2+openOffset)).getQuads(state, side, rand, extraData));
            } else if (bottom) {
                quads.addAll(getBakedModels().get(modelParts.get(3+openOffset)).getQuads(state, side, rand, extraData));
            } else if (top) {
                quads.addAll(getBakedModels().get(modelParts.get(1+openOffset)).getQuads(state, side, rand, extraData));
            } else if (hasFreezer) {
                quads.addAll(getBakedModels().get(modelParts.get(4+openOffset)).getQuads(state, side, rand, extraData));
            } else {
                quads.addAll(getBakedModels().get(modelParts.get(openOffset)).getQuads(state, side, rand, extraData));
            }
        }
        return quads;
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        BitSet set = new BitSet();
        set.set(0, state.isOf(world.getBlockState(pos.up()).getBlock()));
        set.set(1, state.isOf(world.getBlockState(pos.down()).getBlock()));
        set.set(2, world.getBlockState(pos.down()).getBlock() instanceof IronFreezerBlock);
        builder.withInitial(CONNECTIONS, new ModelBitSetProperty(set));
        return builder.build();
    }
}