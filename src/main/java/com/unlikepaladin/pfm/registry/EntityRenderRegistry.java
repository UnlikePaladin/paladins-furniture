package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.entity.render.ChairEntityRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class EntityRenderRegistry {
    public static void registerRender() {
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.CHAIR, (context) -> new ChairEntityRenderer(context));

    }

    }

