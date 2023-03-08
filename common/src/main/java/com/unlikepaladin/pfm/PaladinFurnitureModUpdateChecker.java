package com.unlikepaladin.pfm;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import com.unlikepaladin.pfm.utilities.Version;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*** Source: the Iris Update Checker: <a href="https://github.com/IrisShaders/Iris/blob/trunk/src/main/java/net/coderbot/iris/UpdateChecker.java">...</a> */
public class PaladinFurnitureModUpdateChecker {
    private CompletableFuture<UpdateInfo> info;
        private boolean shouldShowUpdateMessage;

        public PaladinFurnitureModUpdateChecker() {
        }

        @ExpectPlatform
        public static File getUpdateFile() {
            PaladinFurnitureMod.GENERAL_LOGGER.error("[Paladin's Furniture Update Check] Unable to get local update file!");
            return null;
        }

        @ExpectPlatform
        public static URL getUpdateURL() throws MalformedURLException{
            PaladinFurnitureMod.GENERAL_LOGGER.error("[Paladin's Furniture Update Check] Unable to get update URL!");
            return null;
        }

        public void checkForUpdates(PaladinFurnitureModConfig pfmConfig) {
            if (!pfmConfig.shouldCheckForUpdates()) {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("[Paladin's Furniture Update Check] Not checking for updates as updater is disabled.");
                shouldShowUpdateMessage = false;
                return;
            }

            this.info = CompletableFuture.supplyAsync(() -> {
                try {
                    File updateFile = getUpdateFile();
                    if (DateUtils.isSameDay(new Date(), new Date(updateFile.lastModified()))) {
                        PaladinFurnitureMod.GENERAL_LOGGER.warn("[Paladin's Furniture Update Check] Cached update file detected, using that!");
                        UpdateInfo updateInfo;
                        try {
                            updateInfo = new Gson().fromJson(FileUtils.readFileToString(updateFile, StandardCharsets.UTF_8), UpdateInfo.class);
                        } catch (JsonSyntaxException | NullPointerException e) {
                            PaladinFurnitureMod.GENERAL_LOGGER.error("[Paladin's Furniture Update Check] Cached file invalid, will delete!", e);
                            Files.delete(updateFile.toPath());
                            return null;
                        }
                        try {
                            if (Version.getVersion(updateInfo.semanticVersion)) {
                                shouldShowUpdateMessage = true;
                                PaladinFurnitureMod.GENERAL_LOGGER.warn("[Paladin's Furniture Update Check] New update detected, showing update message!");
                                return updateInfo;
                            } else {
                                return null;
                            }
                        } catch (Exception e) {
                            PaladinFurnitureMod.GENERAL_LOGGER.error("[Paladin's Furniture Update Check] Caught a VersionParsingException while parsing semantic versions!", e);
                        }
                    }

                    try (InputStream in = getUpdateURL().openStream()) {
                        String updateIndex;
                        try {
                            updateIndex = new JsonParser().parse(new InputStreamReader(in)).getAsJsonObject().get(getMcVersion()).getAsString();
                        } catch (NullPointerException e) {
                            PaladinFurnitureMod.GENERAL_LOGGER.warn("[Paladin's Furniture Update Check] This version doesn't have an update index, skipping.");
                            return null;
                        }
                        String json = IOUtils.toString(new URL(updateIndex), StandardCharsets.UTF_8);
                        UpdateInfo updateInfo = new Gson().fromJson(json, UpdateInfo.class);
                        BufferedWriter writer = new BufferedWriter(new FileWriter(updateFile));
                        writer.write(json);
                        writer.close();
                        try {
                            if (Version.getVersion(updateInfo.semanticVersion)) {
                                shouldShowUpdateMessage = true;
                                PaladinFurnitureMod.GENERAL_LOGGER.info("[Paladin's Furniture Update Check] New update detected, showing update message!");
                                return updateInfo;
                            } else {
                                PaladinFurnitureMod.GENERAL_LOGGER.info("[Paladin's Furniture Update Check] Is up to date!");
                                return null;
                            }
                        } catch (Exception e) {
                            PaladinFurnitureMod.GENERAL_LOGGER.error("[Paladin's Furniture Update Check] Caught a VersionParsingException while parsing semantic versions!", e);
                        }
                    }
                } catch(FileNotFoundException e) {
                    PaladinFurnitureMod.GENERAL_LOGGER.warn("[Paladin's Furniture Update Check] Unable to download " + e.getMessage());
                } catch (IOException e) {
                    PaladinFurnitureMod.GENERAL_LOGGER.warn("[Paladin's Furniture Update Check] Failed to get update info!", e);
                }
                PaladinFurnitureMod.GENERAL_LOGGER.warn("[Paladin's Furniture Update Check] Returning null!");
                return null;
            });
        }

