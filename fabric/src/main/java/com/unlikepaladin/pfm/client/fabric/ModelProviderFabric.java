package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ModelProviderFabric implements ModelResourceProvider {
    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) {
        if (ModelHelper.containsIdentifier(UnbakedMirrorModel.MIRROR_MODEL_IDS, resourceId)){
            return new UnbakedMirrorModel(UnbakedMirrorModel.DEFAULT_REFLECT, UnbakedMirrorModel.DEFAULT_FRAME_TEXTURE, UnbakedMirrorModel.DEFAULT_GLASS);
        }
        else
            return null;
    }
}
