package com.unlikepaladin.pfm.data.fabric;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class PFMTagsImpl {
    public static TagKey<Block> createTag(Identifier identifier) {
        return TagKey.of(RegistryKeys.BLOCK, identifier);
    }
}
