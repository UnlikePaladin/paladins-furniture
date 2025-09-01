package com.unlikepaladin.pfm.utilities.forge;

import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.minecraft.resource.ResourcePack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PFMFileUtilImpl {
    public static Path getGamePath() {
        return FMLPaths.GAMEDIR.relative().normalize();
    }

    public static List<ResourcePack> getSubPacks(ResourcePack pack) {
        return Collections.singletonList(pack);
    }

    public static PFMFileUtil.ModLoader getModLoader() {
        return PFMFileUtil.ModLoader.MINECRAFTFORGE;
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static Optional<String> getVersion(String modId) {
        if (ModList.get().isLoaded(modId)) {
            return Optional.of(ModList.get().getModContainerById(modId).get().getModInfo().getVersion().toString());
        }
        return Optional.empty();
    }
}
