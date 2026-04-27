package com.unlikepaladin.pfm.client.forge;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;
import net.minecraftforge.registries.IRegistryDelegate;

import java.util.Map;

public interface BlockColorsExtension {
    Map<IRegistryDelegate<Block>, BlockColor> getColorMap();
}
