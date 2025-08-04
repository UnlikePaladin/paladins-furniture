package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFreezerBlock;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
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

public class ForgeIronFridgeModel extends PFMForgeBakedModel {
    private final List<String> modelParts;

    public ForgeIronFridgeModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }


    @Override
    public void collectParts(Random random, List<BlockModelPart> dest, ModelData extraData, @Nullable BlockRenderLayer renderType) {
        BlockState state = extraData.get(STATE);
        List<BlockModelPart> quads = new ArrayList<>();
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            BitSet data = extraData.get(CONNECTIONS).connections;
            boolean bottom = data.get(0);
            boolean top = data.get(1);
            boolean hasFreezer = data.get(2);
            int openOffset = state.get(FridgeBlock.OPEN) ? 5 : 0;
            if (top && bottom) {
                quads.add(getTemplateBakedModels().get(2+openOffset));
            } else if (bottom) {
                quads.add(getTemplateBakedModels().get(3+openOffset));
            } else if (top) {
                quads.add(getTemplateBakedModels().get(1+openOffset));
            } else if (hasFreezer) {
                quads.add(getTemplateBakedModels().get(4+openOffset));
            } else {
                quads.add(getTemplateBakedModels().get(openOffset));
            }
        }
        dest.addAll(quads);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
        BitSet set = new BitSet();
        set.set(0, state.isOf(world.getBlockState(pos.up()).getBlock()));
        set.set(1, state.isOf(world.getBlockState(pos.down()).getBlock()));
        set.set(2, world.getBlockState(pos.down()).getBlock() instanceof IronFreezerBlock);
        builder.with(CONNECTIONS, new ModelBitSetProperty(set));
        builder.with(STATE, state);
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        return List.of();
    }
}