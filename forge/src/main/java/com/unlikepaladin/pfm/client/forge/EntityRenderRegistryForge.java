package com.unlikepaladin.pfm.client.forge;


import com.unlikepaladin.pfm.client.EntityRenderIDs;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.*;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Entities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRenderRegistryForge {
    @SubscribeEvent
    public static void registerRender(EntityRenderersEvent.RegisterRenderers renderersEvent){
        renderersEvent.registerEntityRenderer(Entities.CHAIR, ChairEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockEntityRender(EntityRenderersEvent.RegisterRenderers renderersEvent){
        renderersEvent.registerBlockEntityRenderer(BlockEntities.MICROWAVE_BLOCK_ENTITY, MicrowaveBlockEntityRenderer::new);
        renderersEvent.registerBlockEntityRenderer(BlockEntities.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntityRenderer::new);
        renderersEvent.registerBlockEntityRenderer(BlockEntities.PLATE_BLOCK_ENTITY, PlateBlockEntityRenderer::new);
        renderersEvent.registerBlockEntityRenderer(BlockEntities.STOVE_BLOCK_ENTITY, StoveBlockEntityRenderer::new);
        renderersEvent.registerBlockEntityRenderer(BlockEntities.TRASHCAN_BLOCK_ENTITY, TrashcanBlockEntityRenderer::new);
    }


    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions registerLayerDefinitions){
        registerLayerDefinitions.registerLayerDefinition(EntityRenderIDs.MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);
    }
}
