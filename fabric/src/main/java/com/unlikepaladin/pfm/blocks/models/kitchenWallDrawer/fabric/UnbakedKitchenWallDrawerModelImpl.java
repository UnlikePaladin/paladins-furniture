package com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer.fabric;

import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class UnbakedKitchenWallDrawerModelImpl {
    public static BakedModel getBakedModel(ResourceLocation modelId, ModelState settings, List<BakedModel> modelParts) {
        if (PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().containsKey(settings))
            return PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().get(settings);

        BakedModel model = new FabricKitchenWallDrawerModel(settings, modelParts);
        PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().put(settings,model);
        return model;
    }
}
