package com.unlikepaladin.pfm.client.forge;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;

import java.util.Map;

public interface BlockColorsExtension {
    Map<Holder.Reference<Block>, BlockColor> getColorMap();
}
