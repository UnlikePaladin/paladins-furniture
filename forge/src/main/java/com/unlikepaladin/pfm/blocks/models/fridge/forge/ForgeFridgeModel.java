package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.RandomSource;

public class ForgeFridgeModel extends PFMForgeBakedModel {
    private final List<String> modelParts;
    public ForgeFridgeModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable ChunkSectionLayer renderType) {
        List<BlockModelPart> quads = new ArrayList<>();
        BlockState state = extraData.get(STATE);
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            BitSet data = extraData.get(CONNECTIONS).connections;
            boolean bottom = data.get(0);
            boolean top = data.get(1);
            boolean hasFreezer = data.get(2);
            int openOffset = state.getValue(FridgeBlock.OPEN) ? 6 : 0;
            if (top && hasFreezer) {
                quads.add(getTemplateBakedModels().get(5+openOffset));
            }
            else if (top && bottom) {
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
    public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
        BitSet set = new BitSet();
        set.set(0, state.is(world.getBlockState(pos.above()).getBlock()));
        set.set(1, state.is(world.getBlockState(pos.below()).getBlock()));
        set.set(2, world.getBlockState(pos.above()).getBlock() instanceof FreezerBlock && !(world.getBlockState(pos.above()).getBlock() instanceof IronFridgeBlock));
        builder.with(CONNECTIONS, new ModelBitSetProperty(set));
        builder.with(STATE, state);
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        return List.of();
    }
}
