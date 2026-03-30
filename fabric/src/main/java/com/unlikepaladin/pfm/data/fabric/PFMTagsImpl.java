package com.unlikepaladin.pfm.data.fabric;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;

public class PFMTagsImpl {
    public static TagKey<Block> createTag(ResourceLocation identifier) {
        return TagKey.of(RegistryKeys.BLOCK, identifier);
    }
}
