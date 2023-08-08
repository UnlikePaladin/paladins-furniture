package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.*;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Entities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import java.util.function.Function;

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

    @ExpectPlatform
    public static <E extends BlockEntity> void registerBlockEntityRender(BlockEntityType blockEntityType, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<? super E>> blockEntityRendererFactory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <E extends Entity> void registerEntityRender(EntityType entityType, Function<EntityRenderDispatcher, EntityRenderer<? super E>> entityRendererFactory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <E extends StoveBlockEntity> Function<BlockEntityRenderDispatcher, BlockEntityRenderer<? super E>> getStoveBlockEntityRenderer() {
        throw new AssertionError();
    }
}
