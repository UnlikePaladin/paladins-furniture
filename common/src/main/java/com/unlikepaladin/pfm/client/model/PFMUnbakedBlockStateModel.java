package com.unlikepaladin.pfm.client.model;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.model.BlockStateModel;

public interface PFMUnbakedBlockStateModel extends BlockStateModel.Unbaked {
    MapCodec<? extends BlockStateModel.Unbaked> codec();
}
