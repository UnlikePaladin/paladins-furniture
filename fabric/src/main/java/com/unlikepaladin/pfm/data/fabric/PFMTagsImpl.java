package com.unlikepaladin.pfm.data.fabric;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PFMTagsImpl {
    public static TagKey<Block> createTag(Identifier identifier) {
        return TagKey.of(Registry.BLOCK_KEY, identifier);
    }
}
