package com.unlikepaladin.pfm.blocks.models.mirror.forge;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class UnbakedMirrorModelImpl {
    public static BakedModel getBakedModel(Sprite frame, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        return new ForgeMirrorModel(frame, glassTex, reflectTex, settings, bakedModels, MODEL_PARTS);
    }
}
