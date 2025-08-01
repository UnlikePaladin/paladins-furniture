package com.unlikepaladin.pfm.blocks.models.mirror.forge;

import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.ModelBakeSettings;

import java.util.List;
import java.util.Map;

public class UnbakedMirrorModelImpl {
    public static BlockStateModel getBakedModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        return new ForgeMirrorModel(settings, bakedModels, MODEL_PARTS);
    }
}
