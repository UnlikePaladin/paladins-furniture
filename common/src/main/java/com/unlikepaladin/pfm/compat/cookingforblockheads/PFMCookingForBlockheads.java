package com.unlikepaladin.pfm.compat.cookingforblockheads;

import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import dev.architectury.injectables.annotations.ExpectPlatform;

public abstract class PFMCookingForBlockheads implements PFMModCompatibility {
    @ExpectPlatform
    public static PFMCookingForBlockheads getInstance() {
        throw new AssertionError();
    }
}