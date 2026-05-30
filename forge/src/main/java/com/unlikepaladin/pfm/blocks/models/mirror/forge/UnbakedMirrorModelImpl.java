package com.unlikepaladin.pfm.blocks.models.mirror.forge;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

public class UnbakedMirrorModelImpl {
    public static BlockStateModel getBakedModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        return new ForgeMirrorModel(settings, bakedModels, MODEL_PARTS);
    }
}
