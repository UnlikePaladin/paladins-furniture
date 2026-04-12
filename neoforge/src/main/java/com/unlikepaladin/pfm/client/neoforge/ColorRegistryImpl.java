package com.unlikepaladin.pfm.client.neoforge;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ColorRegistryImpl {
    public static final Map<Block, BlockColor> BLOCK_COLOR_PROVIDER_MAP = new HashMap<>();
    public static final Map<Block, RenderType> BLOCK_RENDER_LAYER_MAP = new HashMap<>();
    public static final Map<Item, ItemColor> ITEM_COLOR_PROVIDER_MAP = new HashMap<>();

    public static BlockColors blockColors;
    public static ItemColors itemColors;

    public static void registerBlockColor(Block block, BlockColor blockColorProvider) {
        BLOCK_COLOR_PROVIDER_MAP.put(block, blockColorProvider);
    }

    public static void registerBlockToRenderLayer(Block block, RenderType renderLayer) {
        BLOCK_RENDER_LAYER_MAP.put(block, renderLayer);
    }

    public static void registerItemColor(Item item, ItemColor colorProvider) {
        ITEM_COLOR_PROVIDER_MAP.put(item, colorProvider);
    }

    public static BlockColor getBlockColor(Block block) {
        if (BLOCK_COLOR_PROVIDER_MAP.containsKey(block)) {
            return BLOCK_COLOR_PROVIDER_MAP.get(block);
        }
        return ((BlockColorsExtension) blockColors).getColorMap().get(block);
    }

    public static ItemColor getItemColor(Item item) {
        if (ITEM_COLOR_PROVIDER_MAP.containsKey(item)) {
            return ITEM_COLOR_PROVIDER_MAP.get(item);
        }
        return ((ItemColorsExtension) itemColors).getColorMap().get(item);
    }
}
