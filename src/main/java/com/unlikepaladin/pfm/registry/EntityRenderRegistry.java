package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.entity.render.ChairEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class EntityRenderRegistry {
    public static void registerRender() {
        EntityRendererRegistry.register(EntityRegistry.CHAIR, ChairEntityRenderer::new);
    }

}

