package com.unlikepaladin.pfm.runtime.data.forge;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.ForgeRegistries;

public class PFMRecipeProviderImpl {
    public static Identifier getId(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static Identifier getId(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
}
