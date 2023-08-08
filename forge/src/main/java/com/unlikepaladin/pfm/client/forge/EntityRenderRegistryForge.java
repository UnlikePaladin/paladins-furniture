package com.unlikepaladin.pfm.client.forge;


import com.unlikepaladin.pfm.client.EntityRenderIDs;
import com.unlikepaladin.pfm.client.EntityRenderRegistry;
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
import net.minecraftforge.fmllegacy.network.NetworkEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRenderRegistryForge {
    @SubscribeEvent
    public static void registerRender(FMLClientSetupEvent event){
        EntityRenderRegistry.registerEntityRenderers();
        EntityRenderRegistryImpl.entityRendererFactoryMap.forEach((entityType, entityRenderDispatcherEntityRendererFunction) -> {
            RenderingRegistry.registerEntityRenderingHandler(entityType, entityRenderDispatcherEntityRendererFunction::apply);
        });
    }

    @SubscribeEvent
    public static void registerBlockEntityRender(FMLClientSetupEvent event){
        EntityRenderRegistry.registerBlockEntityRenderers();
        EntityRenderRegistryImpl.blockEntityRendererFactoryMap.forEach(ClientRegistry::bindTileEntityRenderer);
    }
}
