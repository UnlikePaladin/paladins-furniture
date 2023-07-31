package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.*;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Entities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;

public class EntityRenderRegistry {

    public static void registerBlockEntityRenderers() {
        registerBlockEntityRender(BlockEntities.MICROWAVE_BLOCK_ENTITY, MicrowaveBlockEntityRenderer::new);
        registerBlockEntityRender(BlockEntities.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntityRenderer::new);
        registerBlockEntityRender(BlockEntities.PLATE_BLOCK_ENTITY, PlateBlockEntityRenderer::new);
        registerBlockEntityRender(BlockEntities.STOVE_BLOCK_ENTITY, getStoveBlockEntityRenderer());
        registerBlockEntityRender(BlockEntities.TRASHCAN_BLOCK_ENTITY, TrashcanBlockEntityRenderer::new);
        registerBlockEntityRender(BlockEntities.TOASTER_BLOCK_ENTITY, PFMToasterBlockEntityRenderer::new);
        PaladinFurnitureMod.pfmModCompatibilities.forEach(pfmModCompatibility -> {
            if (pfmModCompatibility.getClientModCompatiblity().isPresent()){
                pfmModCompatibility.getClientModCompatiblity().get().registerBlockEntityRenderer();
            }
        });
    }

    public static void registerEntityRenderers() {
        registerEntityRender(Entities.CHAIR, ChairEntityRenderer::new);
        PaladinFurnitureMod.pfmModCompatibilities.forEach(pfmModCompatibility -> {
            if (pfmModCompatibility.getClientModCompatiblity().isPresent()){
                pfmModCompatibility.getClientModCompatiblity().get().registerEntityRenderer();
            }
        });
    }

    public static void registerModelLayers() {
        registerModelLayer(EntityRenderIDs.MODEL_CUBE_LAYER, ModelEmpty.getTexturedModelData());
    }

    @ExpectPlatform
    public static void registerBlockEntityRender(BlockEntityType blockEntityType, BlockEntityRendererFactory blockEntityRendererFactory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static void registerEntityRender(EntityType entityType, EntityRendererFactory entityRendererFactory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static void registerModelLayer(EntityModelLayer entityType, TexturedModelData texturedModelData) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static BlockEntityRendererFactory getStoveBlockEntityRenderer() {
        throw new AssertionError();
    }
}
