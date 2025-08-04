package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.util.math.random.Random;

public class ForgeFreezerModel extends PFMForgeBakedModel {
    private final List<String> modelParts;
    public ForgeFreezerModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> modelParts) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = modelParts;
    }

    @Override
    public void collectParts(Random random, List<BlockModelPart> dest, ModelData extraData, @Nullable BlockRenderLayer renderType) {
        List<BlockModelPart> quads = new ArrayList<>();
        BlockState state = extraData.get(STATE);
        if (state != null) {
            Boolean hasFridge = extraData.get(HAS_FRIDGE_PROPERTY);
            int openOffset = state.get(FreezerBlock.OPEN) ? 2 : 0;
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
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        boolean hasFridge = world.getBlockState(pos.down()).getBlock() instanceof FridgeBlock && !(world.getBlockState(pos.down()).getBlock() instanceof IronFridgeBlock);
        ModelData.Builder builder = ModelData.builder();
        builder.with(HAS_FRIDGE_PROPERTY, hasFridge);
        builder.with(STATE, state);
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        return List.of();
    }
}
