package com.unlikepaladin.pfm.compat.farmersdelight;

import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.data.PFMTags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;

public abstract class PFMFarmersDelight implements PFMModCompatibility {
    @ExpectPlatform
    public static PFMFarmersDelight getInstance() {
        throw new AssertionError();
    };

    protected static final TagKey<Block> HEAT_SOURCES = PFMTags.createTag(ResourceLocation.fromNamespaceAndPath("farmersdelight", "heat_sources"));
}
