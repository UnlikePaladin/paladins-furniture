package com.unlikepaladin.pfm.runtime.data.fabric;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PFMRecipeProviderImpl {
    public static Identifier getId(Block block) {
        return Registry.BLOCK.getId(block);
    }

    public static Identifier getId(Item item) {
        return Registry.ITEM.getId(item);
    }
}
