package com.unlikepaladin.pfm.blocks.models.mirror.fabric;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

public class UnbakedMirrorModelImpl {
    public static BlockStateModel getBakedModel(ModelState settings, Map<String, BlockStateModelPart> bakedModels, List<String> MODEL_PARTS) {
        return new FabricMirrorModel(settings, bakedModels, MODEL_PARTS);
    }
}
