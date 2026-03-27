package com.unlikepaladin.pfm.data.forge;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;

public class PFMTagsImpl {
    public static Tag.Named<Block> createTag(ResourceLocation identifier) {
        return BlockTags.createOptional(new ResourceLocation("pfm", "tuckable_blocks"));
    }
}
