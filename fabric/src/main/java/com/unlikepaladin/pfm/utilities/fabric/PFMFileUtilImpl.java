package com.unlikepaladin.pfm.utilities.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PFMFileUtilImpl {
    public static Path getGamePath() {
        return FabricLoader.getInstance().getGameDir();
    }
}
