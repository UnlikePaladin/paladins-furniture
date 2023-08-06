package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.client.EntityRenderRegistry;
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
        EntityRenderRegistry.registerBlockEntityRenderers();
        EntityRenderRegistry.registerEntityRenderers();
        EntityRenderRegistry.registerModelLayers();
    }

}

