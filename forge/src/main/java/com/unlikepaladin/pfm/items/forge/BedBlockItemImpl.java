package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.client.forge.PFMItemRendererForge;
import com.unlikepaladin.pfm.items.BedBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BedBlockItemImpl extends BedBlockItem {

    public BedBlockItemImpl(Block block, Settings settings) {
        super(block, settings);
    }

    public static BlockItem getItemFactory(Block block, Item.Settings settings) {
        return new BedBlockItemImpl(block, settings.setISTER(() -> PFMItemRendererForge::new));
    }
}
