package com.unlikepaladin.pfm.utilities.neoforge;

import com.unlikepaladin.pfm.utilities.Version;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
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
