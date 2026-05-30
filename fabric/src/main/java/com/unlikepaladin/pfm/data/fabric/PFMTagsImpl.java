package com.unlikepaladin.pfm.data.fabric;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;

public class PFMTagsImpl {
    public static TagKey<Block> createTag(Identifier identifier) {
        return TagKey.create(Registries.BLOCK, identifier);
    }
}
