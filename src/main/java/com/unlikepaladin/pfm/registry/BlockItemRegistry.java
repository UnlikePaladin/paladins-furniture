package com.unlikepaladin.pfm.registry;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.items.DyeKit;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class BlockItemRegistry {
    public static final Block OAK_CHAIR = new BasicChair(FabricBlockSettings.of(Material.WOOD).strength(2.0f).resistance(2.0f).nonOpaque().requiresTool());
    public static final Block BIRCH_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CHAIR = new BasicChair(FabricBlockSettings.of(Material.NETHER_WOOD).strength(2.0f).resistance(2.0f).nonOpaque().requiresTool());
    public static final Block WARPED_CHAIR = new BasicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block QUARTZ_CHAIR = new BasicChair(FabricBlockSettings.of(Material.STONE).strength(2.0f).resistance(2.0f).nonOpaque().requiresTool());
    public static final Block NETHERITE_CHAIR = new BasicChair(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool());

    public static final Block PLAYER_CHAIR = new PlayerChair(FabricBlockSettings.of(Material.ORGANIC_PRODUCT).strength(20.0f).resistance(100.0f).nonOpaque().requiresTool());



    //Dinner Chairs
    public static final Block OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));


    //Froggy Chairs
    public static final Block FROGGY_CHAIR = new FroggyChair(FabricBlockSettings.of(Material.METAL).strength(9.0f).resistance(8.0f).nonOpaque().requiresTool());
    public static final Block FROGGY_CHAIR_PINK = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));
    public static final Block FROGGY_CHAIR_BLUE = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));
    public static final Block FROGGY_CHAIR_LIGHT_BLUE = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));
    public static final Block FROGGY_CHAIR_ORANGE = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));
    public static final Block FROGGY_CHAIR_YELLOW = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));

    //Classic Chair
    public static final Block OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block CHAIR_CLASSIC_WOOL = new ClassicChairDyeable(FabricBlockSettings.copyOf(OAK_CHAIR));

    //Modern Chair
    public static final Block OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    //Arm Chairs
    public static final Block ARM_CHAIR_STANDARD = new ArmChairDyeable(FabricBlockSettings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque());
    public static final Block ARM_CHAIR_LEATHER = new ArmChair(FabricBlockSettings.of(Material.ORGANIC_PRODUCT).strength(2.0f).resistance(2.0f).nonOpaque());




    //tables
    public static final Block OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));


    public static final Block OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));


    public static final Block OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block OAK_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_STEM_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block WARPED_STEM_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block WARPED_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_WARPED_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block OAK_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));


    public static final Item DYE_KIT_YELLOW = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.YELLOW);
    public static final Item DYE_KIT_BLUE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.BLUE);
    public static final Item DYE_KIT_WHITE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.WHITE);
    public static final Item DYE_KIT_PINK = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.PINK);
    public static final Item DYE_KIT_PURPLE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.PURPLE);
    public static final Item DYE_KIT_GREEN = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.GREEN);
    public static final Item DYE_KIT_LIGHT_BLUE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.LIGHT_BLUE);
    public static final Item DYE_KIT_LIGHT_GRAY = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.LIGHT_GRAY);
    public static final Item DYE_KIT_LIME = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.LIME);
    public static final Item DYE_KIT_ORANGE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.ORANGE);
    public static final Item DYE_KIT_BLACK = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.BLACK);
    public static final Item DYE_KIT_BROWN = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.BROWN);
    public static final Item DYE_KIT_MAGENTA = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.MAGENTA);
    public static final Item DYE_KIT_RED = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.RED);
    public static final Item DYE_KIT_CYAN = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.CYAN);
    public static final Item DYE_KIT_GRAY = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.GRAY);



    public static void registerFurniture(String blockName, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, blockName),  block);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, blockName), new BlockItem(block, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP)));
    }

    public static void registerItem(String itemName, Item item) {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, itemName), item);
    }

    public static void register(){
        //Block Registry
        //Basic Chairs
        registerFurniture("oak_chair", OAK_CHAIR);
        registerFurniture("birch_chair", BIRCH_CHAIR);
        registerFurniture("spruce_chair", SPRUCE_CHAIR);
        registerFurniture("acacia_chair", ACACIA_CHAIR);
        registerFurniture("jungle_chair", JUNGLE_CHAIR);
        registerFurniture("dark_oak_chair", DARK_OAK_CHAIR);
        registerFurniture("warped_chair", WARPED_CHAIR);
        registerFurniture("crimson_chair", CRIMSON_CHAIR);
        registerFurniture("stripped_oak_chair", STRIPPED_OAK_CHAIR);
        registerFurniture("stripped_birch_chair", STRIPPED_BIRCH_CHAIR);
        registerFurniture("stripped_spruce_chair", STRIPPED_SPRUCE_CHAIR);
        registerFurniture("stripped_acacia_chair", STRIPPED_ACACIA_CHAIR);
        registerFurniture("stripped_jungle_chair", STRIPPED_JUNGLE_CHAIR);
        registerFurniture("stripped_dark_oak_chair", STRIPPED_DARK_OAK_CHAIR);
        registerFurniture("stripped_warped_chair", STRIPPED_WARPED_CHAIR);
        registerFurniture("stripped_crimson_chair", STRIPPED_CRIMSON_CHAIR);
        registerFurniture("quartz_chair", QUARTZ_CHAIR);
        registerFurniture("netherite_chair", NETHERITE_CHAIR);


        //much concern
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "player_chair"), PLAYER_CHAIR);

        //Dinner Chairs
        registerFurniture("oak_chair_dinner", OAK_CHAIR_DINNER);
        registerFurniture("birch_chair_dinner", BIRCH_CHAIR_DINNER);
        registerFurniture("spruce_chair_dinner", SPRUCE_CHAIR_DINNER);
        registerFurniture("acacia_chair_dinner", ACACIA_CHAIR_DINNER);
        registerFurniture("jungle_chair_dinner", JUNGLE_CHAIR_DINNER);
        registerFurniture("dark_oak_chair_dinner", DARK_OAK_CHAIR_DINNER);
        registerFurniture("warped_chair_dinner", WARPED_CHAIR_DINNER);
        registerFurniture("crimson_chair_dinner", CRIMSON_CHAIR_DINNER);
        registerFurniture("stripped_oak_chair_dinner", STRIPPED_OAK_CHAIR_DINNER);
        registerFurniture("stripped_birch_chair_dinner", STRIPPED_BIRCH_CHAIR_DINNER);
        registerFurniture("stripped_spruce_chair_dinner", STRIPPED_SPRUCE_CHAIR_DINNER);
        registerFurniture("stripped_acacia_chair_dinner", STRIPPED_ACACIA_CHAIR_DINNER);
        registerFurniture("stripped_jungle_chair_dinner", STRIPPED_JUNGLE_CHAIR_DINNER);
        registerFurniture("stripped_dark_oak_chair_dinner", STRIPPED_DARK_OAK_CHAIR_DINNER);
        registerFurniture("stripped_warped_chair_dinner", STRIPPED_WARPED_CHAIR_DINNER);
        registerFurniture("stripped_crimson_chair_dinner", STRIPPED_CRIMSON_CHAIR_DINNER);


        //Froggy Chairs
        registerFurniture("froggy_chair", FROGGY_CHAIR);
        registerFurniture("froggy_chair_pink", FROGGY_CHAIR_PINK);
        registerFurniture("froggy_chair_light_blue", FROGGY_CHAIR_LIGHT_BLUE);
        registerFurniture("froggy_chair_blue", FROGGY_CHAIR_BLUE);
        registerFurniture("froggy_chair_orange",FROGGY_CHAIR_ORANGE);
        registerFurniture("froggy_chair_yellow", FROGGY_CHAIR_YELLOW);

        //Classic Chairs
        registerFurniture("chair_classic_wool", CHAIR_CLASSIC_WOOL);
        registerFurniture("oak_chair_classic", OAK_CHAIR_CLASSIC);
        registerFurniture("birch_chair_classic", BIRCH_CHAIR_CLASSIC);
        registerFurniture("spruce_chair_classic", SPRUCE_CHAIR_CLASSIC);
        registerFurniture("acacia_chair_classic", ACACIA_CHAIR_CLASSIC);
        registerFurniture("jungle_chair_classic", JUNGLE_CHAIR_CLASSIC);
        registerFurniture("dark_oak_chair_classic", DARK_OAK_CHAIR_CLASSIC);
        registerFurniture("warped_chair_classic", WARPED_CHAIR_CLASSIC);
        registerFurniture("crimson_chair_classic", CRIMSON_CHAIR_CLASSIC);
        registerFurniture("stripped_oak_chair_classic", STRIPPED_OAK_CHAIR_CLASSIC);
        registerFurniture("stripped_birch_chair_classic", STRIPPED_BIRCH_CHAIR_CLASSIC);
        registerFurniture("stripped_spruce_chair_classic", STRIPPED_SPRUCE_CHAIR_CLASSIC);
        registerFurniture("stripped_acacia_chair_classic", STRIPPED_ACACIA_CHAIR_CLASSIC);
        registerFurniture("stripped_jungle_chair_classic", STRIPPED_JUNGLE_CHAIR_CLASSIC);
        registerFurniture("stripped_dark_oak_chair_classic", STRIPPED_DARK_OAK_CHAIR_CLASSIC);
        registerFurniture("stripped_warped_chair_classic", STRIPPED_WARPED_CHAIR_CLASSIC);
        registerFurniture("stripped_crimson_chair_classic", STRIPPED_CRIMSON_CHAIR_CLASSIC);

        //Modern Chair
        registerFurniture("oak_chair_modern", OAK_CHAIR_MODERN);
        registerFurniture("birch_chair_modern", BIRCH_CHAIR_MODERN);
        registerFurniture("spruce_chair_modern", SPRUCE_CHAIR_MODERN);
        registerFurniture("acacia_chair_modern", ACACIA_CHAIR_MODERN);
        registerFurniture("jungle_chair_modern", JUNGLE_CHAIR_MODERN);
        registerFurniture("dark_oak_chair_modern",DARK_OAK_CHAIR_MODERN );
        registerFurniture("warped_chair_modern", WARPED_CHAIR_MODERN);
        registerFurniture("crimson_chair_modern", CRIMSON_CHAIR_MODERN);
        registerFurniture("stripped_oak_chair_modern", STRIPPED_OAK_CHAIR_MODERN);
        registerFurniture("stripped_birch_chair_modern", STRIPPED_BIRCH_CHAIR_MODERN);
        registerFurniture("stripped_spruce_chair_modern", STRIPPED_SPRUCE_CHAIR_MODERN);
        registerFurniture("stripped_acacia_chair_modern", STRIPPED_ACACIA_CHAIR_MODERN);
        registerFurniture("stripped_jungle_chair_modern", STRIPPED_JUNGLE_CHAIR_MODERN);
        registerFurniture("stripped_dark_oak_chair_modern",STRIPPED_DARK_OAK_CHAIR_MODERN);
        registerFurniture("stripped_warped_chair_modern", STRIPPED_WARPED_CHAIR_MODERN);
        registerFurniture("stripped_crimson_chair_modern", STRIPPED_CRIMSON_CHAIR_MODERN);

        //Armchairs
        registerFurniture("arm_chair_standard", ARM_CHAIR_STANDARD);
        registerFurniture("arm_chair_leather", ARM_CHAIR_LEATHER);

        //Tables
        registerFurniture("oak_table_basic", OAK_BASIC_TABLE);
        registerFurniture("birch_table_basic", BIRCH_BASIC_TABLE);
        registerFurniture("spruce_table_basic", SPRUCE_BASIC_TABLE);
        registerFurniture("acacia_table_basic", ACACIA_BASIC_TABLE);
        registerFurniture("jungle_table_basic", JUNGLE_BASIC_TABLE);
        registerFurniture("dark_oak_table_basic",DARK_OAK_BASIC_TABLE);
        registerFurniture("crimson_table_basic", CRIMSON_BASIC_TABLE);
        registerFurniture("warped_table_basic", WARPED_BASIC_TABLE);
        registerFurniture("stripped_oak_table_basic", STRIPPED_OAK_BASIC_TABLE);
        registerFurniture("stripped_birch_table_basic", STRIPPED_BIRCH_BASIC_TABLE);
        registerFurniture("stripped_spruce_table_basic", STRIPPED_SPRUCE_BASIC_TABLE);
        registerFurniture("stripped_acacia_table_basic", STRIPPED_ACACIA_BASIC_TABLE);
        registerFurniture("stripped_jungle_table_basic", STRIPPED_JUNGLE_BASIC_TABLE);
        registerFurniture("stripped_dark_oak_table_basic", STRIPPED_DARK_OAK_BASIC_TABLE);
        registerFurniture("stripped_crimson_table_basic", STRIPPED_CRIMSON_BASIC_TABLE);
        registerFurniture("stripped_warped_table_basic", STRIPPED_WARPED_BASIC_TABLE);

        //Classic Table
        registerFurniture("oak_table_classic", OAK_CLASSIC_TABLE);
        registerFurniture("birch_table_classic", BIRCH_CLASSIC_TABLE);
        registerFurniture("spruce_table_classic", SPRUCE_CLASSIC_TABLE);
        registerFurniture("acacia_table_classic", ACACIA_CLASSIC_TABLE);
        registerFurniture("jungle_table_classic", JUNGLE_CLASSIC_TABLE);
        registerFurniture("dark_oak_table_classic", DARK_OAK_CLASSIC_TABLE);
        registerFurniture("crimson_table_classic", CRIMSON_CLASSIC_TABLE);
        registerFurniture("warped_table_classic", WARPED_CLASSIC_TABLE);
        registerFurniture("stripped_oak_table_classic", STRIPPED_OAK_CLASSIC_TABLE);
        registerFurniture("stripped_birch_table_classic", STRIPPED_BIRCH_CLASSIC_TABLE);
        registerFurniture("stripped_spruce_table_classic", STRIPPED_SPRUCE_CLASSIC_TABLE);
        registerFurniture("stripped_acacia_table_classic", STRIPPED_ACACIA_CLASSIC_TABLE);
        registerFurniture("stripped_jungle_table_classic", STRIPPED_JUNGLE_CLASSIC_TABLE);
        registerFurniture("stripped_dark_oak_table_classic", STRIPPED_DARK_OAK_CLASSIC_TABLE);
        registerFurniture("stripped_crimson_table_classic", STRIPPED_CRIMSON_CLASSIC_TABLE);
        registerFurniture("stripped_warped_table_classic", STRIPPED_WARPED_CLASSIC_TABLE);

        //Log Table
        registerFurniture("oak_table_log", OAK_LOG_TABLE);
        registerFurniture("birch_table_log", BIRCH_LOG_TABLE);
        registerFurniture("spruce_table_log", SPRUCE_LOG_TABLE);
        registerFurniture("acacia_table_log", ACACIA_LOG_TABLE);
        registerFurniture("jungle_table_log", JUNGLE_LOG_TABLE);
        registerFurniture("dark_oak_table_log", DARK_OAK_LOG_TABLE);
        registerFurniture("crimson_table_log", CRIMSON_LOG_TABLE);
        registerFurniture("warped_table_log", WARPED_LOG_TABLE);
        registerFurniture("stripped_oak_table_log", STRIPPED_OAK_LOG_TABLE);
        registerFurniture("stripped_birch_table_log", STRIPPED_BIRCH_LOG_TABLE);
        registerFurniture("stripped_spruce_table_log", STRIPPED_SPRUCE_LOG_TABLE);
        registerFurniture("stripped_acacia_table_log", STRIPPED_ACACIA_LOG_TABLE);
        registerFurniture("stripped_jungle_table_log", STRIPPED_JUNGLE_LOG_TABLE);
        registerFurniture("stripped_dark_oak_table_log", STRIPPED_DARK_OAK_LOG_TABLE);
        registerFurniture("stripped_crimson_table_log", STRIPPED_CRIMSON_LOG_TABLE);
        registerFurniture("stripped_warped_table_log", STRIPPED_WARPED_LOG_TABLE);

        //Raw Log Table
        registerFurniture("oak_raw_table_log", OAK_RAW_LOG_TABLE);
        registerFurniture("birch_raw_table_log", BIRCH_RAW_LOG_TABLE);
        registerFurniture("acacia_raw_table_log", ACACIA_RAW_LOG_TABLE);
        registerFurniture("spruce_raw_table_log", SPRUCE_RAW_LOG_TABLE);
        registerFurniture("jungle_raw_table_log", JUNGLE_RAW_LOG_TABLE);
        registerFurniture("dark_oak_raw_table_log", DARK_OAK_RAW_LOG_TABLE);
        registerFurniture("warped_raw_table_stem", WARPED_RAW_STEM_TABLE);
        registerFurniture("crimson_raw_table_stem", CRIMSON_RAW_STEM_TABLE);
        registerFurniture("stripped_oak_raw_table_log", STRIPPED_OAK_RAW_LOG_TABLE);
        registerFurniture("stripped_birch_raw_table_log", STRIPPED_BIRCH_RAW_LOG_TABLE);
        registerFurniture("stripped_acacia_raw_table_log", STRIPPED_ACACIA_RAW_LOG_TABLE);
        registerFurniture("stripped_spruce_raw_table_log", STRIPPED_SPRUCE_RAW_LOG_TABLE);
        registerFurniture("stripped_jungle_raw_table_log", STRIPPED_JUNGLE_RAW_LOG_TABLE);
        registerFurniture("stripped_dark_oak_raw_table_log", STRIPPED_DARK_OAK_RAW_LOG_TABLE);
        registerFurniture("stripped_warped_raw_table_stem", STRIPPED_WARPED_RAW_STEM_TABLE);
        registerFurniture("stripped_crimson_raw_table_stem", STRIPPED_CRIMSON_RAW_STEM_TABLE);

        registerFurniture("oak_table_dinner", OAK_DINNER_TABLE);
        registerFurniture("birch_table_dinner", BIRCH_DINNER_TABLE);
        registerFurniture("spruce_table_dinner", SPRUCE_DINNER_TABLE);
        registerFurniture("acacia_table_dinner", ACACIA_DINNER_TABLE);
        registerFurniture("jungle_table_dinner", JUNGLE_DINNER_TABLE);
        registerFurniture("dark_oak_table_dinner", DARK_OAK_DINNER_TABLE);
        registerFurniture("crimson_table_dinner", CRIMSON_DINNER_TABLE);
        registerFurniture("warped_table_dinner", WARPED_DINNER_TABLE);
        registerFurniture("stripped_oak_table_dinner", STRIPPED_OAK_DINNER_TABLE);
        registerFurniture("stripped_birch_table_dinner", STRIPPED_BIRCH_DINNER_TABLE);
        registerFurniture("stripped_spruce_table_dinner", STRIPPED_SPRUCE_DINNER_TABLE);
        registerFurniture("stripped_acacia_table_dinner", STRIPPED_ACACIA_DINNER_TABLE);
        registerFurniture("stripped_jungle_table_dinner", STRIPPED_JUNGLE_DINNER_TABLE);
        registerFurniture("stripped_dark_oak_table_dinner", STRIPPED_DARK_OAK_DINNER_TABLE);
        registerFurniture("stripped_crimson_table_dinner", STRIPPED_CRIMSON_DINNER_TABLE);
        registerFurniture("stripped_warped_table_dinner", STRIPPED_WARPED_DINNER_TABLE);

        registerFurniture("oak_log_stool", OAK_LOG_STOOL);
        registerFurniture("birch_log_stool", BIRCH_LOG_STOOL);
        registerFurniture("spruce_log_stool", SPRUCE_LOG_STOOL);
        registerFurniture("acacia_log_stool", ACACIA_LOG_STOOL);
        registerFurniture("jungle_log_stool", JUNGLE_LOG_STOOL);
        registerFurniture("dark_oak_log_stool", DARK_OAK_LOG_STOOL);
        registerFurniture("crimson_stem_stool", CRIMSON_STEM_STOOL);
        registerFurniture("warped_stem_stool", WARPED_STEM_STOOL);

        //Item Registry
            //This makes the chair inaccessible, on purpose
          //Registry.register(Registry.ITEM, new Identifier(MOD_ID, "player_chair"), new BlockItem(PLAYER_CHAIR, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP)));

        //Counter time
        registerFurniture("oak_kitchen_counter", OAK_KITCHEN_COUNTER);

        //Dye Kits
        registerItem("dye_kit_red", DYE_KIT_RED);
        registerItem("dye_kit_orange", DYE_KIT_ORANGE);
        registerItem("dye_kit_yellow", DYE_KIT_YELLOW);
        registerItem("dye_kit_lime", DYE_KIT_LIME);
        registerItem("dye_kit_green", DYE_KIT_GREEN);
        registerItem("dye_kit_cyan", DYE_KIT_CYAN);
        registerItem("dye_kit_light_blue", DYE_KIT_LIGHT_BLUE);
        registerItem("dye_kit_blue", DYE_KIT_BLUE);
        registerItem("dye_kit_purple", DYE_KIT_PURPLE);
        registerItem("dye_kit_magenta", DYE_KIT_MAGENTA);
        registerItem("dye_kit_pink", DYE_KIT_PINK);
        registerItem("dye_kit_brown", DYE_KIT_BROWN);
        registerItem("dye_kit_black", DYE_KIT_BLACK);
        registerItem("dye_kit_gray", DYE_KIT_GRAY);
        registerItem("dye_kit_light_gray", DYE_KIT_LIGHT_GRAY);
        registerItem("dye_kit_white", DYE_KIT_WHITE);
    }
}
