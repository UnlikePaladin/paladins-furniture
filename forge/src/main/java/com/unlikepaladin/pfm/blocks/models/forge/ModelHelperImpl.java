package com.unlikepaladin.pfm.blocks.models.forge;

import com.unlikepaladin.pfm.ducks.forge.PFMBakedModelManagerExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelHelperImpl {
    public static BakedModel getModelFromIdentifier(ResourceLocation id) {
        return ((PFMBakedModelManagerExtensions)Minecraft.getInstance().getModelManager()).pfm_getModel(id);
    }
}
