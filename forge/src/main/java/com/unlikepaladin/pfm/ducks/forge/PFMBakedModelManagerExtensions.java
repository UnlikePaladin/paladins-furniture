package com.unlikepaladin.pfm.ducks.forge;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public interface PFMBakedModelManagerExtensions {

    BakedModel pfm_getModel(ResourceLocation id);
}
