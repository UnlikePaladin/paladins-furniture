package com.unlikepaladin.pfm.ducks.forge;

import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.Identifier;

public interface PFMBakedModelManagerExtensions {

    BlockStateModel pfm_getModel(Identifier id);
}
