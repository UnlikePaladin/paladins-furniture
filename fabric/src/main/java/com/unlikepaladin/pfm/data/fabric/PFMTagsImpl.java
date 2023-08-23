package com.unlikepaladin.pfm.data.fabric;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class PFMTagsImpl {
    public static Tag.Identified<Block> createTag(Identifier identifier) {
        return (Tag.Identified<Block>) TagRegistry.block(identifier);
    }
}
