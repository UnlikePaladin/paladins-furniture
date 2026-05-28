package com.unlikepaladin.pfm.items.neoforge;

import com.unlikepaladin.pfm.items.BedBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;

import java.util.function.Consumer;

public class BedBlockItemImpl extends BedBlockItem {

    public BedBlockItemImpl(Block block, Properties settings) {
        super(block, settings);
    }

    public static BlockItem getItemFactory(Block block, Properties settings) {
        return new BedBlockItemImpl(block, settings);
    }
}
