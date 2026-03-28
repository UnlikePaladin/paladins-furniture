package com.unlikepaladin.pfm.client.forge;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Map;

public interface BlockColorsExtension {
    Map<RegistryEntry.Reference<Block>, BlockColor> getColorMap();
}
