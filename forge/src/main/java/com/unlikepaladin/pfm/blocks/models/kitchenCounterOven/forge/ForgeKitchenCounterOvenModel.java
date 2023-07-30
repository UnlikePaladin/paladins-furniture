package com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.forge;

import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class ForgeKitchenCounterOvenModel extends AbstractBakedModel {
    private final List<String> modelParts;
    public ForgeKitchenCounterOvenModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(frame, settings, bakedModels);
        this.modelParts = modelParts;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer renderLayer) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state.getBlock() instanceof KitchenCounterOvenBlock) {
            BitSet data = extraData.get(CONNECTIONS).connections;
            int openOffset = state.get(KitchenCounterOvenBlock.OPEN) ? 2 : 0;
            if (data.get(0) || data.get(1)) {
                quads.addAll(getBakedModels().get(modelParts.get(1 + openOffset)).getQuads(state, side, rand, extraData, renderLayer));
            } else {
                quads.addAll(getBakedModels().get(modelParts.get(openOffset)).getQuads(state, side, rand, extraData, renderLayer));
            }
        }
        return quads;
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
        if (state.getBlock() instanceof KitchenCounterOvenBlock) {
            BitSet set = new BitSet();
            set.set(0, KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.up()).getBlock()));
            set.set(1, KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.down()).getBlock()));
            builder.with(CONNECTIONS, new ModelBitSetProperty(set));
        }
        return builder.build();
    }
}
