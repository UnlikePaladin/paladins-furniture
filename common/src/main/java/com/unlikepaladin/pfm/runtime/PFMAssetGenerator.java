package com.unlikepaladin.pfm.runtime;

import com.google.common.base.Stopwatch;
import com.mojang.bridge.game.PackType;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.runtime.assets.PFMBlockstateModelProvider;
import com.unlikepaladin.pfm.runtime.assets.PFMLangProvider;
import com.unlikepaladin.pfm.runtime.data.PFMMCMetaProvider;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.minecraft.data.DataCache;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PFMAssetGenerator extends PFMGenerator {
    private static boolean FROZEN = false;
    public PFMAssetGenerator(Path output, boolean logOrDebug) {
        super(output, logOrDebug, LogManager.getLogger("PFM-Asset-Generation"));
    }

    public void run() throws IOException {
        if (!FROZEN) {
            log("Packs:");
            for (ResourcePack pack : PFMRuntimeResources.RESOURCE_PACK_LIST) {
                log("\tPack {}", pack.getName());
                for (String namespace : pack.getNamespaces(ResourceType.CLIENT_RESOURCES)) {
                    log("\t\tNamespace {}", namespace);
                }
            }
            this.setRunning(true);
            FROZEN = true;

            Path modListPath = output.resolve("modsList");
            Path hashPath = output.resolve("dataHash");
            if (!modListPath.toFile().isFile()) {
                Files.deleteIfExists(modListPath);
                Files.createFile(modListPath);
            }
            if (!hashPath.toFile().isFile()) {
                Files.deleteIfExists(hashPath);
                Files.createFile(hashPath);
            }
            List<String> hashToCompare = hashDirectory(output.toFile(), true);
            List<String> oldHash = Files.readAllLines(hashPath);
            List<String> modList = Files.readAllLines(modListPath);
            if (!hashToCompare.toString().equals(oldHash.toString()) || !modList.toString().replace("[", "").replace("]", "").equals(PaladinFurnitureMod.getVersionMap().toString())) {
                getLogger().info("Starting PFM Asset Generation");
                PFMFileUtil.deleteDir(output.toFile());
                PFMRuntimeResources.createDirIfNeeded(output);
                DataCache dataCache = new DataCache(this.output, "cache");
                dataCache.ignore(this.output.resolve("version.json"));
                Stopwatch stopwatch = Stopwatch.createStarted();
                Stopwatch stopwatch2 = Stopwatch.createUnstarted();

                log("Starting provider: {}", "PFM Asset MC Meta");
                stopwatch2.start();
                new PFMMCMetaProvider(this).run(PackType.RESOURCE, "PFM-Assets");
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM Asset MC Meta", stopwatch2.elapsed(TimeUnit.MILLISECONDS));

                log("Starting provider: {}", "PFM Blockstates and Models");
                stopwatch2.start();
                new PFMBlockstateModelProvider(this).run(dataCache);
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM Blockstates and Models", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                stopwatch2.reset();

                log("Starting provider: {}", "PFM Lang");
                stopwatch2.start();
                new PFMLangProvider(this).run();
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM Lang", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                stopwatch2.reset();

                getLogger().info("Asset providers took: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
                dataCache.write();
                Files.deleteIfExists(hashPath);
                Files.createFile(hashPath);
                List<String> newDataHash = hashDirectory(output.toFile(), true);
                Files.writeString(PFMRuntimeResources.createDirIfNeeded(hashPath), newDataHash.toString().replace("[", "").replace("]", ""), StandardOpenOption.APPEND);

                Files.deleteIfExists(modListPath);
                Files.createFile(modListPath);
                Files.writeString(PFMRuntimeResources.createDirIfNeeded(modListPath), PaladinFurnitureMod.getVersionMap().toString().replace("[", "").replace("]", ""), StandardOpenOption.APPEND);

                this.createPackIcon();
                this.setRunning(false);
            } else {
                log("Data Hash and Mod list matched, skipping generation");
            }
        }
    }
}
