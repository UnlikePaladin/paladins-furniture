package com.unlikepaladin.pfm.utilities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.utilities.Version;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ComparableVersion;

import java.util.Optional;

public class VersionImpl {
    public static boolean getVersion(String targetVersionNum) {
        Optional<? extends ModContainer> modInfo = ModList.get().getModContainerById("pfm");
        if (modInfo.isEmpty()) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Couldn't find container for PFM, something is incredibly wrong");
            return false;
        }

        ComparableVersion currentVersion = new ComparableVersion(modInfo.get().getModInfo().getVersion().toString());
        ComparableVersion targetVersion = new ComparableVersion(targetVersionNum);

        return (currentVersion.compareTo(targetVersion) < 0);
    }

    public static String getCurrentVersion() {
        Optional<? extends ModContainer> modInfo = ModList.get().getModContainerById("pfm");
        if (modInfo.isPresent()) {
            return modInfo.get().getModInfo().getVersion().toString();
        }
        PaladinFurnitureMod.GENERAL_LOGGER.error("Couldn't find container for PFM, something is incredibly wrong");
        return "0";
    }

    public static boolean compareVersions(String targetVersionNum, String version2) {
        ComparableVersion targetVersion = new ComparableVersion(targetVersionNum);
        ComparableVersion comparableVersion = new ComparableVersion(version2);
        return (targetVersion.compareTo(comparableVersion) < 0);
    }
}
