package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;

public class PFMTags {
    public static TagKey<Block> TUCKABLE_BLOCKS = createTag(new ResourceLocation("pfm", "tuckable_blocks"));
    public static TagKey<Block> FURNITURE = createTag(new ResourceLocation("pfm", "furniture"));

    @ExpectPlatform
    public static TagKey<Block> createTag(ResourceLocation identifier) {
        throw new AssertionError();
    }
}
