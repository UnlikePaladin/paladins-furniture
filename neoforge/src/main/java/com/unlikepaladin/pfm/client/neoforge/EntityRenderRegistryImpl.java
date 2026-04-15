package com.unlikepaladin.pfm.client.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.client.PFMCookingForBlockheadsClient;
import com.unlikepaladin.pfm.entity.render.StoveBlockEntityRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityRenderRegistryImpl {
    public static Map<BlockEntityType, BlockEntityRendererProvider> blockEntityRendererFactoryMap = new HashMap<>();
    public static Map<EntityType, EntityRendererProvider> entityRendererFactoryMap = new HashMap<>();
    public static Map<ModelLayerLocation, LayerDefinition> entityModelLayerTexturedModelDataMap = new HashMap<>();

    public static void registerBlockEntityRender(BlockEntityType blockEntityType, BlockEntityRendererProvider blockEntityRendererFactory) {
        blockEntityRendererFactoryMap.put(blockEntityType, blockEntityRendererFactory);
    }

    public static void registerEntityRender(EntityType entityType, EntityRendererProvider entityRendererFactory) {
        entityRendererFactoryMap.put(entityType, entityRendererFactory);
    }

    public static void registerModelLayer(ModelLayerLocation entityType, LayerDefinition texturedModelData) {
        entityModelLayerTexturedModelDataMap.put(entityType, texturedModelData);
    }

    public static BlockEntityRendererProvider getStoveBlockEntityRenderer() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return PFMCookingForBlockheadsClient.getStoveRenderer();
        } else {
            return StoveBlockEntityRenderer::new;
        }
    }
}
