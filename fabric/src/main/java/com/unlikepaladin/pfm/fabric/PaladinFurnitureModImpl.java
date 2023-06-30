package com.unlikepaladin.pfm.fabric;

import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaladinFurnitureModImpl {
    public static PaladinFurnitureModConfig getPFMConfig() {
        return PaladinFurnitureModFabric.pfmConfig;
    }

    public static List<String> getModList() {
        return FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).map(ModMetadata::getId).filter(s -> !s.contains("generated_")).sorted().collect(Collectors.toList());
    }
}
