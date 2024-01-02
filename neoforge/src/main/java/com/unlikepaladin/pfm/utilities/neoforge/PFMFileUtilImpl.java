package com.unlikepaladin.pfm.utilities.neoforge;


import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class PFMFileUtilImpl {
    public static Path getGamePath() {
        return FMLPaths.GAMEDIR.relative().normalize();
    }
}
