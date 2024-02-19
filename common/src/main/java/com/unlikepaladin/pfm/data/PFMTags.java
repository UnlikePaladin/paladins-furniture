package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class PFMTags {
    public static Tag.Identified<Block> TUCKABLE_BLOCKS = createTag(new Identifier("pfm", "tuckable_blocks"));
    public static Tag.Identified<Block> FURNITURE = createTag(new Identifier("pfm", "furniture"));

    @ExpectPlatform
    public static Tag.Identified<Block> createTag(Identifier identifier) {
        throw new AssertionError();
    }
}
