package com.unlikepaladin.pfm.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;

public class Tags {

    @ExpectPlatform
    public static TagKey<Block> getTuckableBlocks() {
        throw new AssertionError("Failed to get Tag");
    }
}
