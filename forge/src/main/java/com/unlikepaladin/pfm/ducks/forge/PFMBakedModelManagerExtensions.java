package com.unlikepaladin.pfm.ducks.forge;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;

public interface PFMBakedModelManagerExtensions {

    BlockStateModel pfm_getModel(ResourceLocation id);
}
