package com.unlikepaladin.pfm.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.core.Registry;

public class StatisticsRegistry {
    public static void registerStatistics() {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, "fridge_opened", Statistics.FRIDGE_OPENED);
        Stats.CUSTOM.get(Statistics.FRIDGE_OPENED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "sink_filled", Statistics.SINK_FILLED);
        Stats.CUSTOM.get(Statistics.SINK_FILLED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "use_sink", Statistics.USE_SINK);
        Stats.CUSTOM.get(Statistics.USE_SINK, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "bathtub_filled", Statistics.BATHTUB_FILLED);
        Stats.CUSTOM.get(Statistics.BATHTUB_FILLED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "use_bathtub", Statistics.USE_BATHTUB);
        Stats.CUSTOM.get(Statistics.USE_BATHTUB, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "freezer_opened", Statistics.FREEZER_OPENED);
        Stats.CUSTOM.get(Statistics.FREEZER_OPENED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "drawer_searched", Statistics.DRAWER_SEARCHED);
        Stats.CUSTOM.get(Statistics.DRAWER_SEARCHED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "cabinet_searched", Statistics.CABINET_SEARCHED);
        Stats.CUSTOM.get(Statistics.CABINET_SEARCHED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "stove_opened", Statistics.STOVE_OPENED);
        Stats.CUSTOM.get(Statistics.STOVE_OPENED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "stovetop_used", Statistics.STOVETOP_USED);
        Stats.CUSTOM.get(Statistics.STOVETOP_USED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "plate_used", Statistics.PLATE_USED);
        Stats.CUSTOM.get(Statistics.PLATE_USED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "microwave_used", Statistics.MICROWAVE_USED);
        Stats.CUSTOM.get(Statistics.MICROWAVE_USED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "chair_used", Statistics.CHAIR_USED);
        Stats.CUSTOM.get(Statistics.CHAIR_USED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "toilet_used", Statistics.TOILET_USED);
        Stats.CUSTOM.get(Statistics.TOILET_USED, StatFormatter.DEFAULT);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, "trashcan_opened", Statistics.TRASHCAN_OPENED);
        Stats.CUSTOM.get(Statistics.TRASHCAN_OPENED, StatFormatter.DEFAULT);
    }
}
