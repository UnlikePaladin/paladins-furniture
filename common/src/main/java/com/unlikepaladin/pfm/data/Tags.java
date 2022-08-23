package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public class Tags {

    @ExpectPlatform
    public static Tag.Identified<Block> getTuckableBlocks() {
        throw new AssertionError("Failed to get Tag");
    }
}
