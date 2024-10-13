package com.unlikepaladin.pfm.client.forge;

import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Map;

public interface BlockColorsExtension {
    Map<RegistryEntry.Reference<Block>, BlockColorProvider> getColorMap();
}
