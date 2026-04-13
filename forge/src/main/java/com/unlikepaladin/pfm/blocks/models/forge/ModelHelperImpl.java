package com.unlikepaladin.pfm.blocks.models.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelHelperImpl {
    public static BakedModel getModelFromIdentifier(ResourceLocation id) {
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(id, "block"));
    }
}
