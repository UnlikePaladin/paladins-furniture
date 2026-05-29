package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.client.PFMBuiltinItemRendererExtension;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;

public class BedBlockItem extends BlockItem implements PFMBuiltinItemRendererExtension {

    public BedBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    @ExpectPlatform
    public static BlockItem getItemFactory(Block block, Properties settings) {
        throw new AssertionError();
    }

}
