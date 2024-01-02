package com.unlikepaladin.pfm.blocks.models.basicLamp.neoforge;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;

import java.util.List;

public class UnbakedBasicLampModelImpl {
    public static BakedModel getBakedModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        return new NeoForgeBasicLampModel(settings, modelParts);
    }
}
