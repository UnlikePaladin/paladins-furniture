package com.unlikepaladin.pfm.client.forge;


import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.*;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Entities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRenderRegistryForge {
    @SubscribeEvent
    public static void registerRender(FMLClientSetupEvent renderersEvent){
        RenderingRegistry.registerEntityRenderingHandler(Entities.CHAIR, ChairEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockEntityRender(FMLClientSetupEvent renderersEvent){
        ClientRegistry.bindTileEntityRenderer(BlockEntities.MICROWAVE_BLOCK_ENTITY, MicrowaveBlockEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(BlockEntities.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(BlockEntities.PLATE_BLOCK_ENTITY, PlateBlockEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(BlockEntities.STOVE_BLOCK_ENTITY, StoveBlockEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(BlockEntities.TRASHCAN_BLOCK_ENTITY, TrashcanBlockEntityRenderer::new);
    }
}
