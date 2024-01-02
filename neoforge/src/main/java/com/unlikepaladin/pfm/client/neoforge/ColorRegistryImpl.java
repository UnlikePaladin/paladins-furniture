package com.unlikepaladin.pfm.client.neoforge;

import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ColorRegistryImpl {
    public static final Map<Block, BlockColorProvider> BLOCK_COLOR_PROVIDER_MAP = new HashMap<>();
    public static final Map<Block, RenderLayer> BLOCK_RENDER_LAYER_MAP = new HashMap<>();
    public static final Map<Item, ItemColorProvider> ITEM_COLOR_PROVIDER_MAP = new HashMap<>();


    public static void registerBlockColor(Block block, BlockColorProvider blockColorProvider) {
        BLOCK_COLOR_PROVIDER_MAP.put(block, blockColorProvider);
    }

    public static void registerBlockToRenderLayer(Block block, RenderLayer renderLayer) {
        BLOCK_RENDER_LAYER_MAP.put(block, renderLayer);
    }

    public static void registerItemColor(Item item, ItemColorProvider colorProvider) {
        ITEM_COLOR_PROVIDER_MAP.put(item, colorProvider);
    }
}
