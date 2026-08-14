package com.unlikepaladin.pfm.client.neoforge;

import com.unlikepaladin.pfm.client.ColorRegistry;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class ColorRegistryNeoForge {
    public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event){
        ColorRegistryImpl.blockColors = event.getBlockColors();
        ColorRegistry.registerBlockColors();
        ColorRegistryImpl.BLOCK_COLOR_PROVIDER_MAP.forEach((block, tintSources) -> event.getBlockColors().register(tintSources, block));
    }

    public static void registerBlockRenderLayers() {
        ColorRegistry.registerBlockRenderLayers();
    }
}
