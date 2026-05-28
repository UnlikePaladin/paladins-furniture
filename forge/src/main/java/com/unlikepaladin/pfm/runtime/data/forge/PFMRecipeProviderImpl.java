package com.unlikepaladin.pfm.runtime.data.forge;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class PFMRecipeProviderImpl {
    public static ResourceLocation getId(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static ResourceLocation getId(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
}
