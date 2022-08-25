package com.unlikepaladin.pfm.client.forge;


import com.unlikepaladin.pfm.client.EntityRenderIDs;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.ChairEntityRenderer;
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
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions registerLayerDefinitions){
        registerLayerDefinitions.registerLayerDefinition(EntityRenderIDs.MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);
    }
}
