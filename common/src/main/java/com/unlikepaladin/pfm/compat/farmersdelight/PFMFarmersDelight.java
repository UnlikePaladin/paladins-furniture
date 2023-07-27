package com.unlikepaladin.pfm.compat.farmersdelight;

import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import dev.architectury.injectables.annotations.ExpectPlatform;

public abstract class PFMFarmersDelight implements PFMModCompatibility {
    @ExpectPlatform
    public static PFMFarmersDelight getInstance() {
        throw new AssertionError();
    };
}
