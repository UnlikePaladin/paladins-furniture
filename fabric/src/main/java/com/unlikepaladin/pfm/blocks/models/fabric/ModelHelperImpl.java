package com.unlikepaladin.pfm.blocks.models.fabric;

import com.unlikepaladin.pfm.client.fabric.PFMModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.Identifier;

public class ModelHelperImpl {
    public static BlockStateModel getModelFromIdentifier(Identifier id) {
        return MinecraftClient.getInstance().getBakedModelManager().getModel(PFMModelLoadingPlugin.modelKeyMap.get(id));
    }
}
