package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;

public class PFMTags {
    public static TagKey<Block> TUCKABLE_BLOCKS = createTag(ResourceLocation.fromNamespaceAndPath("pfm", "tuckable_blocks"));
    public static TagKey<Block> FURNITURE = createTag(ResourceLocation.fromNamespaceAndPath("pfm", "furniture"));
    public static TagKey<Block> KITCHEN_SINKS = createTag(ResourceLocation.fromNamespaceAndPath("pfm", "kitchen_sinks"));
    
    @ExpectPlatform
    public static TagKey<Block> createTag(ResourceLocation identifier) {
        throw new AssertionError();
    }
}
