package com.unlikepaladin.pfm.client.fabric;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;

public class ColorRegistryImpl {
    public static void registerBlockColor(Block block, BlockColor blockColorProvider) {
        ColorProviderRegistry.BLOCK.register(blockColorProvider, block);
    }

    public static void registerBlockToRenderLayer(Block block, RenderType renderLayer) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
    }

    public static BlockColor getBlockColor(Block block) {
        return ColorProviderRegistry.BLOCK.get(block);
    }

}
