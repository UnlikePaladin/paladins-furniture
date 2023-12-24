package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.client.EntityRenderRegistry;
public class EntityRenderRegistryFabric {

    public static void registerRender() {
        EntityRenderRegistry.registerBlockEntityRenderers();
        EntityRenderRegistry.registerEntityRenderers();
        EntityRenderRegistry.registerModelLayers();
    }

}

