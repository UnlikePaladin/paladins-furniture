package com.unlikepaladin.pfm.runtime.data.fabric;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PFMRecipeProviderImpl {
    public static Identifier getId(Block block) {
        return Registry.BLOCK.getId(block);
    }
}
