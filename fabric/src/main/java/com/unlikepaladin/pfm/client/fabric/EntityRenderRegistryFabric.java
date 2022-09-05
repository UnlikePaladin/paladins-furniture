package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.*;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Entities;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class EntityRenderRegistryFabric {

    public static void registerRender() {
        EntityRendererRegistry.INSTANCE.register(Entities.CHAIR,  (entityRenderDispatcher, context) -> new ChairEntityRenderer(entityRenderDispatcher));
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntities.MICROWAVE_BLOCK_ENTITY, MicrowaveBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntities.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntities.PLATE_BLOCK_ENTITY, PlateBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntities.STOVE_BLOCK_ENTITY, StoveBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntities.TRASHCAN_BLOCK_ENTITY, TrashcanBlockEntityRenderer::new);

    }

}

