package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.blocks.IronFridgeBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ForgeFreezerModel extends PFMForgeBakedModel {
    private final List<String> modelParts;
    public ForgeFreezerModel(TextureAtlasSprite frame, ModelState settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(settings, new ArrayList<>(bakedModels.values()));
        this.modelParts = modelParts;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state != null) {
            Boolean hasFridge = extraData.getData(HAS_FRIDGE_PROPERTY);
            int openOffset = state.getValue(FreezerBlock.OPEN) ? 2 : 0;
            if (Boolean.TRUE.equals(hasFridge)) {
                quads.addAll(getTemplateBakedModels().get(1+openOffset).getQuads(state, side, rand, extraData));
            } else {
                quads.addAll(getTemplateBakedModels().get(openOffset).getQuads(state, side, rand, extraData));
            }
        }
        return quads;
    }
    public static ModelProperty<Boolean> HAS_FRIDGE_PROPERTY = new ModelProperty<>();
    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        boolean hasFridge = world.getBlockState(pos.below()).getBlock() instanceof FridgeBlock && !(world.getBlockState(pos.below()).getBlock() instanceof IronFridgeBlock);
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        builder.withInitial(HAS_FRIDGE_PROPERTY, hasFridge);
        return builder.build();
    }
}
