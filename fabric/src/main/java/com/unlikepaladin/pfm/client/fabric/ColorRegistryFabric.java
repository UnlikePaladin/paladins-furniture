package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.client.ColorRegistry;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;

import java.util.ArrayList;
import java.util.List;

public class ColorRegistryFabric {
    public static void registerAll(){
        ColorRegistry.registerBlockColors();
        ColorRegistry.registerBlockRenderLayers();
        ColorRegistry.registerItemColors();
    }

}
