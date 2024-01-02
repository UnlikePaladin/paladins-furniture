package com.unlikepaladin.pfm.data.neoforge;

import net.minecraft.block.Block;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class PFMTagsImpl {
    public static TagKey<Block> createTag(Identifier identifier) {
        return BlockTags.create(new Identifier("pfm", "tuckable_blocks"));
    }
}
