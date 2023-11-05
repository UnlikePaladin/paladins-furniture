package com.unlikepaladin.pfm.compat.imm_ptl;

import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class PFMImmersivePortals {
    @ExpectPlatform
    public static PFMModCompatibility getInstance() {
        throw new AssertionError();
    }
}
