package com.unlikepaladin.pfm.ducks.forge;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

public interface PFMBakedModelManagerExtensions {

    BakedModel pfm_getModel(Identifier id);
}
