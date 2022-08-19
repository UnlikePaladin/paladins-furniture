package com.unlikepaladin.pfm.compat;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "pfm")
public class PaladinFurnitureModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean tablesOfDifferentMaterialsConnect = false;

    @ConfigEntry.Gui.Tooltip
    public boolean chairsFacePlayer = true;

    @ConfigEntry.Gui.Tooltip
    public boolean countersOfDifferentMaterialsConnect = false;

    @ConfigEntry.Gui.Tooltip
    public boolean foodPopsOffStove = false;

}
