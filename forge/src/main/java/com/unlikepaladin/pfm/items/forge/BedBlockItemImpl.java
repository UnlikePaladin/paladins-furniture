package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.client.forge.PFMItemRendererForge;
import com.unlikepaladin.pfm.items.BedBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class BedBlockItemImpl extends BedBlockItem {

    public BedBlockItemImpl(Block block, Properties settings) {
        super(block, settings);
    }

    public static BlockItem getItemFactory(Block block, Item.Properties settings) {
        return new BedBlockItemImpl(block, settings.setISTER(() -> PFMItemRendererForge::new));
    }
}
