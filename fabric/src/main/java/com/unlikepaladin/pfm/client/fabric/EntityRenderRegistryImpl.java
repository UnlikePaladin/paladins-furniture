package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
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
