package com.unlikepaladin.pfm.fabric;

import com.unlikepaladin.pfm.compat.PaladinFurnitureModConfig;

public class PaladinFurnitureModImpl {
    public static PaladinFurnitureModConfig getPFMConfig() {
        return PaladinFurnitureModFabric.pfmConfig.getConfig();
    }
}
