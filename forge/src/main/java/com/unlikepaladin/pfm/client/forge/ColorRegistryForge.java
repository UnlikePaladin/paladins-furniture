package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.client.ColorRegistry;
import net.minecraft.client.render.RenderLayers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorRegistryForge {
    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event){
        ColorRegistryImpl.blockColors = event.getBlockColors();
        ColorRegistry.registerBlockColors();
        ColorRegistryImpl.BLOCK_COLOR_PROVIDER_MAP.forEach((block, blockColorProvider) -> event.getBlockColors().registerColorProvider(blockColorProvider, block));
    }
    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event){
        ColorRegistryImpl.itemColors = event.getItemColors();
        ColorRegistry.registerItemColors();
        ColorRegistryImpl.ITEM_COLOR_PROVIDER_MAP.forEach((item, colorProvider) -> event.getItemColors().register(colorProvider, item));
    }

    public static void registerBlockRenderLayers() {
        ColorRegistry.registerBlockRenderLayers();
        ColorRegistryImpl.BLOCK_RENDER_LAYER_MAP.forEach(RenderLayers::setRenderLayer);
    }
}
