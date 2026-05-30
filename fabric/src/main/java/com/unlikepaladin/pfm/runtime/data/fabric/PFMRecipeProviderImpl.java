package com.unlikepaladin.pfm.runtime.data.fabric;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;

public class PFMRecipeProviderImpl {
    public static Identifier getId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static Identifier getId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }
}
