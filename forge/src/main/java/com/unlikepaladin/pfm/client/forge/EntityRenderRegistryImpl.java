package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.client.PFMCookingForBlockheadsClient;
import com.unlikepaladin.pfm.entity.render.StoveBlockEntityRenderer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

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
