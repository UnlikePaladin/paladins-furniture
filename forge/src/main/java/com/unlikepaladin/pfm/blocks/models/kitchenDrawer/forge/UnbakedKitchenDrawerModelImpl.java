package com.unlikepaladin.pfm.blocks.models.kitchenDrawer.forge;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;

import java.util.List;
import java.util.Map;

public class UnbakedKitchenDrawerModelImpl {
    public static BakedModel getBakedModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        return new ForgeKitchenDrawerModel(frame, settings, bakedModels, MODEL_PARTS);
    }
}
