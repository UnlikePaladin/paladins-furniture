package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.client.PFMCookingForBlockheadsClient;
import com.unlikepaladin.pfm.entity.render.StoveBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.EntityType;

public class EntityRenderRegistryImpl {
    public static void registerBlockEntityRender(BlockEntityType blockEntityType, BlockEntityRendererProvider blockEntityRendererFactory) {
        BlockEntityRendererRegistry.register(blockEntityType, blockEntityRendererFactory);
    }

    public static void registerEntityRender(EntityType entityType, EntityRendererProvider entityRendererFactory) {
        EntityRendererRegistry.register(entityType, entityRendererFactory);
    }

    public static void registerModelLayer(ModelLayerLocation entityType, LayerDefinition texturedModelData) {
        EntityModelLayerRegistry.registerModelLayer(entityType, () -> texturedModelData);
    }

    public static BlockEntityRendererProvider getStoveBlockEntityRenderer() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return PFMCookingForBlockheadsClient.getStoveRenderer();
        } else {
            return StoveBlockEntityRenderer::new;
        }
    }
}
