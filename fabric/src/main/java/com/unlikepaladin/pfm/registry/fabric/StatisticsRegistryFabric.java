package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.registry.Statistics;
import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.registry.Registry;

public class StatisticsRegistryFabric {

    public static void registerStatistics(){
        StatisticsRegistry.registerStatistics();
    }
}
