package com.unlikepaladin.pfm.client.model;

import net.minecraft.resources.Identifier;

import java.util.Optional;

public interface PFMModelVariantExtension {
    Optional<Identifier> pfm$getCustomType();

    void pfm$setCustomType(Identifier customType);
}
