package com.unlikepaladin.pfm.blocks.models.classicNightstand.forge;

import com.unlikepaladin.pfm.blocks.models.classicCoffeeTable.forge.ForgeClassicCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.classicTable.forge.ForgeClassicTableModel;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UnbakedClassicNightstandModelImpl {
    public static BakedModel getBakedModel(Identifier modelId, ModelBakeSettings settings, List<BakedModel> modelParts) {
        if (PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().containsKey(settings))
            return PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().get(settings);

        BakedModel model = new ForgeClassicNightstandModel(settings, modelParts);
        PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().put(settings,model);
        return model;
    }
}
