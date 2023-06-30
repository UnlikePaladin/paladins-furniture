package com.unlikepaladin.pfm.forge;

import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.List;
import java.util.stream.Collectors;

public class PaladinFurnitureModImpl {
    public static PaladinFurnitureModConfig getPFMConfig() {
        return PaladinFurnitureModForge.pfmConfig;
    }

    public static List<String> getModList() {
        return ModList.get().getMods().stream().map(IModInfo::getModId).filter(s -> !s.contains("generated_")).sorted().collect(Collectors.toList());
    }
}
