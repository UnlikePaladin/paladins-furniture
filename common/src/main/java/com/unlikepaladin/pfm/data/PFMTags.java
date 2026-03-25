package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;

public class PFMTags {
    public static Tag.Named<Block> TUCKABLE_BLOCKS = createTag(new ResourceLocation("pfm", "tuckable_blocks"));
    public static Tag.Named<Block> FURNITURE = createTag(new ResourceLocation("pfm", "furniture"));

    @ExpectPlatform
    public static Tag.Named<Block> createTag(ResourceLocation identifier) {
        throw new AssertionError();
    }
}
