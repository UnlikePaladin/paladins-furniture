package com.unlikepaladin.pfm.client.forge;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

public interface BlockColorsExtension {
    Map<Block, List<BlockTintSource>> getColorMap();
}
