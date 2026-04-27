package com.unlikepaladin.pfm.items.fabric;

import com.unlikepaladin.pfm.items.BedBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class BedBlockItemImpl {
    public static BlockItem getItemFactory(Block block, Item.Properties settings) {
        return new BedBlockItem(block, settings);
    }
}
