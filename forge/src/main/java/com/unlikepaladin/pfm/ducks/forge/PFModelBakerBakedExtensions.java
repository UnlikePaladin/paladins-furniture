package com.unlikepaladin.pfm.ducks.forge;

import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface PFModelBakerBakedExtensions {
    @Nullable
    Map<Identifier, BlockStateModel> pfm_getExtraModels();

    void pfm_setExtraModels(@Nullable Map<Identifier, BlockStateModel> extraModels);
}
