package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.client.PFMBuiltinItemRendererExtension;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class BedBlockItem extends BlockItem implements PFMBuiltinItemRendererExtension {

    public BedBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @ExpectPlatform
    public static BlockItem getItemFactory(Block block, Settings settings) {
        throw new AssertionError();
    }

}
