package com.unlikepaladin.pfm.data.fabric;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;

public class PFMTagsImpl {
    public static Tag.Named<Block> createTag(ResourceLocation identifier) {
        return (Tag.Identified<Block>) TagRegistry.block(identifier);
    }
}
