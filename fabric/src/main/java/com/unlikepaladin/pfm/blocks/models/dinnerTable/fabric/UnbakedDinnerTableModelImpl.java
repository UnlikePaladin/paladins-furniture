package com.unlikepaladin.pfm.blocks.models.dinnerTable.fabric;

import com.unlikepaladin.pfm.blocks.models.dinnerTable.BakedDinnerTableModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;

import java.util.List;
import java.util.Map;

public class UnbakedDinnerTableModelImpl {
    public static BakedModel getBakedModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        return new FabricDinnerTableModel(frame, settings, bakedModels, MODEL_PARTS);
    }
}
