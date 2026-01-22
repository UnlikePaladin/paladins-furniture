package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.items.BedBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import java.util.function.Consumer;

public class BedBlockItemImpl extends BedBlockItem {

    public BedBlockItemImpl(Block block, Settings settings) {
        super(block, settings);
    }

    public static BlockItem getItemFactory(Block block, Item.Settings settings) {
        return new BedBlockItemImpl(block, settings);
    }
}
