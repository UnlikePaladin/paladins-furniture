package com.unlikepaladin.pfm.data.fabric;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TagsImpl {

    public static TagKey<Block> TUCKABLE_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier("pfm", "tuckable_blocks"));

    public static TagKey<Block> getTuckableBlocks() {
        return TUCKABLE_BLOCKS;
    }
}
