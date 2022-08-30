package com.unlikepaladin.pfm.data.forge;

import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraftforge.event.RegistryEvent;

public class TagsImpl {
    public static TagKey<Block> TUCKABLE_BLOCKS;


    public static TagKey<Block> getTuckableBlocks() {
        return TUCKABLE_BLOCKS;
    }
}
