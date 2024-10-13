package com.unlikepaladin.pfm.client.neoforge;

import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Map;

public interface BlockColorsExtension {
    Map<Block, BlockColorProvider> getColorMap();
}
