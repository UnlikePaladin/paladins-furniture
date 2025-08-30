package com.unlikepaladin.pfm.utilities.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;

import java.util.Objects;
import java.util.Optional;

public class VersionImpl {
    public static boolean getVersion(String targetVersionNum) {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("pfm");
        if (modContainer.isPresent()) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Couldn't find container for PFM, something is incredibly wrong");
            return false;
        }
        SemanticVersion currentVersion;
        SemanticVersion targetVersion;
        try {
           currentVersion = SemanticVersion.parse(modContainer.get().getMetadata().getVersion().getFriendlyString());
           targetVersion = SemanticVersion.parse(targetVersionNum);
        } catch (VersionParsingException exception) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("[Paladin's Furniture Update Check] Caught a VersionParsingException while parsing semantic versions!", exception);
            return false;
        }
        return (currentVersion.compareTo(targetVersion) < 0);
    }

    public static String getCurrentVersion() {
        if (FabricLoader.getInstance().getModContainer("pfm").isEmpty()) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Couldn't find container for PFM, something is incredibly wrong");
            return "0";
        }
        return FabricLoader.getInstance().getModContainer("pfm").get().getMetadata().getVersion().getFriendlyString();
    }

    public static boolean compareVersions(String targetVersionNum, String version2) {
        SemanticVersion currentVersion;
        SemanticVersion targetVersion;
        try {
            currentVersion = SemanticVersion.parse(FabricLoader.getInstance().getModContainer("pfm").get().getMetadata().getVersion().getFriendlyString());
            targetVersion = SemanticVersion.parse(targetVersionNum);
        } catch (VersionParsingException exception) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Caught a VersionParsingException while parsing semantic versions!", exception);
            return false;
        }
        return (currentVersion.compareTo(targetVersion) < 0);
    }
}
