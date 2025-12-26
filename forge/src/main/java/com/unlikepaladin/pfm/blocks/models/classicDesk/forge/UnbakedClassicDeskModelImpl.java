package com.unlikepaladin.pfm.blocks.models.classicDesk.forge;

import com.unlikepaladin.pfm.blocks.models.basicDesk.forge.ForgeBasicDeskModel;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.util.Identifier;import java.util.List;

public class UnbakedClassicDeskModelImpl {
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        if (PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().containsKey(settings))
            return PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().get(settings);

        BlockStateModel model = new ForgeBasicDeskModel(settings, itemSettings, modelParts);
        PFMRuntimeResources.modelCacheMap.get(modelId).getBakedModels().put(settings,model);
        return model;
    }
}