        public String getUpdateVersion() {
            if (getUpdateInfo() != null) {
                return getUpdateInfo().semanticVersion;
            }
            return "null";
        }

        @Nullable
        public UpdateInfo getUpdateInfo() {
            if (info != null && info.isDone()) {
                try {
                    return info.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            return null;
        }

        @Environment(EnvType.CLIENT)
        public Optional<Text> getUpdateMessage() {
            if (shouldShowUpdateMessage) {
                UpdateInfo info = getUpdateInfo();

                if (info == null) {
                    return Optional.empty();
                }

                String languageCode = MinecraftClient.getInstance().options.language.toLowerCase(Locale.ROOT);
                String originalText = info.updateInfo.containsKey(languageCode) ? info.updateInfo.get(languageCode) : info.updateInfo.get("en_us");
                String[] textParts = originalText.split("\\{link}");
                if (textParts.length > 1) {
                    MutableText component1 = new LiteralText(textParts[0]);
                    MutableText component2 = new LiteralText(textParts[1]);
                    MutableText link = new LiteralText(info.modHost).styled(arg -> arg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, info.modDownload)).withUnderline(true));
                    return Optional.of(component1.append(link).append(component2));
                } else {
                    MutableText link = new LiteralText(info.modHost).styled(arg -> arg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, info.modDownload)).withUnderline(true));
                    return Optional.of(new LiteralText(textParts[0]).append(link));
                }
            } else {
                return Optional.empty();
            }
        }

    public Optional<Text> getUpdateMessageServer() {
        if (shouldShowUpdateMessage) {
            UpdateInfo info = getUpdateInfo();

            if (info == null) {
                return Optional.empty();
            }

            String originalText = info.updateInfo.get("en_us");
            String[] textParts = originalText.split("\\{link}");
            if (textParts.length > 1) {
                MutableText component1 = new LiteralText(textParts[0]);
                MutableText component2 = new LiteralText(textParts[1]);
                MutableText link = new LiteralText(info.modHost).styled(arg -> arg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, info.modDownload)).withUnderline(true));
                return Optional.of(component1.append(link).append(component2));
            } else {
                MutableText link = new LiteralText(info.modHost).styled(arg -> arg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, info.modDownload)).withUnderline(true));
                return Optional.of(new LiteralText(textParts[0]).append(link));
            }
        } else {
            return Optional.empty();
        }
    }


        public Optional<String> getUpdateLink() {
            if (shouldShowUpdateMessage) {
                UpdateInfo info = getUpdateInfo();
                return Optional.of(info.modDownload);
            } else {
                return Optional.empty();
            }
        }

    class UpdateInfo {
        public String semanticVersion;
        public Map<String, String> updateInfo;
        public String modHost;
        public String modDownload;
    }

    /**
     * Gets the current mc version String in a 5 digit format
     *
     * @return mc version string
     * @see <a href="https://github.com/sp614x/optifine/blob/9c6a5b5326558ccc57c6490b66b3be3b2dc8cbef/OptiFineDoc/doc/shaders.txt#L696-L699">Optifine Doc</a>
     */
    public static String getMcVersion() {
        String version = SharedConstants.getGameVersion().getReleaseTarget();
        // release target so snapshots are set to the higher version
        //
        // For example if we were running the mod on 21w07a, getReleaseTarget() would return 1.17

        if (version == null) {
            throw new IllegalStateException("Could not get the current minecraft version!");
        }

        String[] splitVersion = version.split("\\.");

        if (splitVersion.length < 2) {
            throw new IllegalStateException("Could not parse game version \"" + version +  "\"");
        }

        String major = splitVersion[0];
        String minor = splitVersion[1];
        String bugfix;

        if (splitVersion.length < 3) {
            bugfix = "00";
        } else {
            bugfix = splitVersion[2];
        }

        if (minor.length() == 1) {
            minor = 0 + minor;
        }
        if (bugfix.length() == 1) {
            bugfix = 0 + bugfix;
        }

        return major + minor + bugfix;
    }
}

