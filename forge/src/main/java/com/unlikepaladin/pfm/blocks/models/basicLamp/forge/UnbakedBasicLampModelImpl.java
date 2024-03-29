package com.unlikepaladin.pfm.blocks.models.basicLamp.forge;

import com.unlikepaladin.pfm.data.materials.WoodVariant;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;

import java.util.List;
import java.util.Map;

public class UnbakedBasicLampModelImpl {
    public static BakedModel getBakedModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        return new ForgeBasicLampModel(settings, modelParts);
    }
}
