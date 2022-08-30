package com.unlikepaladin.pfm.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Path;

public class PaladinFurnitureModUpdateCheckerImpl {
    public static File getUpdateFile() {
        Path path = FabricLoader.getInstance().getGameDir().resolve("pfmUpdateInfo.json");
        return path.toFile();
    }
}
