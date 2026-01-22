package com.unlikepaladin.pfm.items.fabric;

import com.unlikepaladin.pfm.items.BedBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BedBlockItemImpl {
    public static BlockItem getItemFactory(Block block, Item.Settings settings) {
        return new BedBlockItem(block, settings);
    }
}
