package com.unlikepaladin.pfm.config;

import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.BooleanConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import net.minecraft.text.TranslatableText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;


/**
 * A class dedicated to storing the config values of the Mod. Original source: the Iris Config System: <a href="https://github.com/IrisShaders/Iris/blob/trunk/src/main/java/net/coderbot/iris/config/IrisConfig.java">...</a>
 * */
public class PaladinFurnitureModConfig {
    //TODO: Sync Client's Config to Server's.
    private static final String COMMENT =
            "This file stores configuration options for Paladin's Furniture Mod";
    private final Path propertiesPath;
    public HashMap<String, AbstractConfigOption> options = new HashMap<>();

    public static final String MOD_OPTIONS = "pfm.config.categories.mod_options";
    public static final String GAMEPLAY_OPTIONS = "pfm.config.categories.gameplay_options";

    //TODO: Get the right size for Config Objects
    //TODO: Actually sync the config when user is on a server
    //TODO: Gray out Buttons with tooltip
    //TODO: Add tooltips to options
    //TODO: Add syncing on Fabric
    public PaladinFurnitureModConfig(Path propertiesPath) {
        this.addOptions(
            checkForUpdates = new BooleanConfigOption(new TranslatableText("pfm.option.checkForUpdates"), new TranslatableText("pfm.option.checkForUpdates.tooltip"), MOD_OPTIONS, true, Side.CLIENT),
            doChairsFacePlayer = new BooleanConfigOption(new TranslatableText("pfm.option.chairsFacePlayer"), new TranslatableText("pfm.option.chairsFacePlayer.tooltip"), GAMEPLAY_OPTIONS, true, Side.SERVER),
            foodPopsOffStove = new BooleanConfigOption(new TranslatableText("pfm.option.foodPopsOffStove"), new TranslatableText("pfm.option.foodPopsOffStove.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            countersOfDifferentMaterialsConnect = new BooleanConfigOption(new TranslatableText("pfm.option.countersOfDifferentMaterialsConnect"), new TranslatableText("pfm.option.countersOfDifferentMaterialsConnect.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            tablesOfDifferentMaterialsConnect = new BooleanConfigOption(new TranslatableText("pfm.option.tablesOfDifferentMaterialsConnect"), new TranslatableText("pfm.option.tablesOfDifferentMaterialsConnect.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER)
        );
        this.propertiesPath = propertiesPath;
    }

    private void addOptions(AbstractConfigOption<?>... args) {
        Arrays.stream(args).forEach(abstractConfigOption -> {
            options.put(((TranslatableText)abstractConfigOption.getTitle()).getKey(), abstractConfigOption);
        });
    }

    /**
     * Initializes the configuration, loading it if it is present and creating a default config otherwise.
     *
     * @throws IOException file exceptions
     */
    public void initialize() throws IOException {
        load();
        if (!Files.exists(propertiesPath)) {
            save();
        }
    }

    public boolean doTablesOfDifferentMaterialsConnect() {
       return tablesOfDifferentMaterialsConnect.getValue();
    }

    public boolean doChairsFacePlayer() {
        return doChairsFacePlayer.getValue();
    }

    public boolean doCountersOfDifferentMaterialsConnect() {
        return countersOfDifferentMaterialsConnect.getValue();
    }

    public boolean doesFoodPopOffStove() {
        return foodPopsOffStove.getValue();
    }

    public boolean shouldCheckForUpdates() {
        return checkForUpdates.getValue();
    }

    private BooleanConfigOption checkForUpdates;

    private BooleanConfigOption doChairsFacePlayer;

    private BooleanConfigOption countersOfDifferentMaterialsConnect;

    private BooleanConfigOption foodPopsOffStove;

    private BooleanConfigOption tablesOfDifferentMaterialsConnect;

    public void setCheckForUpdates(boolean setUpdates) {
        checkForUpdates.setValue(setUpdates);
    }

    public void setDoChairsFacePlayer(boolean chairsFacePlayer) {
        doChairsFacePlayer.setValue(chairsFacePlayer);
    }

    public void setFoodPopsOffStove(boolean setPop) {
        foodPopsOffStove.setValue(setPop);
    }

    public void setTablessOfDifferentMaterialsConnect(boolean differentMaterialsConnect) {
        tablesOfDifferentMaterialsConnect.setValue(differentMaterialsConnect);
    }

    public void setCountersOfDifferentMaterialsConnect(boolean differentMaterialsConnect) {
        countersOfDifferentMaterialsConnect.setValue(differentMaterialsConnect);
    }

    public Path getPath() {
        return this.propertiesPath;
    }
    /**
     * loads the config file and then populates the string, int, and boolean entries with the parsed entries
     *
     * @throws IOException if the file cannot be loaded
     */

    public void load() throws IOException {
        if (!Files.exists(propertiesPath)) {
            return;
        }

        Properties properties = new Properties();
        // NB: This uses ISO-8859-1 with unicode escapes as the encoding
        try (InputStream is = Files.newInputStream(propertiesPath)) {
            properties.load(is);
        }
        checkForUpdates.setValue( "true".equals(properties.getProperty("checkForUpdates")));
        doChairsFacePlayer.setValue("true".equals(properties.getProperty("doChairsFacePlayer")));
        countersOfDifferentMaterialsConnect.setValue(!"false".equals(properties.getProperty("countersOfDifferentMaterialsConnect")));
        tablesOfDifferentMaterialsConnect.setValue(!"false".equals(properties.getProperty("tablesOfDifferentMaterialsConnect")));
        foodPopsOffStove.setValue(!"false".equals(properties.getProperty("foodPopsOffStove")));
    }

    /**
     * Serializes the config into a file. Should be called whenever any config values are modified.
     *
     * @throws IOException file exceptions
     */
    public void save() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("checkForUpdates", checkForUpdates.getValue() ? "true" : "false");
        properties.setProperty("doChairsFacePlayer", doChairsFacePlayer.getValue() ? "true" : "false");
        properties.setProperty("countersOfDifferentMaterialsConnect", countersOfDifferentMaterialsConnect.getValue() ? "true" : "false");
        properties.setProperty("foodPopsOffStove", foodPopsOffStove.getValue() ? "true" : "false");
        properties.setProperty("tablesOfDifferentMaterialsConnect",tablesOfDifferentMaterialsConnect.getValue() ? "true" : "false");
        // NB: This uses ISO-8859-1 with unicode escapes as the encoding
        try (OutputStream os = Files.newOutputStream(propertiesPath)) {
            properties.store(os, COMMENT);
        }
    }
}
