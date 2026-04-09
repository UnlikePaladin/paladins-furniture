package com.unlikepaladin.pfm.data.neoforge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class PFMTagsImpl {
    public static TagKey<Block> createTag(ResourceLocation identifier) {
        return BlockTags.create(identifier);
    }
}
