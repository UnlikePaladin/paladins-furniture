package com.unlikepaladin.pfm.client.forge;


import com.unlikepaladin.pfm.client.EntityRenderIDs;
import com.unlikepaladin.pfm.client.EntityRenderRegistry;
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
        EntityRenderRegistry.registerEntityRenderers();
        EntityRenderRegistryImpl.entityRendererFactoryMap.forEach(renderersEvent::registerEntityRenderer);
    }

    @SubscribeEvent
    public static void registerBlockEntityRender(EntityRenderersEvent.RegisterRenderers renderersEvent){
        EntityRenderRegistry.registerBlockEntityRenderers();
        EntityRenderRegistryImpl.blockEntityRendererFactoryMap.forEach(renderersEvent::registerBlockEntityRenderer);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions registerLayerDefinitions){
        EntityRenderRegistry.registerModelLayers();
        EntityRenderRegistryImpl.entityModelLayerTexturedModelDataMap.forEach((entityModelLayer, texturedModelData) -> registerLayerDefinitions.registerLayerDefinition(entityModelLayer, () -> texturedModelData));
    }
}
