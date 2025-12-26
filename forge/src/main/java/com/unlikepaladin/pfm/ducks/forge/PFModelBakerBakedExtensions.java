package com.unlikepaladin.pfm.ducks.forge;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface PFModelBakerBakedExtensions {
    @Nullable
    Map<Identifier, BakedModel> pfm_getExtraModels();

    void pfm_setExtraModels(@Nullable Map<Identifier, BakedModel> extraModels);
}
