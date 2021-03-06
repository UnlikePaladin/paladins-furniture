package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StatisticsRegistry {
    public static final Identifier FRIDGE_OPENED = new Identifier(PaladinFurnitureMod.MOD_ID, "fridge_opened");
    public static final Identifier FREEZER_OPENED = new Identifier(PaladinFurnitureMod.MOD_ID, "freezer_opened");
    public static final Identifier DRAWER_SEARCHED = new Identifier(PaladinFurnitureMod.MOD_ID, "drawer_searched");
    public static final Identifier CABINET_SEARCHED = new Identifier(PaladinFurnitureMod.MOD_ID, "cabinet_searched");
    public static final Identifier STOVE_OPENED = new Identifier(PaladinFurnitureMod.MOD_ID, "stove_opened");
    public static final Identifier STOVETOP_USED = new Identifier(PaladinFurnitureMod.MOD_ID, "stovetop_used");
    public static final Identifier SINK_FILLED = new Identifier(PaladinFurnitureMod.MOD_ID, "sink_filled");
    public static final Identifier USE_SINK = new Identifier(PaladinFurnitureMod.MOD_ID, "use_sink");

    public static void registerStatistics(){
        Registry.register(Registry.CUSTOM_STAT, "fridge_opened", FRIDGE_OPENED);
        Stats.CUSTOM.getOrCreateStat(FRIDGE_OPENED, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "sink_filled", SINK_FILLED);
        Stats.CUSTOM.getOrCreateStat(SINK_FILLED, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "use_sink", USE_SINK);
        Stats.CUSTOM.getOrCreateStat(USE_SINK, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "freezer_opened", FREEZER_OPENED);
        Stats.CUSTOM.getOrCreateStat(FREEZER_OPENED, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "drawer_searched", DRAWER_SEARCHED);
        Stats.CUSTOM.getOrCreateStat(DRAWER_SEARCHED, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "cabinet_searched", CABINET_SEARCHED);
        Stats.CUSTOM.getOrCreateStat(CABINET_SEARCHED, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "stove_opened", STOVE_OPENED);
        Stats.CUSTOM.getOrCreateStat(STOVE_OPENED, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "stovetop_used", STOVETOP_USED);
        Stats.CUSTOM.getOrCreateStat(STOVETOP_USED, StatFormatter.DEFAULT);
    }
}
