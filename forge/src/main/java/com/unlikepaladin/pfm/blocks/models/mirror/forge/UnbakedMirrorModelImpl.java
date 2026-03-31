package com.unlikepaladin.pfm.blocks.models.mirror.forge;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class UnbakedMirrorModelImpl {
    public static BakedModel getBakedModel(TextureAtlasSprite frame, TextureAtlasSprite glassTex, TextureAtlasSprite reflectTex, ModelState settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        return new ForgeMirrorModel(frame, glassTex, reflectTex, settings, bakedModels, MODEL_PARTS);
    }
}
