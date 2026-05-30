package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.Identifier;

public class PFMTags {
    public static TagKey<Block> TUCKABLE_BLOCKS = createTag(Identifier.fromNamespaceAndPath("pfm", "tuckable_blocks"));
    public static TagKey<Block> FURNITURE = createTag(Identifier.fromNamespaceAndPath("pfm", "furniture"));

    @ExpectPlatform
    public static TagKey<Block> createTag(Identifier identifier) {
        throw new AssertionError();
    }
}
