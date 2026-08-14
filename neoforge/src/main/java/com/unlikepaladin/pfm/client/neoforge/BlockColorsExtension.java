package com.unlikepaladin.pfm.client.neoforge;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

public interface BlockColorsExtension {
    Map<Block, List<BlockTintSource>> getColorMap();
}
