package com.unlikepaladin.pfm.ducks.forge;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface PFModelBakerBakedExtensions {
    @Nullable
    Map<ResourceLocation, BlockStateModel> pfm_getExtraModels();

    void pfm_setExtraModels(@Nullable Map<ResourceLocation, BlockStateModel> extraModels);
}
