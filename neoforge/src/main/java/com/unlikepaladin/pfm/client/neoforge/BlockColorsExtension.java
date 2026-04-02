package com.unlikepaladin.pfm.client.neoforge;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.Holder;

import java.util.Map;

public interface BlockColorsExtension {
    Map<Block, BlockColor> getColorMap();
}
