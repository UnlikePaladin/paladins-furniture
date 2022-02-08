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
    public static final Identifier STOVE_OPENED = new Identifier(PaladinFurnitureMod.MOD_ID, "stove_opened");

    public static void register(){
        Registry.register(Registry.CUSTOM_STAT, "fridge_opened", FRIDGE_OPENED);
        Stats.CUSTOM.getOrCreateStat(FRIDGE_OPENED, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "freezer_opened", FREEZER_OPENED);
        Stats.CUSTOM.getOrCreateStat(FREEZER_OPENED, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "drawer_searched", DRAWER_SEARCHED);
        Stats.CUSTOM.getOrCreateStat(DRAWER_SEARCHED, StatFormatter.DEFAULT);

        Registry.register(Registry.CUSTOM_STAT, "stove_opened", STOVE_OPENED);
        Stats.CUSTOM.getOrCreateStat(STOVE_OPENED, StatFormatter.DEFAULT);
    }
}
