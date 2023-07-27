package com.unlikepaladin.pfm.runtime.data.forge;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.ForgeRegistries;

public class PFMRecipeProviderImpl {
    public static Identifier getId(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
}
