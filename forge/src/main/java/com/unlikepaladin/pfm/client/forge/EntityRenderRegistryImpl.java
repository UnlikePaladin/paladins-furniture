package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.client.PFMCookingForBlockheadsClient;
import com.unlikepaladin.pfm.entity.render.StoveBlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityRenderRegistryImpl {
    public static Map<BlockEntityType, Function<BlockEntityRenderDispatcher, BlockEntityRenderer>> blockEntityRendererFactoryMap = new HashMap<>();
    public static Map<EntityType, Function<EntityRenderDispatcher, EntityRenderer>> entityRendererFactoryMap = new HashMap<>();

    public static <E extends BlockEntity> void registerBlockEntityRender(BlockEntityType blockEntityType, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<? super E>> blockEntityRendererFactory) {
        blockEntityRendererFactoryMap.put(blockEntityType, (Function<BlockEntityRenderDispatcher, BlockEntityRenderer>)(Object)blockEntityRendererFactory);
    }

    public static <E extends Entity> void registerEntityRender(EntityType entityType, Function<EntityRenderDispatcher, EntityRenderer<? super E>> entityRendererFactory) {
        entityRendererFactoryMap.put(entityType, (Function<EntityRenderDispatcher, EntityRenderer>)(Object) entityRendererFactory);
    }

    public static <E extends StoveBlockEntity> Function<BlockEntityRenderDispatcher, BlockEntityRenderer<? super E>> getStoveBlockEntityRenderer() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return PFMCookingForBlockheadsClient.getStoveRenderer();
        } else {
            return StoveBlockEntityRenderer::new;
        }
    }
}
