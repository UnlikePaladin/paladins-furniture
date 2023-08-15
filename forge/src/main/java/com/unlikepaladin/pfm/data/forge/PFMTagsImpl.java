package com.unlikepaladin.pfm.data.forge;

import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

public class PFMTagsImpl {
    public static TagKey<Block> createTag(Identifier identifier) {
        return BlockTags.create(new Identifier("pfm", "tuckable_blocks"));
    }
}
