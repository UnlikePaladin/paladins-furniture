package com.unlikepaladin.pfm.data.forge;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;

public class PFMTagsImpl {
    public static TagKey<Block> createTag(Identifier identifier) {
        return BlockTags.create(identifier);
    }
}
