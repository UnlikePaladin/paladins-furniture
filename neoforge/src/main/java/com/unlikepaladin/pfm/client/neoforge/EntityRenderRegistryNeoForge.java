package com.unlikepaladin.pfm.client.neoforge;


import com.unlikepaladin.pfm.client.EntityRenderRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "pfm", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRenderRegistryNeoForge {
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
