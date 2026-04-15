package com.unlikepaladin.pfm.utilities.forge;

import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PFMFileUtilImpl {
    public static Path getGamePath() {
        return FMLPaths.GAMEDIR.relative().normalize();
    }

    public static List<PackResources> getSubPacks(PackResources pack) {
        return Collections.singletonList(pack);
    }

    public static PFMFileUtil.ModLoader getModLoader() {
        return PFMFileUtil.ModLoader.MINECRAFTFORGE;
    }

    public static MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
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
