package com.unlikepaladin.pfm.config;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.config.option.*;
import net.minecraft.text.TranslatableText;

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
            checkForUpdates = new BooleanConfigOption(new TranslatableText("pfm.option.checkForUpdates"), new TranslatableText("pfm.option.checkForUpdates.tooltip"), MOD_OPTIONS, true, Side.CLIENT),
            shaderSolidFix = new BooleanConfigOption(new TranslatableText("pfm.option.shaderSolidFix"), new TranslatableText("pfm.option.shaderSolidFix.tooltip"), MOD_OPTIONS, false, Side.CLIENT),
            doChairsFacePlayer = new BooleanConfigOption(new TranslatableText("pfm.option.chairsFacePlayer"), new TranslatableText("pfm.option.chairsFacePlayer.tooltip"), GAMEPLAY_OPTIONS, true, Side.SERVER),
            foodPopsOffStove = new BooleanConfigOption(new TranslatableText("pfm.option.foodPopsOffStove"), new TranslatableText("pfm.option.foodPopsOffStove.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            countersOfDifferentMaterialsConnect = new BooleanConfigOption(new TranslatableText("pfm.option.countersOfDifferentMaterialsConnect"), new TranslatableText("pfm.option.countersOfDifferentMaterialsConnect.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            tablesOfDifferentMaterialsConnect = new BooleanConfigOption(new TranslatableText("pfm.option.tablesOfDifferentMaterialsConnect"), new TranslatableText("pfm.option.tablesOfDifferentMaterialsConnect.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            differentMirrorsConnect = new BooleanConfigOption(new TranslatableText("pfm.option.differentMirrorsConnect"), new TranslatableText("pfm.option.differentMirrorsConnect.tooltip"), GAMEPLAY_OPTIONS, false, Side.SERVER),
            enableBook = new BooleanConfigOption(new TranslatableText("pfm.option.enableBook"), new TranslatableText("pfm.option.enableBook.tooltip"), GAMEPLAY_OPTIONS, true, Side.SERVER),
            mobsSitOnChairs = new BooleanConfigOption(new TranslatableText("pfm.option.mobsSitOnChairs"), new TranslatableText("pfm.option.mobsSitOnChairs.tooltip"), GAMEPLAY_OPTIONS, true, Side.SERVER),
            renderImmersivePortalsMirrors = new BooleanConfigOption(new TranslatableText("pfm.option.renderImmersivePortalsMirrors"), new TranslatableText("pfm.option.renderImmersivePortalsMirrors.tooltip"), GAMEPLAY_OPTIONS, true, Side.CLIENT),
            spawnImmersivePortalsMirror  = new BooleanConfigOption(new TranslatableText("pfm.option.spawnImmersivePortalsMirror"), new TranslatableText("pfm.option.spawnImmersivePortalsMirror.tooltip"), GAMEPLAY_OPTIONS, true, Side.SERVER)
       //     variantBlacklist = new StringArrayConfigOption(new TranslatableText("pfm.option.variantBlacklist"), new TranslatableText("pfm.option.variantBlacklist.tooltip"), GAMEPLAY_OPTIONS, new ArrayList<>(), Side.SERVER) alas it is not ready yet
        );
        this.propertiesPath = propertiesPath.resolve("pfm.json");
        this.directoryPath = propertiesPath;
    }

    private void addOptions(AbstractConfigOption<?>... args) {
        ArrayList<AbstractConfigOption> configOptions = new ArrayList<>(Arrays.asList(args));
        configOptions.sort(Comparator.comparing(config1 -> config1.getCategory().substring(22).replace("_options", "")));
        Collections.reverse(configOptions);
        for (AbstractConfigOption configOption : configOptions) {
            options.put(((TranslatableText)configOption.getTitle()).getKey(), configOption);
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
//    private StringArrayConfigOption variantBlacklist;


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
      /*  JsonObject object = getFromJsonElement(config.get("variantBlacklist"), new JsonObject());
        if (object != null && object.isJsonArray()) {
            JsonArray variantBlackListArray = object.getAsJsonArray();

            // Extract strings from the array
            List<String> stringList = new ArrayList<>();
            for (JsonElement arrayElement : variantBlackListArray) {
                stringList.add(arrayElement.getAsString());
            }
            variantBlacklist.addAll(stringList);
        }*/
        for (String key : options.keySet()) {
            if (!config.has(key.replace("pfm.option.", ""))){
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Missing Config Option: " +  key.replace("pfm.option.", "") + ", resetting to default value.");
                options.get(key).setValue(options.get(key).getDefaultValue());
                save();
            }
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
      //  object.add("variantBlacklist", toJsonElement(variantBlacklist.getValue()));
        try (FileWriter writer = new FileWriter(propertiesPath.toString())) {
            GSON.toJson(object, writer);
        }
    }

    public JsonElement toJsonElement(String[] stringArray) {
        JsonArray jsonArray = new JsonArray();
        for (String value : stringArray) {
            jsonArray.add(new JsonPrimitive(value));
        }
        return jsonArray;
    }
}
