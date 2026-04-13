package com.unlikepaladin.pfm.blocks.models.fridge.forge;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.List;
import java.util.Map;

public class UnbakedFreezerModelImpl {
    public static BakedModel getBakedModel(TextureAtlasSprite frame, ModelState settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        return new ForgeFreezerModel(frame, settings, bakedModels, MODEL_PARTS);
    }
}
