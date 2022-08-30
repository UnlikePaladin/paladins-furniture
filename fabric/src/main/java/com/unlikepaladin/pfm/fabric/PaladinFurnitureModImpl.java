package com.unlikepaladin.pfm.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureModUpdateChecker;
import com.unlikepaladin.pfm.compat.PaladinFurnitureModConfig;
import net.fabricmc.loader.api.FabricLoader;

public class PaladinFurnitureModImpl {
    public static PaladinFurnitureModConfig getPFMConfig() {
        return PaladinFurnitureModFabric.pfmConfig.getConfig();
    }
}
