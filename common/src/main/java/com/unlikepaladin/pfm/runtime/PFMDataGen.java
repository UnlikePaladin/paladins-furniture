package com.unlikepaladin.pfm.runtime;

import com.google.common.base.Stopwatch;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.runtime.assets.PFMBlockstateModelProvider;
import com.unlikepaladin.pfm.runtime.assets.PFMLangProvider;
import com.unlikepaladin.pfm.runtime.data.PFMLootTableProvider;
import com.unlikepaladin.pfm.runtime.data.PFMMCMetaProvider;
import com.unlikepaladin.pfm.runtime.data.PFMRecipeProvider;
import com.unlikepaladin.pfm.runtime.data.PFMTagProvider;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PFMDataGen {
    public static final Logger LOGGER = LogManager.getLogger("PFM-DataGen");
    public static boolean frozen = false;
    public static final HashFunction SHA1 = Hashing.sha1();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final Path output;
    private final boolean logOrDebug;
    public static boolean running = false;
    public PFMDataGen(Path output, boolean logOrDebug) {
        this.output = output;
        this.logOrDebug = logOrDebug;
    }
    public void run() throws IOException {
        if (!frozen) {
            log("Packs:");
            for (ResourcePack pack : PFMRuntimeResources.RESOURCE_PACK_LIST) {
                log("\tPack {}", pack.getName());
                for (String namespace : pack.getNamespaces(ResourceType.CLIENT_RESOURCES)) {
                    log("\t\tNamespace {}", namespace);
                }
            }
            running = true;
            frozen = true;
            Path modListPath = PFMRuntimeResources.getPFMDirectory().resolve("modsList");
            Path hashPath = PFMRuntimeResources.getPFMDirectory().resolve("dataHash");
            if (!modListPath.toFile().isFile()) {
                Files.deleteIfExists(modListPath);
                Files.createFile(modListPath);
            }
            if (!hashPath.toFile().isFile()) {
                Files.deleteIfExists(hashPath);
                Files.createFile(hashPath);
            }
            List<String> hashToCompare = hashDirectory(PFMRuntimeResources.getResourceDirectory().toFile(), true);
            List<String> oldHash = Files.readAllLines(hashPath);
            List<String> modList = Files.readAllLines(modListPath);
            if (!hashToCompare.toString().equals(oldHash.toString()) || !modList.toString().replace("[", "").replace("]", "").equals(PaladinFurnitureMod.getVersionMap().toString())) {
                LOGGER.info("Starting PFM Data and Asset Gen, this might take a bit.");
                PFMFileUtil.deleteDir(PFMRuntimeResources.getResourceDirectory().toFile());
                DataCache dataCache = new DataCache(this.output, "cache");
                dataCache.ignore(this.output.resolve("version.json"));
                Stopwatch stopwatch = Stopwatch.createStarted();
                Stopwatch stopwatch2 = Stopwatch.createUnstarted();

                log("Starting provider: {}", "PFM PFMTags");
                stopwatch2.start();
                new PFMTagProvider().run(dataCache);
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM PFMTags", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                stopwatch2.reset();

                log("Starting provider: {}", "PFM Drops");
                stopwatch2.start();
                new PFMLootTableProvider().run(dataCache);
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM Drops", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                stopwatch2.reset();

                log("Starting provider: {}", "PFM Recipes");
                stopwatch2.start();
                new PFMRecipeProvider().run(dataCache);
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM Recipes", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                stopwatch2.reset();

                log("Starting provider: {}", "PFM MC Meta");
                stopwatch2.start();
                new PFMMCMetaProvider().run();
                stopwatch2.stop();
                log("{} finished after {} ms", "PFM MC Meta", stopwatch2.elapsed(TimeUnit.MILLISECONDS));

                if (PaladinFurnitureMod.isClient) {
                    log("Starting provider: {}", "PFM Blockstates and Models");
                    stopwatch2.start();
                    new PFMBlockstateModelProvider().run(dataCache);
                    stopwatch2.stop();
                    log("{} finished after {} ms", "PFM Blockstates and Models", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                    stopwatch2.reset();

                    log("Starting provider: {}", "PFM Lang");
                    stopwatch2.start();
                    new PFMLangProvider().run();
                    stopwatch2.stop();
                    log("{} finished after {} ms", "PFM Lang", stopwatch2.elapsed(TimeUnit.MILLISECONDS));
                    stopwatch2.reset();
                }

                LOGGER.info("All providers took: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

                dataCache.write();

                Files.deleteIfExists(hashPath);
                Files.createFile(hashPath);
                List<String> newDataHash = hashDirectory(PFMRuntimeResources.getResourceDirectory().toFile(), true);
                Files.writeString(PFMRuntimeResources.createDirIfNeeded(hashPath), newDataHash.toString().replace("[", "").replace("]", ""), StandardOpenOption.APPEND);

                Files.deleteIfExists(modListPath);
                Files.createFile(modListPath);
                Files.writeString(PFMRuntimeResources.createDirIfNeeded(modListPath), PaladinFurnitureMod.getVersionMap().toString().replace("[", "").replace("]", ""), StandardOpenOption.APPEND);
                running = false;
            } else {
                log("Data Hash and Mod list matched, skipping generation");
            }
        }
    }

    public void log(String s, Object p0) {
        log(s, p0, "");
    }

    public void log(String s) {
        log(s, "", "");
    }
    
    public void log(String s, Object p0, Object p1) {
        if (this.logOrDebug)
            PFMDataGen.LOGGER.info(s, p0, p1);
        else 
            PFMDataGen.LOGGER.debug(s, p0, p1);
    }

    public static List<String> hashDirectory(File directory, boolean includeHiddenFiles) throws IOException {
        if (!directory.isDirectory()) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Not a directory");
            throw new IllegalArgumentException("Not a directory");
        }
        Vector<String> fileStreams = new Vector<>();
        collectFiles(directory, fileStreams, includeHiddenFiles);
        return fileStreams;
    }

    private static void collectFiles(File directory, List<String> hashList,
                                     boolean includeHiddenFiles) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));

            for (File file : files) {
                if (includeHiddenFiles || !Files.isHidden(file.toPath())) {
                    if (file.isDirectory()) {
                        collectFiles(file, hashList, includeHiddenFiles);
                    } else {
                        FileInputStream stream = new FileInputStream(file);
                        try {
                            HashCode code = HashCode.fromBytes(stream.readAllBytes());
                            hashList.add(code.toString());
                        } catch (Exception e) {
                            LOGGER.warn("File {} was less than 1 byte or invalid, skipping, {}", file.getName(), e);
                        }
                        stream.close();
                    }
                }
            }
        }
    }

}