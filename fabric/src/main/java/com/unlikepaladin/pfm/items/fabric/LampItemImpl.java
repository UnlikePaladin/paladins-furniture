package com.unlikepaladin.pfm.items.fabric;

import com.unlikepaladin.pfm.items.LampItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class LampItemImpl {
    public static BlockItem getItemFactory(Block block, Item.Properties settings) {
        return new LampItem(block, settings);
    }
}
