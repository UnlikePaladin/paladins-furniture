package com.unlikepaladin.pfm.client.model;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public interface PFMModelVariantExtension {
    Optional<ResourceLocation> pfm$getCustomType();

    void pfm$setCustomType(ResourceLocation customType);
}
