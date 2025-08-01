package com.unlikepaladin.pfm.blocks.models.classicStool.forge;

import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.util.Identifier;

import java.util.List;

public class UnbakedClassicStoolModelImpl {
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        if (PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().containsKey(settings))
            return PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().get(settings);

        BlockStateModel model = new ForgeClassicStoolModel(settings, modelSettings, modelParts);
        PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().put(settings,model);
        return model;
    }
}
