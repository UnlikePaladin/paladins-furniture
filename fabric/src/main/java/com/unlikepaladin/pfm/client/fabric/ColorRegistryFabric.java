package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.client.ColorRegistry;

public class ColorRegistryFabric {
    public static void registerAll(){
        ColorRegistry.registerBlockColors();
        ColorRegistry.registerBlockRenderLayers();
        ColorRegistry.registerItemColors();
    }

}
