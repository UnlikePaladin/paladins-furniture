package com.unlikepaladin.pfm.data.fabric;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class TagsImpl {

    public static TagKey<Block> TUCKABLE_BLOCKS = TagKey.of(Registries.BLOCK.getKey(), new Identifier("pfm", "tuckable_blocks"));

    public static TagKey<Block> getTuckableBlocks() {
        return TUCKABLE_BLOCKS;
    }
}
