package com.unlikepaladin.pfm.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.*;
import java.util.stream.Collectors;

public class PaladinFurnitureModImpl {
    public static PaladinFurnitureModConfig getPFMConfig() {
        return PaladinFurnitureModFabric.pfmConfig;
    }

    public static List<String> getModList() {
        return FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).map(ModMetadata::getId).filter(s -> !s.contains("generated_")).sorted().collect(Collectors.toList());
    }

    public static PaladinFurnitureMod.Loader getLoader() {
        return PaladinFurnitureMod.Loader.FABRIC_LIKE;
    }

    public static Map<String, String> getVersionMap() {
        Map<String, String> map = new LinkedHashMap<>();
        FabricLoader.getInstance().getAllMods().stream().sorted(Comparator.comparing(modContainer -> modContainer.getMetadata().getId())).filter(modContainer -> !modContainer.getMetadata().getId().contains("generated_")).forEach(modContainer -> map.put(modContainer.getMetadata().getId(), modContainer.getMetadata().getVersion().getFriendlyString()));
        return map;
    }
}
