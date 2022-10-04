package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.*;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Entities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import static com.unlikepaladin.pfm.client.EntityRenderIDs.MODEL_CUBE_LAYER;

public class EntityRenderRegistryFabric {

    public static void registerRender() {
        EntityRendererRegistry.register(Entities.CHAIR, ChairEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntities.MICROWAVE_BLOCK_ENTITY, MicrowaveBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntities.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntities.PLATE_BLOCK_ENTITY, PlateBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntities.STOVE_BLOCK_ENTITY, StoveBlockEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);
        BlockEntityRendererRegistry.register(BlockEntities.TRASHCAN_BLOCK_ENTITY, TrashcanBlockEntityRenderer::new);
    }

}

