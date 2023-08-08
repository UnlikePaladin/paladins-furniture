package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.entity.render.StoveBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import java.util.function.Function;

public class EntityRenderRegistryImpl {
    public static <E extends BlockEntity> void registerBlockEntityRender(BlockEntityType blockEntityType, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<? super E>> blockEntityRendererFactory) {
        BlockEntityRendererRegistry.INSTANCE.register(blockEntityType, blockEntityRendererFactory);
    }

    public static <E extends Entity> void registerEntityRender(EntityType entityType, Function<EntityRenderDispatcher, EntityRenderer<? super E>> entityRendererFactory) {
        EntityRendererRegistry.INSTANCE.register((EntityType<?>) entityType, (manager, context) -> entityRendererFactory.apply(manager));
    }

    public static <E extends StoveBlockEntity> Function<BlockEntityRenderDispatcher, BlockEntityRenderer<? super E>> getStoveBlockEntityRenderer() {
        return StoveBlockEntityRenderer::new;
    }
}
