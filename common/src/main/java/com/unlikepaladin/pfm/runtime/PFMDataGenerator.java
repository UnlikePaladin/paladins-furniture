package com.unlikepaladin.pfm.runtime;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.data.materials.StoneVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.data.PFMLootTableProvider;
import com.unlikepaladin.pfm.runtime.data.PFMMCMetaProvider;
import com.unlikepaladin.pfm.runtime.data.PFMRecipeProvider;
import com.unlikepaladin.pfm.runtime.data.PFMTagProvider;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.minecraft.SharedConstants;
import com.unlikepaladin.pfm.utilities.Version;
import net.minecraft.data.DataProvider;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class PFMDataGenerator extends PFMGenerator {
    public static boolean FROZEN = false;

    public PFMDataGenerator(Path output, boolean logOrDebug) {
        super(output, logOrDebug, LogManager.getLogger("PFM-DataGen"));
    }

    public void run() throws IOException {
        if (!FROZEN) {
            setDataRunning(true);
            log("Packs:");
            for (PackResources pack : PFMRuntimeResources.RESOURCE_PACK_LIST) {
                log("\tPack {}", pack.location().title().getString());
                for (String namespace : pack.getNamespaces(PackType.SERVER_DATA)) {
                    log("\t\tNamespace {}", namespace);
                }
            }
            FROZEN = true;
            Path pfmCacheDataFile = output.resolve("pfmCacheData.json");
            if (!pfmCacheDataFile.toFile().isFile()) {
                Files.deleteIfExists(pfmCacheDataFile);
                Files.createFile(pfmCacheDataFile);
                Files.writeString(pfmCacheDataFile, "{}");
            }
            PFMCache cached = PFMCache.fromJson(JSON_PARSER.parse(Files.readString(pfmCacheDataFile)));
            List<String> hashToCompare = hashDirectory(output.toFile(), false, getLogger());
            List<ResourceLocation> variants = new ArrayList<>();

            WoodVariantRegistry.getVariants().stream().sorted().forEach(woodVariant -> variants.add(woodVariant.identifier));
            StoneVariantRegistry.getVariants().stream().sorted().forEach(stoneVariant -> variants.add(stoneVariant.identifier));
            PFMCache current = new PFMCache(SharedConstants.getCurrentVersion().getName(), Version.getCurrentVersion(), PFMFileUtil.getModLoader(), hashToCompare, variants);

            if (!cached.equals(current)) {
                List<PFMProvider> providers = new ArrayList<>();
                getLogger().info("Starting PFM Data Generation");
                PFMFileUtil.deleteDir(output.toFile());
                Stopwatch stopwatch = Stopwatch.createStarted();

                providers.add(new PFMTagProvider(this));
                providers.add(new PFMLootTableProvider(this));
                providers.add(new PFMRecipeProvider(this));

                PFMMCMetaProvider metaProvider = new PFMMCMetaProvider(this);
                metaProvider.setInfo(new PFMMCMetaProvider.PackInfo(PackType.SERVER_DATA, "PFM-Data"));
                providers.add(metaProvider);
                this.setTotalCount(providers.size());

                if (PaladinFurnitureMod.isClientSide && !PaladinFurnitureMod.getPFMConfig().disableGeneratingScreen())
                    ClientOverlaySetter.setOverlayToPFMOverlay(this);
                boolean allDone = false;

                ExecutorService executor = Executors.newFixedThreadPool(providers.size());
                List<? extends Future<?>> futures = providers.stream()
                        .map(provider -> executor.submit(provider::run))
                        .toList();

                if (PaladinFurnitureMod.isClient)
                    ClientOverlaySetter.setup();
                while (!allDone) {
                    allDone = futures.stream().allMatch(Future::isDone);

                    int completedTasks = (int) futures.stream().filter(Future::isDone).count();
                    this.setCount(completedTasks);
                    if (PaladinFurnitureMod.isClientSide)
                        ClientOverlaySetter.updateScreen();
                }
                executor.shutdown();
                if (PaladinFurnitureMod.isClient)
                    ClientOverlaySetter.finish();

                // Check for errors in providers
                for (Future<?> future : futures) {
                    try {
                        future.get(); // This will throw an exception if the task failed
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupt status
                        error("Provider was interrupted: " + e.getMessage());
                    } catch (ExecutionException e) {
                        error("Provider failed with exception: " + e.getCause());
                        e.getCause().printStackTrace();
                    }
                }

                getLogger().info("Data providers took: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));


                PFMCache.createAndWriteCacheToDisk(output, variants, getLogger());
            } else {
                getLogger().info("Data Hash for Game Data and Variant List matched, skipping generation");
            }
            setDataRunning(false);
        }
    }
}