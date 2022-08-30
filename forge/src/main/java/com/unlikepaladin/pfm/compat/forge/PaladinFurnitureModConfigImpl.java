package com.unlikepaladin.pfm.compat.forge;

import com.unlikepaladin.pfm.compat.PaladinFurnitureModConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.IConfigSpec;

@Config(name = "pfm")
public class PaladinFurnitureModConfigImpl extends PaladinFurnitureModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public static boolean tablesOfDifferentMaterialsConnect = false;

    public static boolean doTablesOfDifferentMaterialsConnect() {
        return tablesOfDifferentMaterialsConnect;
    }

    @ConfigEntry.Gui.Tooltip
    public static boolean chairsFacePlayer = true;

    public static boolean doChairsFacePlayer() {
        return chairsFacePlayer;
    }

    @ConfigEntry.Gui.Tooltip
    public static boolean countersOfDifferentMaterialsConnect = false;

    public static boolean doCountersOfDifferentMaterialsConnect() {
        return countersOfDifferentMaterialsConnect;
    }


    @ConfigEntry.Gui.Tooltip
    public static boolean foodPopsOffStove = false;

    public static boolean doesFoodPopOffStove() {
        return foodPopsOffStove;
    }

    @ConfigEntry.Gui.Tooltip
    public static boolean checkForUpdates = true;

    public static boolean shouldCheckForUpdates() {
        return checkForUpdates;
    }
}
