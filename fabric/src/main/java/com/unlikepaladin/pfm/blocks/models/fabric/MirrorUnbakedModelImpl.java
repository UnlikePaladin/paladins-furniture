package com.unlikepaladin.pfm.blocks.models.fabric;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class MirrorUnbakedModelImpl {
    public static BakedModel getBakedModel(Sprite frame, Map<BlockState, Sprite> frameOverrides, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<Identifier,BakedModel> bakedModels) {
        return new FabricMirrorModel(frame, frameOverrides, glassTex, reflectTex, settings, bakedModels);
    }
}
