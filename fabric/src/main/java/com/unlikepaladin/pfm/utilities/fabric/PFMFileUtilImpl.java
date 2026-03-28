package com.unlikepaladin.pfm.utilities.fabric;

import com.unlikepaladin.pfm.mixin.fabric.PFMGroupResourcePackAccessor;
import net.fabricmc.fabric.impl.resource.loader.GroupResourcePack;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.PackResources;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PFMFileUtilImpl {
    public static Path getGamePath() {
        return FabricLoader.getInstance().getGameDir();
    }

    public static List<PackResources> getSubPacks(PackResources pack) {
        List<PackResources> list = new ArrayList<>();
        if (pack instanceof GroupResourcePack groupResourcePack) {
            list.addAll(((PFMGroupResourcePackAccessor)groupResourcePack).getPacks());
        }
        list.add(pack);
        return list;
    }

    public static PFMFileUtil.ModLoader getModLoader() {
        return PFMFileUtil.ModLoader.FABRIC;
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static Optional<String> getVersion(String modId) {
        if (FabricLoader.getInstance().isModLoaded(modId)) {
            return Optional.of(FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getVersion().getFriendlyString());
        }
        return Optional.empty();
    }
}
