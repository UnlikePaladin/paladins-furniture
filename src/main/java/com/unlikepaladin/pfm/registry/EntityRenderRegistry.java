package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.render.RenderChair;
import com.unlikepaladin.pfm.entity.render.RenderPlayerChairBlockEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class EntityRenderRegistry {
    public static void registerRender() {
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.CHAIR, (context) -> new RenderChair(context));
        BlockEntityRendererRegistry.INSTANCE.register(PaladinFurnitureMod.PLAYER_CHAIR_BLOCK_ENTITY, RenderPlayerChairBlockEntity::new);

    }

    }

