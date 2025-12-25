package com.unlikepaladin.pfm.blocks.models.neoforge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

public class ModelHelperImpl {
    public static BakedModel getModelFromIdentifier(Identifier id) {
        return MinecraftClient.getInstance().getBakedModelManager().getModel(ModelIdentifier.standalone(id));
    }
}
