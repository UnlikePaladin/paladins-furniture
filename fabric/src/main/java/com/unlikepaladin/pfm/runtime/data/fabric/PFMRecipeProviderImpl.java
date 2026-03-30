package com.unlikepaladin.pfm.runtime.data.fabric;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

public class PFMRecipeProviderImpl {
    public static ResourceLocation getId(Block block) {
        return Registries.BLOCK.getKey(block);
    }

    public static ResourceLocation getId(Item item) {
        return Registries.ITEM.getKey(item);
    }
}
