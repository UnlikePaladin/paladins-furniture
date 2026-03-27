package com.unlikepaladin.pfm.data.materials;

import net.minecraft.resources.ResourceLocation;

public class VariantHelper {
    public static VariantBase<?> getVariant(ResourceLocation variant) {
        if (WoodVariantRegistry.getOptionalVariant(variant).isPresent()) {
            return WoodVariantRegistry.getOptionalVariant(variant).get();
        } else if (StoneVariantRegistry.getOptionalVariant(variant).isPresent()) {
            return StoneVariantRegistry.getOptionalVariant(variant).get();
        } else if (ExtraStoolVariant.getOptionalVariant(variant).isPresent()) {
            return ExtraStoolVariant.getOptionalVariant(variant).get();
        } else if (ExtraCounterVariant.getOptionalVariant(variant).isPresent()) {
            return ExtraCounterVariant.getOptionalVariant(variant).get();
        }
        return null;
    }
}