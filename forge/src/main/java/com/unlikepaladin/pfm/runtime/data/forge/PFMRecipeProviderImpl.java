package com.unlikepaladin.pfm.runtime.data.forge;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.minecraftforge.registries.ForgeRegistries;

public class PFMRecipeProviderImpl {
    public static Identifier getId(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static Identifier getId(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
}
