package com.unlikepaladin.pfm.client.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ColorRegistryImpl {
    public static void registerBlockColor(Block block, List<BlockTintSource> blockTintSources) {
        BlockColorRegistry.register(blockTintSources, block);
    }

    public static void registerBlockColor(Block block, BlockTintSource blockTintSource) {
        BlockColorRegistry.register(List.of(blockTintSource), block);
    }

    public static void registerBlockToRenderLayer(Block block, ChunkSectionLayer renderLayer) {
        // No-op in 26.1
    }

    public static List<BlockTintSource> getBlockTintSources(Block block) {
        return Minecraft.getInstance().getBlockColors().getTintSources(block.defaultBlockState());
    }
}
