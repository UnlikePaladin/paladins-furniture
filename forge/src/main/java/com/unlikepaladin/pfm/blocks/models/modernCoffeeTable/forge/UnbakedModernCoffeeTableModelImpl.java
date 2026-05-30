package com.unlikepaladin.pfm.blocks.models.modernCoffeeTable.forge;

import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.resources.Identifier;

import java.util.List;

public class UnbakedModernCoffeeTableModelImpl {
    public static BlockStateModel getBakedModel(Identifier modelId, ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        if (PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().containsKey(settings))
            return PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().get(settings);

        BlockStateModel model = new ForgeModernCoffeeTableModel(settings, modelSettings, modelParts);
        PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().put(settings,model);
        return model;
    }
}
