package com.unlikepaladin.pfm.runtime.assets;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.mixin.PFMLanguageManagerAccessor;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.QuadFunc;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import com.unlikepaladin.pfm.runtime.PFMProvider;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.client.resources.metadata.language.LanguageMetadataSection;
import net.minecraft.data.HashCache;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.locale.Language;
import net.minecraft.util.Tuple;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class PFMLangProvider extends PFMProvider {

    public PFMLangProvider(PFMGenerator parent) {
        super(parent, "PFM Lang");
        parent.setProgress("Generating Language Resources");
    }

    @Override
    public void run() {
        startProviderRun();
        try (SimpleReloadableResourceManager resourceManager = new SimpleReloadableResourceManager(PackType.CLIENT_RESOURCES)) {
            PFMRuntimeResources.RESOURCE_PACK_LIST.forEach(resourceManager::add);
            loadLanguages(resourceManager);
            for (LanguageInfo languageDefinition : languagesToGenerate) {
                language = ClientLanguage.loadFrom(resourceManager, Collections.singletonList(languageDefinition));
                currentLanguageCode = languageDefinition.getCode();
                generate(languageDefinition);
            }
            resourceManager.close();
        }
        catch(IOException e)
        {
            getParent().getLogger().error("Exception while generating: " + e);
            e.printStackTrace();
        }
        endProviderRun();
    }

    public void generate(LanguageInfo languageDefinition) throws IOException {
        translationMap.clear();
        try(BufferedWriter writer = IOUtils.buffer(new FileWriter(new File(PFMRuntimeResources.createDirIfNeeded(getParent().getOrCreateSubDirectory("assets/pfm").resolve("lang")).toFile(), languageDefinition.getCode()+".json"))))  {
            writer.write("{\n");
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMap(), writer, "block.pfm.basic_chair", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.basic_chair", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(DinnerChairBlock.class).getVariantToBlockMap(), writer, "block.pfm.dinner_chair", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(DinnerChairBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.dinner_chair", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicChairBlock.class).getVariantToBlockMap(), writer, "block.pfm.classic_chair", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicChairBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.classic_chair", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernChairBlock.class).getVariantToBlockMap(), writer, "block.pfm.modern_chair", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernChairBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.modern_chair", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicTableBlock.class).getVariantToBlockMap(), writer, "block.pfm.table_basic", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicTableBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.table_basic", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicTableBlock.class).getVariantToBlockMap(), writer, "block.pfm.table_classic", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicTableBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.table_classic", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(LogTableBlock.class).getVariantToBlockMap(), writer, "block.pfm.table_log", this::logTableFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(LogTableBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.table_log", this::logTableFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(RawLogTableBlock.class).getVariantToBlockMap(), writer, "block.pfm.table_log", this::logTableFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(RawLogTableBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.table_log", this::logTableFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(DinnerTableBlock.class).getVariantToBlockMap(), writer, "block.pfm.table_dinner", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(DinnerTableBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.table_dinner", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernDinnerTableBlock.class).getVariantToBlockMap(), writer, "block.pfm.table_modern_dinner", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernDinnerTableBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.table_modern_dinner", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(LogStoolBlock.class).getVariantToBlockMap(), writer, "block.pfm.log_stool", this::logTableFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(LogStoolBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.log_stool", this::logTableFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleStoolBlock.class).getVariantToBlockMap(), writer, "block.pfm.simple_stool", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleStoolBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.simple_stool", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicStoolBlock.class).getVariantToBlockMap(), writer, "block.pfm.classic_stool", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicStoolBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.classic_stool", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernStoolBlock.class).getVariantToBlockMap(), writer, "block.pfm.modern_stool", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernStoolBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.modern_stool", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicNightstandBlock.class).getVariantToBlockMap(), writer, "block.pfm.classic_nightstand", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicNightstandBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.classic_nightstand", this::simpleStrippedFurnitureTranslation);

            generateTranslationForBedMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleBedBlock.class).getVariantToBlockMapList(), writer, "block.pfm.simple_bed", this::bedFurnitureTranslation);
            generateTranslationForBedMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicBedBlock.class).getVariantToBlockMapList(), writer, "block.pfm.classic_bed", this::bedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleBunkLadderBlock.class).getVariantToBlockMap(), writer, "block.pfm.simple_bunk_ladder", this::simpleFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleBunkLadderBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.simple_bunk_ladder", this::simpleFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterBlock.class).getVariantToBlockMap(), writer, "block.pfm.kitchen_counter", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.kitchen_counter", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenDrawerBlock.class).getVariantToBlockMap(), writer, "block.pfm.kitchen_drawer", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenDrawerBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.kitchen_drawer", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCabinetBlock.class).getVariantToBlockMap(), writer, "block.pfm.kitchen_cabinet", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCabinetBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.kitchen_cabinet", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenSinkBlock.class).getVariantToBlockMap(), writer, "block.pfm.kitchen_sink", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenSinkBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.kitchen_sink", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getVariantToBlockMap(), writer, "block.pfm.kitchen_counter_oven", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.kitchen_counter_oven", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallCounterBlock.class).getVariantToBlockMap(), writer, "block.pfm.kitchen_wall_counter", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallCounterBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.kitchen_wall_counter", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getVariantToBlockMap(), writer, "block.pfm.kitchen_wall_drawer", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.kitchen_wall_drawer", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getVariantToBlockMap(), writer, "block.pfm.kitchen_wall_small_drawer", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.kitchen_wall_small_drawer", this::simpleStrippedFurnitureTranslation);

            generateTranslationForLampBlock(writer);
            generateTranslationForOfficeChair(writer);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicCoffeeTableBlock.class).getVariantToBlockMap(), writer, "block.pfm.coffee_table_basic", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicCoffeeTableBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.coffee_table_basic", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernCoffeeTableBlock.class).getVariantToBlockMap(), writer, "block.pfm.coffee_table_modern", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernCoffeeTableBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.coffee_table_modern", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicCoffeeTableBlock.class).getVariantToBlockMap(), writer, "block.pfm.coffee_table_classic", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicCoffeeTableBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.coffee_table_classic", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicDeskBlock.class).getVariantToBlockMap(), writer, "block.pfm.desk_basic", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicDeskBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.desk_basic", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicDeskCabinetBlock.class).getVariantToBlockMap(), writer, "block.pfm.desk_cabinet_basic", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicDeskCabinetBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.desk_cabinet_basic", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicDeskBlock.class).getVariantToBlockMap(), writer, "block.pfm.desk_classic", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicDeskBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.desk_classic", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicDeskCabinetBlock.class).getVariantToBlockMap(), writer, "block.pfm.desk_cabinet_classic", this::simpleStrippedFurnitureTranslation);
            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicDeskCabinetBlock.class).getVariantToBlockMapNonBase(), writer, "block.pfm.desk_cabinet_classic", this::simpleStrippedFurnitureTranslation);

            generateTranslationForVariantBlockMap(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(HerringbonePlankBlock.class).getVariantToBlockMap(), writer, "block.pfm.herringbone_plank", this::simpleStrippedFurnitureTranslation);

            writer.write("    \"pfm.dummy.entry\": \"dummy entry\"\n");
            writer.write("}");
        }
    }
    public String simpleStrippedFurnitureTranslation(Block block, String furnitureKey, String strippedKey, String translatedVariantName) {
        return capitalizeTranslation(translate(furnitureKey, strippedKey, translatedVariantName));
    }

    public String logTableFurnitureTranslation(Block block, String furnitureKey, String strippedKey, String translatedVariantName) {
        String rawFix = block.getDescriptionId().contains("raw") ? translate("block.type.raw") : "";
        String extraLogKey = block.getDescriptionId().contains("stem") ? translate("block.type.stem") : block.getDescriptionId().contains("natural") ? translate("block.type.natural") : translate("block.type.log");
        return capitalizeTranslation(translate(furnitureKey, rawFix + (rawFix.isEmpty() ? "" : " ") + strippedKey, translatedVariantName, extraLogKey));
    }

    public String simpleFurnitureTranslation(Block block, String furnitureKey, String strippedKey, String translatedVariantName) {
        return capitalizeTranslation(translate(furnitureKey, translatedVariantName));
    }

    public String bedFurnitureTranslation(Block block, String furnitureKey, String strippedKey, String translatedVariantName) {
        String color = block instanceof SimpleBedBlock ? translate("color.minecraft."+((SimpleBedBlock) block).getPFMColor().getSerializedName()) : "";
        return capitalizeTranslation(translate(furnitureKey, translatedVariantName, color));
    }

    private boolean isLanguageSupported(String languageCode) {
        boolean supported = false;
        for (PackResources pack : PFMRuntimeResources.RESOURCE_PACK_LIST) {
            try {
                pack.getResource(PackType.CLIENT_RESOURCES, new ResourceLocation(PaladinFurnitureMod.MOD_ID, "lang/" + languageCode + ".json")).close();
                supported = true;
                break;
            } catch (IOException ignored) {
            }
        }
        return supported;
    }

    private Map<String, LanguageInfo> loadAvailableLanguages(Stream<PackResources> packs) {
        HashMap<String, LanguageInfo> map = Maps.newHashMap();
        packs.forEach(pack -> {
            try {
                List<PackResources> subPacks = PFMFileUtil.getSubPacks(pack);
                for (PackResources subPack : subPacks) {
                    LanguageMetadataSection languageResourceMetadata = subPack.getMetadataSection(LanguageMetadataSection.SERIALIZER);
                    if (languageResourceMetadata != null) {
                        for (LanguageInfo languageDefinition : languageResourceMetadata.getLanguages()) {
                            map.putIfAbsent(languageDefinition.getCode(), languageDefinition);
                        }
                    }
                }
            }
            catch (IOException | RuntimeException exception) {
                getParent().getLogger().warn("Unable to parse language metadata section of resourcepack: {}", pack.getName(), exception);
            }
        });
        return ImmutableMap.copyOf(map);
    }

    private volatile Language language = Language.getInstance();
    private String currentLanguageCode = ((PFMLanguageManagerAccessor) Minecraft.getInstance().getLanguageManager()).getCurrentCode();
    private List<LanguageInfo> languagesToGenerate = new ArrayList<>();
    public void loadLanguages(ResourceManager manager) {
        Map<String, LanguageInfo> defs = loadAvailableLanguages(PFMRuntimeResources.RESOURCE_PACK_LIST.stream());
        LanguageInfo enUSDefinition = defs.getOrDefault(LanguageManager.DEFAULT_LANGUAGE, PFMLanguageManagerAccessor.getEnglish_Us());

        LanguageInfo selectedLangDefinition;
        String currentCode = ((PFMLanguageManagerAccessor) Minecraft.getInstance().getLanguageManager()).getCurrentCode();
        // Only generate translations if PFM contains a file for it
        if (defs.containsKey(currentCode) && isLanguageSupported(currentCode)) {
            selectedLangDefinition = defs.get(currentCode);
        } else {
            selectedLangDefinition = enUSDefinition;
        }
        ArrayList<LanguageInfo> list = Lists.newArrayList(enUSDefinition);
        if (selectedLangDefinition != enUSDefinition) {
            list.add(selectedLangDefinition);
        }
        languagesToGenerate = list;
    }


    public String translate(String key, Object ... args) {
        String string = language.getOrDefault(key);
        try {
            return String.format(string, args);
        }
        catch (IllegalFormatException illegalFormatException) {
            getParent().getLogger().error("Format error: " + string + "\n" + illegalFormatException);
            return "Format error: " + string;
        }
    }

    public void generateTranslationForLampBlock(BufferedWriter writer) {
        for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                    break;
                try {
                    String translatedVariantName = getTranslatedVariantName(variant);
                    String translatedColor = translate("color.minecraft."+color.getName());
                    String translatedFurnitureName = capitalizeTranslation(StringUtils.normalizeSpace(translate("block.pfm.basic_lamp", translatedColor, translatedVariantName)));
                    if (translatedFurnitureName.equalsIgnoreCase("block.pfm.basic_lamp"))
                        continue;

                    writer.write(String.format("    \"%1$s\": \"%2$s\",", String.format("block.pfm.basic_%s_%s_lamp", color.getSerializedName(), variant.getSerializedName()), translatedFurnitureName));
                    writer.write("\n");
                } catch (IOException e) {
                    getParent().getLogger().error("Writer exception: " + e);
                    throw new RuntimeException(e);
                }
                i++;
            }
        }
    }

    public void generateTranslationForOfficeChair(BufferedWriter writer) {
        int i = 0;
        for (DyeColor color : DyeColor.values()) {
            if (i > 15)
                break;
            try {
                String translatedColor = translate("color.minecraft."+color.getName());
                String translatedFurnitureName = capitalizeTranslation(StringUtils.normalizeSpace(translate("block.pfm.office_chair", translatedColor)));
                if (translatedFurnitureName.equalsIgnoreCase("block.pfm.office_chair"))
                    continue;

                writer.write(String.format("    \"%1$s\": \"%2$s\",", String.format("block.pfm.%s_office_chair", color.getSerializedName()), translatedFurnitureName));
                writer.write("\n");
            } catch (IOException e) {
                getParent().getLogger().error("Writer exception: " + e);
                throw new RuntimeException(e);
            }
            i++;
        }
    }

    private final HashMap<VariantBase<?>, String> translationMap = new HashMap<>();
    public String getTranslatedVariantName(VariantBase<?> variant) {
        if (translationMap.containsKey(variant))
            return translationMap.get(variant);

        String key = "block.pfm.variant."+variant.getIdentifier().getPath();
        if (!key.equals(translate(key))) {
            translationMap.put(variant, translate(key).toLowerCase(Locale.ROOT));
            return translationMap.get(variant);
        }

        AtomicReference<String> variantName = new AtomicReference<>(translate(variant.getSecondaryBlock().getDescriptionId()));
        String baseBlockName = translate(variant.getBaseBlock().getDescriptionId());
        List<String> common = findCommonWords(variantName.get(), baseBlockName);
        variantName.set("");
        variantName.set(String.join(" ", common));
        translationMap.put(variant, variantName.get().toLowerCase(Locale.ROOT));
        return variantName.get().toLowerCase(Locale.ROOT);
    }

    public void generateTranslationForBedMap(HashMap<VariantBase<?>, ? extends Set<?>> variantBaseHashMap, BufferedWriter writer, String furnitureKey, QuadFunc<Block, String, String, String, String> blockStringStringStringStringQuadFunc) {
        variantBaseHashMap.forEach((variant, list) -> {
            list.forEach(block1 -> {
                Block block = (Block) block1;
                if (variant instanceof WoodVariant) {
                    String translatedVariantName = getTranslatedVariantName(variant);
                    String strippedKey = block.getDescriptionId().contains("stripped") ? translate("block.type.stripped") : "";
                    String translatedFurnitureName = StringUtils.normalizeSpace(blockStringStringStringStringQuadFunc.apply(block, furnitureKey, strippedKey, translatedVariantName));
                    if (translatedFurnitureName.equalsIgnoreCase(furnitureKey))
                        return;

                    try {
                        writer.write(String.format("    \"%1$s\": \"%2$s\",", block.getDescriptionId(), translatedFurnitureName));
                        writer.write("\n");
                    } catch (IOException e) {
                        getParent().getLogger().error("Writer exception: " + e);
                        throw new RuntimeException(e);
                    }
                }
            });
        });

    }

    public void generateTranslationForVariantBlockMap(Map<VariantBase<?>, ? extends Block> variantBaseHashMap, BufferedWriter writer, String furnitureKey, QuadFunc<Block, String, String, String, String> blockStringStringStringStringQuadFunc) {
        variantBaseHashMap.forEach((variant, block) -> {
            if (variant instanceof WoodVariant) {
                String translatedVariantName = getTranslatedVariantName(variant);
                String strippedKey = block.getDescriptionId().contains("stripped") ? translate("block.type.stripped") : "";
                String translatedFurnitureName = StringUtils.normalizeSpace(blockStringStringStringStringQuadFunc.apply(block, furnitureKey, strippedKey, translatedVariantName));
                if (translatedFurnitureName.equalsIgnoreCase(furnitureKey))
                    return;

                try {
                    writer.write(String.format("    \"%1$s\": \"%2$s\",", block.getDescriptionId(), translatedFurnitureName));
                    writer.write("\n");
                } catch (IOException e) {
                    getParent().getLogger().error("Writer exception: " + e);
                    throw new RuntimeException(e);
                }
            } else {
                String translatedVariantName = getTranslatedVariantName(variant);
                String translatedFurnitureName = StringUtils.normalizeSpace(blockStringStringStringStringQuadFunc.apply(block, furnitureKey, "", translatedVariantName));
                if (translatedFurnitureName.equalsIgnoreCase(furnitureKey))
                    return;

                try {
                    writer.write(String.format("    \"%1$s\": \"%2$s\",", block.getDescriptionId(), translatedFurnitureName));
                    writer.write("\n");
                } catch (IOException e) {
                    getParent().getLogger().error("Writer exception: " + e);
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public static List<String> findCommonWords(String input1,String input2) {
        String[] words1 = input1.trim().split("\\s+");
        String[] words2 = input2.trim().split("\\s+");
        List<String>list1 = new ArrayList<>(Arrays.asList(words1));
        List<String>list2 = Arrays.asList(words2);
        list1.retainAll(list2);
        return list1;
    }

    public String capitalizeTranslation(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        // capitalize first letter of the first word for Romance languages
        if (currentLanguageCode.contains("es") || currentLanguageCode.contains("fr") ||
                currentLanguageCode.contains("it") || currentLanguageCode.contains("pt")) {
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        } else {
            // capitalize first letter of each word for other languages such as English and German

            String[] words = input.split("\\s+"); // Split by whitespace
            StringBuilder result = new StringBuilder();

            for (String word : words) {
                if (!word.isEmpty()) {
                    result.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1).toLowerCase())
                            .append(" ");
                }
            }
            return result.toString().trim();
        }
    }

}
