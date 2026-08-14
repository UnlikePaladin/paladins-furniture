package com.unlikepaladin.pfm.client.forge;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorRegistryImpl {
    public static final Map<Block, List<BlockTintSource>> BLOCK_COLOR_PROVIDER_MAP = new HashMap<>();
    public static final Map<Block, ChunkSectionLayer> BLOCK_RENDER_LAYER_MAP = new HashMap<>();

    public static BlockColors blockColors;

    public static void registerBlockColor(Block block, List<BlockTintSource> blockTintSources) {
        BLOCK_COLOR_PROVIDER_MAP.put(block, blockTintSources);
    }

    public static void registerBlockColor(Block block, BlockTintSource blockTintSource) {
        BLOCK_COLOR_PROVIDER_MAP.put(block, List.of(blockTintSource));
    }

    public static void registerBlockToRenderLayer(Block block, ChunkSectionLayer renderLayer) {
        BLOCK_RENDER_LAYER_MAP.put(block, renderLayer);
    }

    public static List<BlockTintSource> getBlockTintSources(Block block) {
        if (BLOCK_COLOR_PROVIDER_MAP.containsKey(block)) {
            return BLOCK_COLOR_PROVIDER_MAP.get(block);
        }
        if (blockColors != null) {
            return ((BlockColorsExtension) blockColors).getColorMap().get(block);
        }
        return null;
    }
}
