package com.unlikepaladin.pfm.blocks.models.forge;

import com.unlikepaladin.pfm.ducks.forge.PFMBakedModelManagerExtensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.Identifier;

public class ModelHelperImpl {
    public static BlockStateModel getModelFromIdentifier(Identifier id) {
        return ((PFMBakedModelManagerExtensions)MinecraftClient.getInstance().getBakedModelManager()).pfm_getModel(id);
    }
}
