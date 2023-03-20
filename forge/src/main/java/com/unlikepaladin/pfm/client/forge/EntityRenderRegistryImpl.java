package com.unlikepaladin.pfm.client.forge;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityRenderRegistryImpl {
    public static Map<BlockEntityType, BlockEntityRendererFactory> blockEntityRendererFactoryMap = new HashMap<>();
    public static Map<EntityType, EntityRendererFactory> entityRendererFactoryMap = new HashMap<>();
    public static Map<EntityModelLayer, TexturedModelData> entityModelLayerTexturedModelDataMap = new HashMap<>();

    public static void registerBlockEntityRender(BlockEntityType blockEntityType, BlockEntityRendererFactory blockEntityRendererFactory) {
        blockEntityRendererFactoryMap.put(blockEntityType, blockEntityRendererFactory);
    }

    public static void registerEntityRender(EntityType entityType, EntityRendererFactory entityRendererFactory) {
        entityRendererFactoryMap.put(entityType, entityRendererFactory);
    }

    public static void registerModelLayer(EntityModelLayer entityType, TexturedModelData texturedModelData) {
        entityModelLayerTexturedModelDataMap.put(entityType, texturedModelData);
    }
}
