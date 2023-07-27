package com.unlikepaladin.pfm.items.fabric;

import com.unlikepaladin.pfm.items.LampItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class LampItemImpl {
    public static BlockItem getItemFactory(Block block, Item.Settings settings) {
        return new LampItem(block, settings);
    }
}
