package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;

public class ForgeFreezerModel extends PFMForgeBakedModel {
    private final List<String> modelParts;
    public ForgeFreezerModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable ChunkSectionLayer renderType) {
        List<BlockModelPart> quads = new ArrayList<>();
        BlockState state = extraData.get(STATE);
        if (state != null) {
            Boolean hasFridge = extraData.get(HAS_FRIDGE_PROPERTY);
            int openOffset = state.getValue(FreezerBlock.OPEN) ? 2 : 0;
            if (Boolean.TRUE.equals(hasFridge)) {
                quads.add(getTemplateBakedModels().get(1+openOffset));
            } else {
                quads.add(getTemplateBakedModels().get(openOffset));
            }
        }
        dest.addAll(quads);
    }

    public static ModelProperty<Boolean> HAS_FRIDGE_PROPERTY = new ModelProperty<>();
    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        boolean hasFridge = world.getBlockState(pos.below()).getBlock() instanceof FridgeBlock && !(world.getBlockState(pos.below()).getBlock() instanceof IronFridgeBlock);
        ModelData.Builder builder = ModelData.builder();
        builder.with(HAS_FRIDGE_PROPERTY, hasFridge);
        builder.with(STATE, state);
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        return List.of();
    }
}
