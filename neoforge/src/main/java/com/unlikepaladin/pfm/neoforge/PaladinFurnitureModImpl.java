package com.unlikepaladin.pfm.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;


import java.util.*;
import java.util.stream.Collectors;

public class PaladinFurnitureModImpl {
    public static PaladinFurnitureModConfig getPFMConfig() {
        return PaladinFurnitureModNeoForge.pfmConfig;
    }

    public static List<String> getModList() {
        return ModList.get().getMods().stream().map(IModInfo::getModId).filter(s -> !s.contains("generated_")).sorted().collect(Collectors.toList());
    }

    public static PaladinFurnitureMod.Loader getLoader() {
        return PaladinFurnitureMod.Loader.FORGE;
    }

    public static Map<String, String> getVersionMap() {
        Map<String, String> map = new LinkedHashMap<>();
        ModList.get().getMods().stream().sorted(Comparator.comparing(IModInfo::getModId)).filter(modContainer -> !modContainer.getModId().contains("generated_")).forEach(iModInfo -> map.put(iModInfo.getModId(), iModInfo.getVersion().toString()));
        return map;
    }
}
