package com.unlikepaladin.pfm.data.forge;

import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class PFMTagsImpl {
    public static Tag.Identified<Block> createTag(Identifier identifier) {
        return BlockTags.createOptional(new Identifier("pfm", "tuckable_blocks"));
    }
}
