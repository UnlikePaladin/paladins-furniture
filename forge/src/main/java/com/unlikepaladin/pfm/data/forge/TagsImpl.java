package com.unlikepaladin.pfm.data.forge;

import net.minecraft.block.Block;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TagsImpl {
    public static TagKey<Block> TUCKABLE_BLOCKS;

    public static TagKey<Block> getTuckableBlocks() {
        return TUCKABLE_BLOCKS;
    }
}
