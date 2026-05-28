package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.registry.Statistics;
import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.core.Registry;

public class StatisticsRegistryFabric {

    public static void registerStatistics(){
        StatisticsRegistry.registerStatistics();
    }
}
