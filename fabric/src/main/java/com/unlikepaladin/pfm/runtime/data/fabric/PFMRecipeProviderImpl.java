package com.unlikepaladin.pfm.runtime.data.fabric;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class PFMRecipeProviderImpl {
    public static Identifier getId(Block block) {
        return Registries.BLOCK.getId(block);
    }
}
