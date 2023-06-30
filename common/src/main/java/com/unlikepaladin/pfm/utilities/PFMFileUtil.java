package com.unlikepaladin.pfm.utilities;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class PFMFileUtil {

    @ExpectPlatform
    public static Path getGamePath() {
        throw new RuntimeException();
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
}
