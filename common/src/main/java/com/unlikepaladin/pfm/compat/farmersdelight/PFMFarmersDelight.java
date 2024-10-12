package com.unlikepaladin.pfm.compat.farmersdelight;

import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.data.PFMTags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public abstract class PFMFarmersDelight implements PFMModCompatibility {
    @ExpectPlatform
    public static PFMFarmersDelight getInstance() {
        throw new AssertionError();
    };

    protected static final TagKey<Block> HEAT_SOURCES = PFMTags.createTag(Identifier.of("farmersdelight", "heat_sources"));
}
