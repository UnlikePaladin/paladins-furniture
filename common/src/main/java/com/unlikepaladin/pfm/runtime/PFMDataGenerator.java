package com.unlikepaladin.pfm.runtime;

import com.google.common.base.Stopwatch;
import com.google.common.hash.HashCode;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.runtime.assets.PFMBlockstateModelProvider;
import com.unlikepaladin.pfm.runtime.assets.PFMLangProvider;
import com.unlikepaladin.pfm.runtime.data.PFMLootTableProvider;
import com.unlikepaladin.pfm.runtime.data.PFMMCMetaProvider;
import com.unlikepaladin.pfm.runtime.data.PFMRecipeProvider;
import com.unlikepaladin.pfm.runtime.data.PFMTagProvider;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.minecraft.data.DataCache;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PFMDataGenerator extends PFMGenerator {
    public static boolean FROZEN = false;
    public PFMDataGenerator(Path output, boolean logOrDebug) {
        super(output, logOrDebug, LogManager.getLogger("PFM-DataGen"));
    }
    public void run() throws IOException {
        if (!FROZEN) {
            log("Packs:");
            for (ResourcePack pack : PFMRuntimeResources.RESOURCE_PACK_LIST) {
                log("\tPack {}", pack.getName());
                for (String namespace : pack.getNamespaces(ResourceType.SERVER_DATA)) {
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
            List<String> hashToCompare = hashDirectory(output.toFile(), false);
            List<String> oldHash = Files.readAllLines(hashPath);
            List<String> modList = Files.readAllLines(modListPath);
            if (!hashToCompare.toString().equals(oldHash.toString()) || !modList.toString().replace("[", "").replace("]", "").equals(PaladinFurnitureMod.getVersionMap().toString())) {
                getLogger().info("Starting PFM Data Generation");
                PFMFileUtil.deleteDir(output.toFile());
                DataCache dataCache = new DataCache(output, "cache");
                dataCache.ignore(output.resolve("version.json"));
                Stopwatch stopwatch = Stopwatch.createStarted();
                Stopwatch stopwatch2 = Stopwatch.createUnstarted();

                log("Starting provider: {}", "PFM PFMTags");
                stopwatch2.start();
                new PFMTagProvider(this).run(dataCache);
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM PFMTags", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                stopwatch2.reset();

                log("Starting provider: {}", "PFM Drops");
                stopwatch2.start();
                new PFMLootTableProvider(this).run(dataCache);
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM Drops", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                stopwatch2.reset();

                log("Starting provider: {}", "PFM Recipes");
                stopwatch2.start();
                new PFMRecipeProvider(this).run(dataCache);
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM Recipes", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                stopwatch2.reset();

                log("Starting provider: {}", "PFM MC Meta");
                stopwatch2.start();
                new PFMMCMetaProvider(this).run("PFM-Data");
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM MC Meta", stopwatch2.elapsed(TimeUnit.MILLISECONDS));

                getLogger().info("Data providers took: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

                dataCache.write();

                Files.deleteIfExists(hashPath);
                Files.createFile(hashPath);
                List<String> newDataHash = hashDirectory(output.toFile(), false);
                Files.write(PFMRuntimeResources.createDirIfNeeded(hashPath), newDataHash.toString().replace("[", "").replace("]", "").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

                Files.deleteIfExists(modListPath);
                Files.createFile(modListPath);
                Files.write(PFMRuntimeResources.createDirIfNeeded(modListPath), PaladinFurnitureMod.getVersionMap().toString().replace("[", "").replace("]", "").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                setRunning(false);
            } else {
                log("Data Hash and Mod list matched, skipping generation");
            }
        }
    }
}