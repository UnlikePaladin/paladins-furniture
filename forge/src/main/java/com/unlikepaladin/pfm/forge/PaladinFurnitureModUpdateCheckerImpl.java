package com.unlikepaladin.pfm.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Path;

public class PaladinFurnitureModUpdateCheckerImpl {
    public static File getUpdateFile() {
        Path path = FMLPaths.GAMEDIR.get().resolve("pfmUpdateInfo.json");
        return path.toFile();
    }
}
