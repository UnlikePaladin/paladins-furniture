package com.unlikepaladin.pfm.data.fabric;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class TagsImpl {

    public static Tag.Identified<Block> TUCKABLE_BLOCKS = TagFactory.BLOCK.create(new Identifier("pfm", "tuckable_blocks"));

    public static Tag.Identified<Block> getTuckableBlocks() {
        return TUCKABLE_BLOCKS;
    }
}
