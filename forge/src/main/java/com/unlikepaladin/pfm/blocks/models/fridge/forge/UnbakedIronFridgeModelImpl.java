package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;

import java.util.List;
import java.util.Map;

public class UnbakedIronFridgeModelImpl {
    public static BlockStateModel getBakedModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        return new ForgeIronFridgeModel(settings, bakedModels, MODEL_PARTS);
    }
}
