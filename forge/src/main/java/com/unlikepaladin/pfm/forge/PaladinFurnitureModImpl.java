package com.unlikepaladin.pfm.forge;

import com.unlikepaladin.pfm.PaladinFurnitureModUpdateChecker;
import com.unlikepaladin.pfm.compat.PaladinFurnitureModConfig;

public class PaladinFurnitureModImpl {
    public static PaladinFurnitureModConfig getPFMConfig() {
        return PaladinFurnitureModForge.pfmConfig.getConfig();
    }
}
