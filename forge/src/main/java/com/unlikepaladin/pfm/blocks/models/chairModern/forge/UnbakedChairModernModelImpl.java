package com.unlikepaladin.pfm.blocks.models.chairModern.forge;

import com.unlikepaladin.pfm.blocks.models.chairDinner.forge.ForgeChairDinnerModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UnbakedChairModernModelImpl {
    static Map<ModelBakeSettings, BakedModel> modelMap = new ConcurrentHashMap<>();
    public static BakedModel getBakedModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        if (modelMap.containsKey(settings))
            return modelMap.get(settings);
        BakedModel model = new ForgeChairModernModel(settings, modelParts);
        modelMap.put(settings, model);
        return model;
    }
}
