package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.List;
import java.util.Map;

public class UnbakedFridgeModelImpl {
    public static BlockStateModel getBakedModel(ModelState settings, Map<String, BlockStateModelPart> bakedModels, List<String> MODEL_PARTS) {
        return new ForgeFridgeModel(settings, bakedModels, MODEL_PARTS);
    }
}
