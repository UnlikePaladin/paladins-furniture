package com.unlikepaladin.pfm.blocks.models.basicLamp.fabric;

import com.unlikepaladin.pfm.data.materials.WoodVariant;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;

import java.util.List;
import java.util.Map;

public class UnbakedBasicLampModelImpl {

    public static BakedModel getBakedModel(Map<WoodVariant, Sprite> textures, ModelBakeSettings settings, Map<WoodVariant, Map<String, BakedModel>> variantToModelMap, List<String> modelParts) {
        return new FabricBasicLampModel(textures, settings, variantToModelMap, modelParts);
    }
}
