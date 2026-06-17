package com.unlikepaladin.pfm.config;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.BooleanConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import com.unlikepaladin.pfm.config.option.DoubleConfigOption;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * A class dedicated to storing the config values of the Mod. Original source: the Iris Config System: <a href="https://github.com/IrisShaders/Iris/blob/trunk/src/main/java/net/coderbot/iris/config/IrisConfig.java">...</a>
 * */
public class PaladinFurnitureModConfig {
    private static final String COMMENT =
            "This file stores configuration options for Paladin's Furniture Mod";
    private final Path propertiesPath;
    private final Path directoryPath;
    public HashMap<String, AbstractConfigOption> options = new LinkedHashMap<>();

    public static final String MOD_OPTIONS = "pfm.config.categories.mod_options";
    public static final String GAMEPLAY_OPTIONS = "pfm.config.categories.gameplay_options";
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public PaladinFurnitureModConfig(Path propertiesPath) {
        this.addOptions(
            checkForUpdates = new BooleanConfigOption(Component.translatable("pfm.option.checkForUpdates"), Component.translatable("pfm.option.checkForUpdates.tooltip"), MOD_OPTIONS, true, Side.CLIENT),
            shaderSolidFix = new BooleanConfigOption(Component.translatable("pfm.option.shaderSolidFix"), Component.translatable("pfm.option.shaderSolidFix.tooltip"), MOD_OPTIONS, false, Side.CLIENT),
            doChairsFacePlayer = new BooleanConfigOption(Component.translatable("pfm.option.chairsFacePlayer"), Component.translatable("pfm.option.chairsFacePlayer.tooltip"), GAMEPLAY_OPTIONS, true, Side.SERVER),
            foodPopsOffStove = new BooleanConfigOption(Component.translatable("pfm.option.foodPopsOffStove"), Component.translatable("pfm.option.foodPopsOffStove.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            countersOfDifferentMaterialsConnect = new BooleanConfigOption(Component.translatable("pfm.option.countersOfDifferentMaterialsConnect"), Component.translatable("pfm.option.countersOfDifferentMaterialsConnect.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            tablesOfDifferentMaterialsConnect = new BooleanConfigOption(Component.translatable("pfm.option.tablesOfDifferentMaterialsConnect"), Component.translatable("pfm.option.tablesOfDifferentMaterialsConnect.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            differentMirrorsConnect = new BooleanConfigOption(Component.translatable("pfm.option.differentMirrorsConnect"), Component.translatable("pfm.option.differentMirrorsConnect.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            enableBook = new BooleanConfigOption(Component.translatable("pfm.option.enableBook"), Component.translatable("pfm.option.enableBook.tooltip"), GAMEPLAY_OPTIONS, true, Side.SERVER),
            mobsSitOnChairs = new BooleanConfigOption(Component.translatable("pfm.option.mobsSitOnChairs"), Component.translatable("pfm.option.mobsSitOnChairs.tooltip"), GAMEPLAY_OPTIONS, true, Side.SERVER),
            renderImmersivePortalsMirrors = new BooleanConfigOption(Component.translatable("pfm.option.renderImmersivePortalsMirrors"), Component.translatable("pfm.option.renderImmersivePortalsMirrors.tooltip"), GAMEPLAY_OPTIONS, true, Side.CLIENT),
            spawnImmersivePortalsMirror  = new BooleanConfigOption(Component.translatable("pfm.option.spawnImmersivePortalsMirror"), Component.translatable("pfm.option.spawnImmersivePortalsMirror.tooltip"), GAMEPLAY_OPTIONS, true, Side.SERVER),
            disableGeneratingScreen  = new BooleanConfigOption(Component.translatable("pfm.option.disableGeneratingScreen"), Component.translatable("pfm.option.disableGeneratingScreen.tooltip"), MOD_OPTIONS, false, Side.CLIENT),
            disableSinytraWarning  = new BooleanConfigOption(Component.translatable("pfm.option.disableSinytraWarning"), Component.translatable("pfm.option.disableSinytraWarning.tooltip"), MOD_OPTIONS, false, Side.CLIENT),
            fuelConsumptionMultiplier = new DoubleConfigOption(Component.translatable("pfm.option.fuelConsumptionMultiplier"), Component.translatable("pfm.option.fuelConsumptionMultiplier.tooltip"), GAMEPLAY_OPTIONS, 1.0D, Side.SERVER),
            ovenSpeedMultiplier = new DoubleConfigOption(Component.translatable("pfm.option.ovenSpeedMultiplier"), Component.translatable("pfm.option.ovenSpeedMultiplier.tooltip"), GAMEPLAY_OPTIONS, 1.0D, Side.SERVER)
        );
        this.propertiesPath = propertiesPath.resolve("pfm.json");
        this.directoryPath = propertiesPath;
    }

    private void addOptions(AbstractConfigOption<?>... args) {
        ArrayList<AbstractConfigOption> configOptions = new ArrayList<>(Arrays.asList(args));
        configOptions.sort(Comparator.comparing(config1 -> config1.getCategory().substring(22).replace("_options", "")));
        Collections.reverse(configOptions);
        for (AbstractConfigOption configOption : configOptions) {
            options.put(((TranslatableContents)configOption.getTitle().getContents()).getKey(), configOption);
        }
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

    public boolean doDifferentMirrorsConnect() {
        return differentMirrorsConnect.getValue();
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

    public boolean shouldGiveGuideBook() {
        return enableBook.getValue();
    }

    public boolean doMobsSitOnChairs() {
        return mobsSitOnChairs.getValue();
    }
    public boolean isShaderSolidFixOn() {
        return shaderSolidFix.getValue();
    }
    public boolean doImmersivePortalsMirrorsRender() {
        return renderImmersivePortalsMirrors.getValue();
    }
    public boolean doImmersivePortalsMirrorsSpawn() {
        return spawnImmersivePortalsMirror.getValue();
    }
    public boolean disableGeneratingScreen() {
        return disableGeneratingScreen.getValue();
    }
    public boolean disableSinytraWarning() {
        return disableSinytraWarning.getValue();
    }

    public double getFuelConsumptionMultiplier() {
        return fuelConsumptionMultiplier.getValue();
    }

    public double getOvenSpeedMultiplier() {
        return ovenSpeedMultiplier.getValue();
    }

    private DoubleConfigOption fuelConsumptionMultiplier;
    private DoubleConfigOption ovenSpeedMultiplier;

    private BooleanConfigOption checkForUpdates;

    private BooleanConfigOption shaderSolidFix;

    private BooleanConfigOption doChairsFacePlayer;

    private BooleanConfigOption countersOfDifferentMaterialsConnect;

    private BooleanConfigOption differentMirrorsConnect;

    private BooleanConfigOption foodPopsOffStove;

    private BooleanConfigOption tablesOfDifferentMaterialsConnect;

    private BooleanConfigOption enableBook;
    private BooleanConfigOption mobsSitOnChairs;
    private BooleanConfigOption renderImmersivePortalsMirrors;
    private BooleanConfigOption spawnImmersivePortalsMirror;
    private BooleanConfigOption disableGeneratingScreen;
    public BooleanConfigOption disableSinytraWarning;


    public Path getPath() {
        return this.propertiesPath;
    }
    /**
     * loads the config file and then populates the string, int, and boolean entries with the parsed entries
     *
     * @throws IOException if the file cannot be loaded
     */
    public void load() throws IOException {
        Path legacyConfig = directoryPath.resolve("pfm.properties");
        if (Files.exists(legacyConfig))
            loadLegacyProperties(legacyConfig);

        if (!Files.exists(propertiesPath)) {
            return;
        }

        JsonObject config = new JsonObject();
        try (FileReader reader = new FileReader(propertiesPath.toString())) {
            JsonElement element = new JsonParser().parse(reader);
            if (element.isJsonObject()) {
                config = element.getAsJsonObject();
            }
        }

        checkForUpdates.setValue(getFromJsonElement(config.get("checkForUpdates"), true));
        shaderSolidFix.setValue(getFromJsonElement(config.get("shaderSolidFix"), false));
        doChairsFacePlayer.setValue(getFromJsonElement(config.get("chairsFacePlayer"), true));
        countersOfDifferentMaterialsConnect.setValue(getFromJsonElement(config.get("countersOfDifferentMaterialsConnect"), false));
        tablesOfDifferentMaterialsConnect.setValue(getFromJsonElement(config.get("tablesOfDifferentMaterialsConnect"), false));
        foodPopsOffStove.setValue(getFromJsonElement(config.get("foodPopsOffStove"), false));
        enableBook.setValue(getFromJsonElement(config.get("enableBook"), false));
        differentMirrorsConnect.setValue(getFromJsonElement(config.get("differentMirrorsConnect"), false));
        mobsSitOnChairs.setValue(getFromJsonElement(config.get("mobsSitOnChairs"), false));
        renderImmersivePortalsMirrors.setValue(getFromJsonElement(config.get("renderImmersivePortalsMirrors"), true));
        spawnImmersivePortalsMirror.setValue(getFromJsonElement(config.get("spawnImmersivePortalsMirror"), true));
        disableGeneratingScreen.setValue(getFromJsonElement(config.get("disableGeneratingScreen"), false));
        disableSinytraWarning.setValue(getFromJsonElement(config.get("disableSinytraWarning"), false));
        boolean needsSave = false;
        if (config.has("fuelConsumptionMultiplier") && config.get("fuelConsumptionMultiplier").isJsonPrimitive()) {
            fuelConsumptionMultiplier.setValue(getFromJsonElement(config.get("fuelConsumptionMultiplier"), 1.0D));
        } else {
            fuelConsumptionMultiplier.setValue(1.0D);
            needsSave = true;
        }
        if (config.has("ovenSpeedMultiplier") && config.get("ovenSpeedMultiplier").isJsonPrimitive()) {
            ovenSpeedMultiplier.setValue(getFromJsonElement(config.get("ovenSpeedMultiplier"), 1.0D));
        } else {
            ovenSpeedMultiplier.setValue(1.0D);
            needsSave = true;
        }
        for (String key : options.keySet()) {
            if (!config.has(key.replace("pfm.option.", ""))){
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Missing Config Option: " +  key.replace("pfm.option.", "") + ", resetting to default value.");
                options.get(key).setValue(options.get(key).getDefaultValue());
                save();
            }
        }
        if (needsSave) {
            save();
        }
    }


    public static <T> T getFromJsonElement(JsonElement element, T defaultValue) {
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            Type targetType;

            if (primitive.isString()) {
                targetType = new TypeToken<T>() {}.getType();
            } else if (primitive.isBoolean()) {
                targetType = new TypeToken<T>() {}.getType();
            } else if (primitive.isNumber()) {
                targetType = new TypeToken<T>() {}.getType();
            } else {
                // Handle the case where the primitive type is not supported
                return null;
            }

            return GSON.fromJson(primitive, targetType);
        }
        return defaultValue;
    }

    public void loadLegacyProperties(Path legacyConfigFile) throws IOException {
        Properties properties = new Properties();
        // NB: This uses ISO-8859-1 with unicode escapes as the encoding
        try (InputStream is = Files.newInputStream(legacyConfigFile)) {
            properties.load(is);
        }

        checkForUpdates.setValue("true".equals(properties.getProperty("checkForUpdates")));
        shaderSolidFix.setValue(!"false".equals(properties.getProperty("shaderSolidFix")));
        doChairsFacePlayer.setValue("true".equals(properties.getProperty("chairsFacePlayer")));
        countersOfDifferentMaterialsConnect.setValue(!"false".equals(properties.getProperty("countersOfDifferentMaterialsConnect")));
        tablesOfDifferentMaterialsConnect.setValue(!"false".equals(properties.getProperty("tablesOfDifferentMaterialsConnect")));
        foodPopsOffStove.setValue(!"false".equals(properties.getProperty("foodPopsOffStove")));
        enableBook.setValue("true".equals(properties.getProperty("enableBook")));
        differentMirrorsConnect.setValue(!"false".equals(properties.getProperty("differentMirrorsConnect")));
        mobsSitOnChairs.setValue("true".equals(properties.getProperty("mobsSitOnChairs")));
        renderImmersivePortalsMirrors.setValue("true".equals(properties.getProperty("renderImmersivePortalsMirrors")));
        spawnImmersivePortalsMirror.setValue("true".equals(properties.getProperty("spawnImmersivePortalsMirror")));
        disableGeneratingScreen.setValue("true".equals(properties.get("disableGeneratingScreen")));
        disableSinytraWarning.setValue("true".equals(properties.get("disableSinytraWarning")));
        fuelConsumptionMultiplier.setValue(1.0D);
        ovenSpeedMultiplier.setValue(1.0D);
        save();
        Files.delete(legacyConfigFile);
        PaladinFurnitureMod.GENERAL_LOGGER.info("Successfully migrated to new config");
    }

    /**
     * Serializes the config into a file. Should be called whenever any config values are modified.
     *
     * @throws IOException file exceptions
     */
    public void save() throws IOException {
        JsonObject object = new JsonObject();
        object.addProperty("checkForUpdates", checkForUpdates.getValue());
        object.addProperty("shaderSolidFix", shaderSolidFix.getValue());
        object.addProperty("chairsFacePlayer", doChairsFacePlayer.getValue());
        object.addProperty("countersOfDifferentMaterialsConnect", countersOfDifferentMaterialsConnect.getValue());
        object.addProperty("foodPopsOffStove", foodPopsOffStove.getValue());
        object.addProperty("tablesOfDifferentMaterialsConnect", tablesOfDifferentMaterialsConnect.getValue());
        object.addProperty("enableBook", enableBook.getValue());
        object.addProperty("differentMirrorsConnect", differentMirrorsConnect.getValue());
        object.addProperty("mobsSitOnChairs", mobsSitOnChairs.getValue());
        object.addProperty("renderImmersivePortalsMirrors", renderImmersivePortalsMirrors.getValue());
        object.addProperty("spawnImmersivePortalsMirror", spawnImmersivePortalsMirror.getValue());
        object.addProperty("disableGeneratingScreen", disableGeneratingScreen.getValue());
        object.addProperty("disableSinytraWarning", disableSinytraWarning.getValue());
        object.addProperty("fuelConsumptionMultiplier", fuelConsumptionMultiplier.getValue());
        object.addProperty("ovenSpeedMultiplier", ovenSpeedMultiplier.getValue());

        try (FileWriter writer = new FileWriter(propertiesPath.toString())) {
            GSON.toJson(object, writer);
        }
    }
}
