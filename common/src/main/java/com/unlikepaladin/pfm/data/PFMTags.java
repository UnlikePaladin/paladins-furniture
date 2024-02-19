package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

public class PFMTags {
    public static TagKey<Block> TUCKABLE_BLOCKS = createTag(new Identifier("pfm", "tuckable_blocks"));
    public static TagKey<Block> FURNITURE = createTag(new Identifier("pfm", "furniture"));

    @ExpectPlatform
    public static TagKey<Block> createTag(Identifier identifier) {
        throw new AssertionError();
    }
}
