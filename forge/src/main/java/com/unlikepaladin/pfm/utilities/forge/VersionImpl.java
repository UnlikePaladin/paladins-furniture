package com.unlikepaladin.pfm.utilities.forge;

import com.unlikepaladin.pfm.utilities.Version;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ComparableVersion;

public class VersionImpl {
    public static boolean getVersion(String targetVersionNum) {
        IModInfo modInfo = ModList.get().getModContainerById("pfm").get().getModInfo();
        ComparableVersion currentVersion = new ComparableVersion(modInfo.getVersion().toString());
        ComparableVersion targetVersion = new ComparableVersion(targetVersionNum);

        return (currentVersion.compareTo(targetVersion) < 0);
    }

    public static String getCurrentVersion() {
        IModInfo modInfo = ModList.get().getModContainerById("pfm").get().getModInfo();
        return modInfo.getVersion().toString();
    }
}
