package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.entity.render.StoveBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;

public class EntityRenderRegistryImpl {
    public static void registerBlockEntityRender(BlockEntityType blockEntityType, BlockEntityRendererFactory blockEntityRendererFactory) {
        BlockEntityRendererRegistry.register(blockEntityType, blockEntityRendererFactory);
    }

    public static void registerEntityRender(EntityType entityType, EntityRendererFactory entityRendererFactory) {
        EntityRendererRegistry.register(entityType, entityRendererFactory);
    }

    public static void registerModelLayer(EntityModelLayer entityType, TexturedModelData texturedModelData) {
        EntityModelLayerRegistry.registerModelLayer(entityType, () -> texturedModelData);
    }

    public static BlockEntityRendererFactory getStoveBlockEntityRenderer() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return PFMCookingForBlockHeadsCompat.getStoveRenderer();
        } else {
            return StoveBlockEntityRenderer::new;
        }
    }
}
