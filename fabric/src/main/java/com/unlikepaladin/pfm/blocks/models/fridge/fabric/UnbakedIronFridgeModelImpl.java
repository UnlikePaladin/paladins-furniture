package com.unlikepaladin.pfm.blocks.models.fridge.fabric;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.List;
import java.util.Map;

public class UnbakedIronFridgeModelImpl {
    public static BlockStateModel getBakedModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        return new FabricIronFridgeModel(settings, bakedModels, MODEL_PARTS);
    }
}
