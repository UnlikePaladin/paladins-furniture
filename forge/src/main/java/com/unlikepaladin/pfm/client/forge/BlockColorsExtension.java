package com.unlikepaladin.pfm.client.forge;

import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraftforge.registries.IRegistryDelegate;

import java.util.Map;

public interface BlockColorsExtension {
    Map<IRegistryDelegate<Block>, BlockColorProvider> getColorMap();
}
