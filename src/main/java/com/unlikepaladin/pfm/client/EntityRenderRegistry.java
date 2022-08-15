package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.*;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class EntityRenderRegistry {

    public static final EntityModelLayer MODEL_CUBE_LAYER = new EntityModelLayer(new Identifier("pfm", "cube"), "main");
    public static void registerRender() {
        EntityRendererRegistry.register(EntityRegistry.CHAIR, ChairEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityRegistry.MICROWAVE_BLOCK_ENTITY, MicrowaveBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityRegistry.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityRegistry.PLATE_BLOCK_ENTITY, PlateBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityRegistry.STOVE_BLOCK_ENTITY, StoveBlockEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);
    }

}

