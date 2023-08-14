package com.unlikepaladin.pfm.runtime;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePack;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PFMRuntimeResources {
    public static final DirectoryResourcePack ASSETS_PACK = new DirectoryResourcePack(getResourceDirectory().toFile());
    public static volatile List<ResourcePack> RESOURCE_PACK_LIST;

    public static Path getResourceDirectory() {
        return createDirIfNeeded(getPFMDirectory().resolve("cache/resources"));
    }

    public static Path getPFMDirectory() {
        Path p = PFMFileUtil.getGamePath().resolve(PaladinFurnitureMod.MOD_ID);
        return createDirIfNeeded(p);
    }

    public static Path getAssetsDirectory() {
        return createDirIfNeeded(getResourceDirectory().resolve("assets/" + PaladinFurnitureMod.MOD_ID));
    }

    public static Path createDirIfNeeded(Path path) {
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException ignored) {
        } catch (Exception e) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Failed to create directory", e);
        }
        return path;
    }

    private static CompletableFuture<Void> future;
    public static CompletableFuture<Void> prepareAsyncResourceGen(boolean logOrDebug) {
        return future = CompletableFuture.runAsync(() -> {
            PFMDataGen dataGen = new PFMDataGen(PFMRuntimeResources.getResourceDirectory(), logOrDebug);
            try {
                dataGen.run();
            } catch (IOException e) {
                PFMDataGen.LOGGER.error("Failed to run data generation {}", e);
                throw new RuntimeException(e);
            }
        });
    }
    public static boolean ready = false;
    public static void prepareAndRunResourceGen(boolean logOrDebug) {
        PFMDataGen dataGen = new PFMDataGen(PFMRuntimeResources.getResourceDirectory(), logOrDebug);
        try {
            dataGen.run();
        } catch (IOException e) {
            PFMDataGen.LOGGER.error("Failed to run data generation {}", e);
            throw new RuntimeException(e);
        }
    }

    public static void runAsyncResourceGen() {
        if (future != null && !future.isDone())
            future.join();
    }

}
