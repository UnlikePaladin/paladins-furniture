package com.unlikepaladin.pfm.ducks.forge;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface PFModelBakerBakedExtensions {
    @Nullable
    Map<ResourceLocation, BakedModel> pfm_getExtraModels();

    void pfm_setExtraModels(@Nullable Map<ResourceLocation, BakedModel> extraModels);
}
