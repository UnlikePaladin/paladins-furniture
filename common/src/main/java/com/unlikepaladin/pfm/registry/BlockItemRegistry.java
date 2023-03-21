package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.items.LightSwitchItem;
import com.unlikepaladin.pfm.items.ShowerHandleItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BlockItemRegistry {

    public static void registerCommonBlocks() {
        registerFurniture("working_table", PaladinFurnitureModBlocksItems.WORKING_TABLE, true);

        //Basic Chairs
        registerFurniture("oak_chair", PaladinFurnitureModBlocksItems.OAK_CHAIR, true);
        registerFurniture("birch_chair", PaladinFurnitureModBlocksItems.BIRCH_CHAIR, true);
        registerFurniture("spruce_chair", PaladinFurnitureModBlocksItems.SPRUCE_CHAIR, true);
        registerFurniture("acacia_chair", PaladinFurnitureModBlocksItems.ACACIA_CHAIR, true);
        registerFurniture("jungle_chair", PaladinFurnitureModBlocksItems.JUNGLE_CHAIR, true);
        registerFurniture("dark_oak_chair", PaladinFurnitureModBlocksItems.DARK_OAK_CHAIR, true);
        registerFurniture("warped_chair", PaladinFurnitureModBlocksItems.WARPED_CHAIR, true);
        registerFurniture("crimson_chair", PaladinFurnitureModBlocksItems.CRIMSON_CHAIR, true);
        registerFurniture("stripped_oak_chair", PaladinFurnitureModBlocksItems.STRIPPED_OAK_CHAIR, true);
        registerFurniture("stripped_birch_chair", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_CHAIR, true);
        registerFurniture("stripped_spruce_chair", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_CHAIR, true);
        registerFurniture("stripped_acacia_chair", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_CHAIR, true);
        registerFurniture("stripped_jungle_chair", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_CHAIR, true);
        registerFurniture("stripped_dark_oak_chair", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_CHAIR, true);
        registerFurniture("stripped_warped_chair", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_CHAIR, true);
        registerFurniture("stripped_crimson_chair", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_CHAIR, true);
        registerFurniture("quartz_chair", PaladinFurnitureModBlocksItems.QUARTZ_CHAIR, true);
        registerFurniture("netherite_chair", PaladinFurnitureModBlocksItems.NETHERITE_CHAIR, true);
        registerFurniture("light_wood_chair", PaladinFurnitureModBlocksItems.LIGHT_WOOD_CHAIR, true);
        registerFurniture("dark_wood_chair", PaladinFurnitureModBlocksItems.DARK_WOOD_CHAIR, true);
        registerFurniture("granite_chair", PaladinFurnitureModBlocksItems.GRANITE_CHAIR, true);
        registerFurniture("calcite_chair", PaladinFurnitureModBlocksItems.CALCITE_CHAIR, true);
        registerFurniture("andesite_chair", PaladinFurnitureModBlocksItems.ANDESITE_CHAIR, true);
        registerFurniture("diorite_chair", PaladinFurnitureModBlocksItems.DIORITE_CHAIR, true);
        registerFurniture("stone_chair", PaladinFurnitureModBlocksItems.STONE_CHAIR, true);
        registerFurniture("blackstone_chair", PaladinFurnitureModBlocksItems.BLACKSTONE_CHAIR, true);
        registerFurniture("deepslate_chair", PaladinFurnitureModBlocksItems.DEEPSLATE_CHAIR, true);

        //Dinner Chairs
        registerFurniture("oak_chair_dinner", PaladinFurnitureModBlocksItems.OAK_CHAIR_DINNER, true);
        registerFurniture("birch_chair_dinner", PaladinFurnitureModBlocksItems.BIRCH_CHAIR_DINNER, true);
        registerFurniture("spruce_chair_dinner", PaladinFurnitureModBlocksItems.SPRUCE_CHAIR_DINNER, true);
        registerFurniture("acacia_chair_dinner", PaladinFurnitureModBlocksItems.ACACIA_CHAIR_DINNER, true);
        registerFurniture("jungle_chair_dinner", PaladinFurnitureModBlocksItems.JUNGLE_CHAIR_DINNER, true);
        registerFurniture("dark_oak_chair_dinner", PaladinFurnitureModBlocksItems.DARK_OAK_CHAIR_DINNER, true);
        registerFurniture("warped_chair_dinner", PaladinFurnitureModBlocksItems.WARPED_CHAIR_DINNER, true);
        registerFurniture("crimson_chair_dinner", PaladinFurnitureModBlocksItems.CRIMSON_CHAIR_DINNER, true);
        registerFurniture("stripped_oak_chair_dinner", PaladinFurnitureModBlocksItems.STRIPPED_OAK_CHAIR_DINNER, true);
        registerFurniture("stripped_birch_chair_dinner", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_CHAIR_DINNER, true);
        registerFurniture("stripped_spruce_chair_dinner", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_CHAIR_DINNER, true);
        registerFurniture("stripped_acacia_chair_dinner", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_CHAIR_DINNER, true);
        registerFurniture("stripped_jungle_chair_dinner", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_CHAIR_DINNER, true);
        registerFurniture("stripped_dark_oak_chair_dinner", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_CHAIR_DINNER, true);
        registerFurniture("stripped_warped_chair_dinner", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_CHAIR_DINNER, true);
        registerFurniture("stripped_crimson_chair_dinner", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_CHAIR_DINNER, true);
        registerFurniture("quartz_chair_dinner", PaladinFurnitureModBlocksItems.QUARTZ_CHAIR_DINNER, true);
        registerFurniture("netherite_chair_dinner", PaladinFurnitureModBlocksItems.NETHERITE_CHAIR_DINNER, true);
        registerFurniture("light_wood_chair_dinner", PaladinFurnitureModBlocksItems.LIGHT_WOOD_CHAIR_DINNER, true);
        registerFurniture("dark_wood_chair_dinner", PaladinFurnitureModBlocksItems.DARK_WOOD_CHAIR_DINNER, true);
        registerFurniture("granite_chair_dinner", PaladinFurnitureModBlocksItems.GRANITE_CHAIR_DINNER, true);
        registerFurniture("calcite_chair_dinner", PaladinFurnitureModBlocksItems.CALCITE_CHAIR_DINNER, true);
        registerFurniture("andesite_chair_dinner", PaladinFurnitureModBlocksItems.ANDESITE_CHAIR_DINNER, true);
        registerFurniture("diorite_chair_dinner", PaladinFurnitureModBlocksItems.DIORITE_CHAIR_DINNER, true);
        registerFurniture("stone_chair_dinner", PaladinFurnitureModBlocksItems.STONE_CHAIR_DINNER, true);
        registerFurniture("blackstone_chair_dinner", PaladinFurnitureModBlocksItems.BLACKSTONE_CHAIR_DINNER, true);
        registerFurniture("deepslate_chair_dinner", PaladinFurnitureModBlocksItems.DEEPSLATE_CHAIR_DINNER, true);

        //Froggy Chairs
        registerFurniture("froggy_chair", PaladinFurnitureModBlocksItems.FROGGY_CHAIR, true);
        registerFurniture("froggy_chair_pink", PaladinFurnitureModBlocksItems.FROGGY_CHAIR_PINK, true);
        registerFurniture("froggy_chair_light_blue", PaladinFurnitureModBlocksItems.FROGGY_CHAIR_LIGHT_BLUE, true);
        registerFurniture("froggy_chair_blue", PaladinFurnitureModBlocksItems.FROGGY_CHAIR_BLUE, true);
        registerFurniture("froggy_chair_orange", PaladinFurnitureModBlocksItems.FROGGY_CHAIR_ORANGE, true);
        registerFurniture("froggy_chair_yellow", PaladinFurnitureModBlocksItems.FROGGY_CHAIR_YELLOW, true);

        //Classic Chairs
        registerFurniture("oak_chair_classic_white", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_WHITE, true);
        registerFurniture("oak_chair_classic_orange", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_ORANGE, true);
        registerFurniture("oak_chair_classic_magenta", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_MAGENTA, true);
        registerFurniture("oak_chair_classic_light_blue", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_LIGHT_BLUE, true);
        registerFurniture("oak_chair_classic_yellow", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_YELLOW, true);
        registerFurniture("oak_chair_classic_lime", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_LIME, true);
        registerFurniture("oak_chair_classic_pink", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_PINK, true);
        registerFurniture("oak_chair_classic_gray", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_GRAY, true);
        registerFurniture("oak_chair_classic_light_gray", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_LIGHT_GRAY, true);
        registerFurniture("oak_chair_classic_cyan", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_CYAN, true);
        registerFurniture("oak_chair_classic_purple", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_PURPLE, true);
        registerFurniture("oak_chair_classic_blue", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_BLUE, true);
        registerFurniture("oak_chair_classic_brown", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_BROWN, true);
        registerFurniture("oak_chair_classic_green", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_GREEN, true);
        registerFurniture("oak_chair_classic_red", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_RED, true);
        registerFurniture("oak_chair_classic_black", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC_BLACK, true);

        registerFurniture("oak_chair_classic", PaladinFurnitureModBlocksItems.OAK_CHAIR_CLASSIC, true);
        registerFurniture("birch_chair_classic", PaladinFurnitureModBlocksItems.BIRCH_CHAIR_CLASSIC, true);
        registerFurniture("spruce_chair_classic", PaladinFurnitureModBlocksItems.SPRUCE_CHAIR_CLASSIC, true);
        registerFurniture("acacia_chair_classic", PaladinFurnitureModBlocksItems.ACACIA_CHAIR_CLASSIC, true);
        registerFurniture("jungle_chair_classic", PaladinFurnitureModBlocksItems.JUNGLE_CHAIR_CLASSIC, true);
        registerFurniture("dark_oak_chair_classic", PaladinFurnitureModBlocksItems.DARK_OAK_CHAIR_CLASSIC, true);
        registerFurniture("warped_chair_classic", PaladinFurnitureModBlocksItems.WARPED_CHAIR_CLASSIC, true);
        registerFurniture("crimson_chair_classic", PaladinFurnitureModBlocksItems.CRIMSON_CHAIR_CLASSIC, true);
        registerFurniture("stripped_oak_chair_classic", PaladinFurnitureModBlocksItems.STRIPPED_OAK_CHAIR_CLASSIC, true);
        registerFurniture("stripped_birch_chair_classic", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_CHAIR_CLASSIC, true);
        registerFurniture("stripped_spruce_chair_classic", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_CHAIR_CLASSIC, true);
        registerFurniture("stripped_acacia_chair_classic", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_CHAIR_CLASSIC, true);
        registerFurniture("stripped_jungle_chair_classic", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_CHAIR_CLASSIC, true);
        registerFurniture("stripped_dark_oak_chair_classic", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_CHAIR_CLASSIC, true);
        registerFurniture("stripped_warped_chair_classic", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_CHAIR_CLASSIC, true);
        registerFurniture("stripped_crimson_chair_classic", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_CHAIR_CLASSIC, true);
        registerFurniture("quartz_chair_classic", PaladinFurnitureModBlocksItems.QUARTZ_CHAIR_CLASSIC, true);
        registerFurniture("netherite_chair_classic", PaladinFurnitureModBlocksItems.NETHERITE_CHAIR_CLASSIC, true);
        registerFurniture("light_wood_chair_classic", PaladinFurnitureModBlocksItems.LIGHT_WOOD_CHAIR_CLASSIC, true);
        registerFurniture("dark_wood_chair_classic", PaladinFurnitureModBlocksItems.DARK_WOOD_CHAIR_CLASSIC, true);
        registerFurniture("granite_chair_classic", PaladinFurnitureModBlocksItems.GRANITE_CHAIR_CLASSIC, true);
        registerFurniture("calcite_chair_classic", PaladinFurnitureModBlocksItems.CALCITE_CHAIR_CLASSIC, true);
        registerFurniture("andesite_chair_classic", PaladinFurnitureModBlocksItems.ANDESITE_CHAIR_CLASSIC, true);
        registerFurniture("diorite_chair_classic", PaladinFurnitureModBlocksItems.DIORITE_CHAIR_CLASSIC, true);
        registerFurniture("stone_chair_classic", PaladinFurnitureModBlocksItems.STONE_CHAIR_CLASSIC, true);
        registerFurniture("blackstone_chair_classic", PaladinFurnitureModBlocksItems.BLACKSTONE_CHAIR_CLASSIC, true);
        registerFurniture("deepslate_chair_classic", PaladinFurnitureModBlocksItems.DEEPSLATE_CHAIR_CLASSIC, true);

        //Modern Chair
        registerFurniture("oak_chair_modern", PaladinFurnitureModBlocksItems.OAK_CHAIR_MODERN, true);
        registerFurniture("birch_chair_modern", PaladinFurnitureModBlocksItems.BIRCH_CHAIR_MODERN, true);
        registerFurniture("spruce_chair_modern", PaladinFurnitureModBlocksItems.SPRUCE_CHAIR_MODERN, true);
        registerFurniture("acacia_chair_modern", PaladinFurnitureModBlocksItems.ACACIA_CHAIR_MODERN, true);
        registerFurniture("jungle_chair_modern", PaladinFurnitureModBlocksItems.JUNGLE_CHAIR_MODERN, true);
        registerFurniture("dark_oak_chair_modern", PaladinFurnitureModBlocksItems.DARK_OAK_CHAIR_MODERN , true);
        registerFurniture("warped_chair_modern", PaladinFurnitureModBlocksItems.WARPED_CHAIR_MODERN, true);
        registerFurniture("crimson_chair_modern", PaladinFurnitureModBlocksItems.CRIMSON_CHAIR_MODERN, true);
        registerFurniture("stripped_oak_chair_modern", PaladinFurnitureModBlocksItems.STRIPPED_OAK_CHAIR_MODERN, true);
        registerFurniture("stripped_birch_chair_modern", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_CHAIR_MODERN, true);
        registerFurniture("stripped_spruce_chair_modern", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_CHAIR_MODERN, true);
        registerFurniture("stripped_acacia_chair_modern", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_CHAIR_MODERN, true);
        registerFurniture("stripped_jungle_chair_modern", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_CHAIR_MODERN, true);
        registerFurniture("stripped_dark_oak_chair_modern", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_CHAIR_MODERN, true);
        registerFurniture("stripped_warped_chair_modern", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_CHAIR_MODERN, true);
        registerFurniture("stripped_crimson_chair_modern", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_CHAIR_MODERN, true);
        registerFurniture("quartz_chair_modern", PaladinFurnitureModBlocksItems.QUARTZ_CHAIR_MODERN, true);
        registerFurniture("netherite_chair_modern", PaladinFurnitureModBlocksItems.NETHERITE_CHAIR_MODERN, true);
        registerFurniture("light_wood_chair_modern", PaladinFurnitureModBlocksItems.LIGHT_WOOD_CHAIR_MODERN, true);
        registerFurniture("dark_wood_chair_modern", PaladinFurnitureModBlocksItems.DARK_WOOD_CHAIR_MODERN, true);
        registerFurniture("granite_chair_modern", PaladinFurnitureModBlocksItems.GRANITE_CHAIR_MODERN, true);
        registerFurniture("calcite_chair_modern", PaladinFurnitureModBlocksItems.CALCITE_CHAIR_MODERN, true);
        registerFurniture("andesite_chair_modern", PaladinFurnitureModBlocksItems.ANDESITE_CHAIR_MODERN, true);
        registerFurniture("diorite_chair_modern", PaladinFurnitureModBlocksItems.DIORITE_CHAIR_MODERN, true);
        registerFurniture("stone_chair_modern", PaladinFurnitureModBlocksItems.STONE_CHAIR_MODERN, true);
        registerFurniture("blackstone_chair_modern", PaladinFurnitureModBlocksItems.BLACKSTONE_CHAIR_MODERN, true);
        registerFurniture("deepslate_chair_modern", PaladinFurnitureModBlocksItems.DEEPSLATE_CHAIR_MODERN, true);

        //Armchairs
        registerFurniture("white_simple_sofa", PaladinFurnitureModBlocksItems.WHITE_SIMPLE_SOFA, true);
        registerFurniture("orange_simple_sofa", PaladinFurnitureModBlocksItems.ORANGE_SIMPLE_SOFA, true);
        registerFurniture("magenta_simple_sofa", PaladinFurnitureModBlocksItems.MAGENTA_SIMPLE_SOFA, true);
        registerFurniture("light_blue_simple_sofa", PaladinFurnitureModBlocksItems.LIGHT_BLUE_SIMPLE_SOFA, true);
        registerFurniture("yellow_simple_sofa", PaladinFurnitureModBlocksItems.YELLOW_SIMPLE_SOFA, true);
        registerFurniture("lime_simple_sofa", PaladinFurnitureModBlocksItems.LIME_SIMPLE_SOFA, true);
        registerFurniture("pink_simple_sofa", PaladinFurnitureModBlocksItems.PINK_SIMPLE_SOFA, true);
        registerFurniture("gray_simple_sofa", PaladinFurnitureModBlocksItems.GRAY_SIMPLE_SOFA, true);
        registerFurniture("light_gray_simple_sofa", PaladinFurnitureModBlocksItems.LIGHT_GRAY_SIMPLE_SOFA, true);
        registerFurniture("cyan_simple_sofa", PaladinFurnitureModBlocksItems.CYAN_SIMPLE_SOFA, true);
        registerFurniture("purple_simple_sofa", PaladinFurnitureModBlocksItems.PURPLE_SIMPLE_SOFA, true);
        registerFurniture("blue_simple_sofa", PaladinFurnitureModBlocksItems.BLUE_SIMPLE_SOFA, true);
        registerFurniture("brown_simple_sofa", PaladinFurnitureModBlocksItems.BROWN_SIMPLE_SOFA, true);
        registerFurniture("green_simple_sofa", PaladinFurnitureModBlocksItems.GREEN_SIMPLE_SOFA, true);
        registerFurniture("red_simple_sofa", PaladinFurnitureModBlocksItems.RED_SIMPLE_SOFA, true);
        registerFurniture("black_simple_sofa", PaladinFurnitureModBlocksItems.BLACK_SIMPLE_SOFA, true);

        registerFurniture("arm_chair_leather", PaladinFurnitureModBlocksItems.ARM_CHAIR_LEATHER, true);

        registerFurniture("white_arm_chair", PaladinFurnitureModBlocksItems.WHITE_ARM_CHAIR, true);
        registerFurniture("orange_arm_chair", PaladinFurnitureModBlocksItems.ORANGE_ARM_CHAIR, true);
        registerFurniture("magenta_arm_chair", PaladinFurnitureModBlocksItems.MAGENTA_ARM_CHAIR, true);
        registerFurniture("light_blue_arm_chair", PaladinFurnitureModBlocksItems.LIGHT_BLUE_ARM_CHAIR, true);
        registerFurniture("yellow_arm_chair", PaladinFurnitureModBlocksItems.YELLOW_ARM_CHAIR, true);
        registerFurniture("lime_arm_chair", PaladinFurnitureModBlocksItems.LIME_ARM_CHAIR, true);
        registerFurniture("pink_arm_chair", PaladinFurnitureModBlocksItems.PINK_ARM_CHAIR, true);
        registerFurniture("gray_arm_chair", PaladinFurnitureModBlocksItems.GRAY_ARM_CHAIR, true);
        registerFurniture("light_gray_arm_chair", PaladinFurnitureModBlocksItems.LIGHT_GRAY_ARM_CHAIR, true);
        registerFurniture("cyan_arm_chair", PaladinFurnitureModBlocksItems.CYAN_ARM_CHAIR, true);
        registerFurniture("purple_arm_chair", PaladinFurnitureModBlocksItems.PURPLE_ARM_CHAIR, true);
        registerFurniture("blue_arm_chair", PaladinFurnitureModBlocksItems.BLUE_ARM_CHAIR, true);
        registerFurniture("brown_arm_chair", PaladinFurnitureModBlocksItems.BROWN_ARM_CHAIR, true);
        registerFurniture("green_arm_chair", PaladinFurnitureModBlocksItems.GREEN_ARM_CHAIR, true);
        registerFurniture("red_arm_chair", PaladinFurnitureModBlocksItems.RED_ARM_CHAIR, true);
        registerFurniture("black_arm_chair", PaladinFurnitureModBlocksItems.BLACK_ARM_CHAIR, true);

        //Tables
        registerFurniture("oak_table_basic", PaladinFurnitureModBlocksItems.OAK_BASIC_TABLE, true);
        registerFurniture("birch_table_basic", PaladinFurnitureModBlocksItems.BIRCH_BASIC_TABLE, true);
        registerFurniture("spruce_table_basic", PaladinFurnitureModBlocksItems.SPRUCE_BASIC_TABLE, true);
        registerFurniture("acacia_table_basic", PaladinFurnitureModBlocksItems.ACACIA_BASIC_TABLE, true);
        registerFurniture("jungle_table_basic", PaladinFurnitureModBlocksItems.JUNGLE_BASIC_TABLE, true);
        registerFurniture("dark_oak_table_basic", PaladinFurnitureModBlocksItems.DARK_OAK_BASIC_TABLE, true);
        registerFurniture("crimson_table_basic", PaladinFurnitureModBlocksItems.CRIMSON_BASIC_TABLE, true);
        registerFurniture("warped_table_basic", PaladinFurnitureModBlocksItems.WARPED_BASIC_TABLE, true);
        registerFurniture("stripped_oak_table_basic", PaladinFurnitureModBlocksItems.STRIPPED_OAK_BASIC_TABLE, true);
        registerFurniture("stripped_birch_table_basic", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_BASIC_TABLE, true);
        registerFurniture("stripped_spruce_table_basic", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_BASIC_TABLE, true);
        registerFurniture("stripped_acacia_table_basic", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_BASIC_TABLE, true);
        registerFurniture("stripped_jungle_table_basic", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_BASIC_TABLE, true);
        registerFurniture("stripped_dark_oak_table_basic", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_BASIC_TABLE, true);
        registerFurniture("stripped_crimson_table_basic", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_BASIC_TABLE, true);
        registerFurniture("stripped_warped_table_basic", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_BASIC_TABLE, true);
        registerFurniture("quartz_table_basic", PaladinFurnitureModBlocksItems.QUARTZ_BASIC_TABLE, true);
        registerFurniture("netherite_table_basic", PaladinFurnitureModBlocksItems.NETHERITE_BASIC_TABLE, true);
        registerFurniture("light_wood_table_basic", PaladinFurnitureModBlocksItems.LIGHT_WOOD_BASIC_TABLE, true);
        registerFurniture("dark_wood_table_basic", PaladinFurnitureModBlocksItems.DARK_WOOD_BASIC_TABLE, true);
        registerFurniture("granite_table_basic", PaladinFurnitureModBlocksItems.GRANITE_BASIC_TABLE, true);
        registerFurniture("calcite_table_basic", PaladinFurnitureModBlocksItems.CALCITE_BASIC_TABLE, true);
        registerFurniture("andesite_table_basic", PaladinFurnitureModBlocksItems.ANDESITE_BASIC_TABLE, true);
        registerFurniture("diorite_table_basic", PaladinFurnitureModBlocksItems.DIORITE_BASIC_TABLE, true);
        registerFurniture("stone_table_basic", PaladinFurnitureModBlocksItems.STONE_BASIC_TABLE, true);
        registerFurniture("blackstone_table_basic", PaladinFurnitureModBlocksItems.BLACKSTONE_BASIC_TABLE, true);
        registerFurniture("deepslate_table_basic", PaladinFurnitureModBlocksItems.DEEPSLATE_BASIC_TABLE, true);

        //Classic Table
        registerFurniture("oak_table_classic", PaladinFurnitureModBlocksItems.OAK_CLASSIC_TABLE, true);
        registerFurniture("birch_table_classic", PaladinFurnitureModBlocksItems.BIRCH_CLASSIC_TABLE, true);
        registerFurniture("spruce_table_classic", PaladinFurnitureModBlocksItems.SPRUCE_CLASSIC_TABLE, true);
        registerFurniture("acacia_table_classic", PaladinFurnitureModBlocksItems.ACACIA_CLASSIC_TABLE, true);
        registerFurniture("jungle_table_classic", PaladinFurnitureModBlocksItems.JUNGLE_CLASSIC_TABLE, true);
        registerFurniture("dark_oak_table_classic", PaladinFurnitureModBlocksItems.DARK_OAK_CLASSIC_TABLE, true);
        registerFurniture("crimson_table_classic", PaladinFurnitureModBlocksItems.CRIMSON_CLASSIC_TABLE, true);
        registerFurniture("warped_table_classic", PaladinFurnitureModBlocksItems.WARPED_CLASSIC_TABLE, true);
        registerFurniture("stripped_oak_table_classic", PaladinFurnitureModBlocksItems.STRIPPED_OAK_CLASSIC_TABLE, true);
        registerFurniture("stripped_birch_table_classic", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_CLASSIC_TABLE, true);
        registerFurniture("stripped_spruce_table_classic", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_CLASSIC_TABLE, true);
        registerFurniture("stripped_acacia_table_classic", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_CLASSIC_TABLE, true);
        registerFurniture("stripped_jungle_table_classic", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_CLASSIC_TABLE, true);
        registerFurniture("stripped_dark_oak_table_classic", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_CLASSIC_TABLE, true);
        registerFurniture("stripped_crimson_table_classic", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_CLASSIC_TABLE, true);
        registerFurniture("stripped_warped_table_classic", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_CLASSIC_TABLE, true);
        registerFurniture("quartz_table_classic", PaladinFurnitureModBlocksItems.QUARTZ_CLASSIC_TABLE, true);
        registerFurniture("netherite_table_classic", PaladinFurnitureModBlocksItems.NETHERITE_CLASSIC_TABLE, true);
        registerFurniture("light_wood_table_classic", PaladinFurnitureModBlocksItems.LIGHT_WOOD_CLASSIC_TABLE, true);
        registerFurniture("dark_wood_table_classic", PaladinFurnitureModBlocksItems.DARK_WOOD_CLASSIC_TABLE, true);
        registerFurniture("granite_table_classic", PaladinFurnitureModBlocksItems.GRANITE_CLASSIC_TABLE, true);
        registerFurniture("calcite_table_classic", PaladinFurnitureModBlocksItems.CALCITE_CLASSIC_TABLE, true);
        registerFurniture("andesite_table_classic", PaladinFurnitureModBlocksItems.ANDESITE_CLASSIC_TABLE, true);
        registerFurniture("diorite_table_classic", PaladinFurnitureModBlocksItems.DIORITE_CLASSIC_TABLE, true);
        registerFurniture("stone_table_classic", PaladinFurnitureModBlocksItems.STONE_CLASSIC_TABLE, true);
        registerFurniture("blackstone_table_classic", PaladinFurnitureModBlocksItems.BLACKSTONE_CLASSIC_TABLE, true);
        registerFurniture("deepslate_table_classic", PaladinFurnitureModBlocksItems.DEEPSLATE_CLASSIC_TABLE, true);

        //Log Table
        registerFurniture("oak_table_log", PaladinFurnitureModBlocksItems.OAK_LOG_TABLE, true);
        registerFurniture("birch_table_log", PaladinFurnitureModBlocksItems.BIRCH_LOG_TABLE, true);
        registerFurniture("spruce_table_log", PaladinFurnitureModBlocksItems.SPRUCE_LOG_TABLE, true);
        registerFurniture("acacia_table_log", PaladinFurnitureModBlocksItems.ACACIA_LOG_TABLE, true);
        registerFurniture("jungle_table_log", PaladinFurnitureModBlocksItems.JUNGLE_LOG_TABLE, true);
        registerFurniture("dark_oak_table_log", PaladinFurnitureModBlocksItems.DARK_OAK_LOG_TABLE, true);
        registerFurniture("crimson_table_stem", PaladinFurnitureModBlocksItems.CRIMSON_STEM_TABLE, true);
        registerFurniture("warped_table_stem", PaladinFurnitureModBlocksItems.WARPED_STEM_TABLE, true);
        registerFurniture("stripped_oak_table_log", PaladinFurnitureModBlocksItems.STRIPPED_OAK_LOG_TABLE, true);
        registerFurniture("stripped_birch_table_log", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_LOG_TABLE, true);
        registerFurniture("stripped_spruce_table_log", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_LOG_TABLE, true);
        registerFurniture("stripped_acacia_table_log", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_LOG_TABLE, true);
        registerFurniture("stripped_jungle_table_log", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_LOG_TABLE, true);
        registerFurniture("stripped_dark_oak_table_log", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_LOG_TABLE, true);
        registerFurniture("stripped_crimson_table_stem", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_STEM_TABLE, true);
        registerFurniture("stripped_warped_table_stem", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_STEM_TABLE, true);
        registerFurniture("quartz_table_natural", PaladinFurnitureModBlocksItems.QUARTZ_NATURAL_TABLE, true);
        registerFurniture("netherite_table_natural", PaladinFurnitureModBlocksItems.NETHERITE_NATURAL_TABLE, true);
        registerFurniture("light_wood_table_natural", PaladinFurnitureModBlocksItems.LIGHT_WOOD_NATURAL_TABLE, true);
        registerFurniture("dark_wood_table_natural", PaladinFurnitureModBlocksItems.DARK_WOOD_NATURAL_TABLE, true);
        registerFurniture("granite_table_natural", PaladinFurnitureModBlocksItems.GRANITE_NATURAL_TABLE, true);
        registerFurniture("calcite_table_natural", PaladinFurnitureModBlocksItems.CALCITE_NATURAL_TABLE, true);
        registerFurniture("andesite_table_natural", PaladinFurnitureModBlocksItems.ANDESITE_NATURAL_TABLE, true);
        registerFurniture("diorite_table_natural", PaladinFurnitureModBlocksItems.DIORITE_NATURAL_TABLE, true);
        registerFurniture("stone_table_natural", PaladinFurnitureModBlocksItems.STONE_NATURAL_TABLE, true);
        registerFurniture("blackstone_table_natural", PaladinFurnitureModBlocksItems.BLACKSTONE_NATURAL_TABLE, true);
        registerFurniture("deepslate_table_natural", PaladinFurnitureModBlocksItems.DEEPSLATE_NATURAL_TABLE, true);

        //Raw Log Table
        registerFurniture("oak_raw_table_log", PaladinFurnitureModBlocksItems.OAK_RAW_LOG_TABLE, true);
        registerFurniture("birch_raw_table_log", PaladinFurnitureModBlocksItems.BIRCH_RAW_LOG_TABLE, true);
        registerFurniture("acacia_raw_table_log", PaladinFurnitureModBlocksItems.ACACIA_RAW_LOG_TABLE, true);
        registerFurniture("spruce_raw_table_log", PaladinFurnitureModBlocksItems.SPRUCE_RAW_LOG_TABLE, true);
        registerFurniture("jungle_raw_table_log", PaladinFurnitureModBlocksItems.JUNGLE_RAW_LOG_TABLE, true);
        registerFurniture("dark_oak_raw_table_log", PaladinFurnitureModBlocksItems.DARK_OAK_RAW_LOG_TABLE, true);
        registerFurniture("warped_raw_table_stem", PaladinFurnitureModBlocksItems.WARPED_RAW_STEM_TABLE, true);
        registerFurniture("crimson_raw_table_stem", PaladinFurnitureModBlocksItems.CRIMSON_RAW_STEM_TABLE, true);
        registerFurniture("stripped_oak_raw_table_log", PaladinFurnitureModBlocksItems.STRIPPED_OAK_RAW_LOG_TABLE, true);
        registerFurniture("stripped_birch_raw_table_log", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_RAW_LOG_TABLE, true);
        registerFurniture("stripped_acacia_raw_table_log", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_RAW_LOG_TABLE, true);
        registerFurniture("stripped_spruce_raw_table_log", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_RAW_LOG_TABLE, true);
        registerFurniture("stripped_jungle_raw_table_log", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_RAW_LOG_TABLE, true);
        registerFurniture("stripped_dark_oak_raw_table_log", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_RAW_LOG_TABLE, true);
        registerFurniture("stripped_warped_raw_table_stem", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_RAW_STEM_TABLE, true);
        registerFurniture("stripped_crimson_raw_table_stem", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_RAW_STEM_TABLE, true);

        registerFurniture("oak_table_dinner", PaladinFurnitureModBlocksItems.OAK_DINNER_TABLE, true);
        registerFurniture("birch_table_dinner", PaladinFurnitureModBlocksItems.BIRCH_DINNER_TABLE, true);
        registerFurniture("spruce_table_dinner", PaladinFurnitureModBlocksItems.SPRUCE_DINNER_TABLE, true);
        registerFurniture("acacia_table_dinner", PaladinFurnitureModBlocksItems.ACACIA_DINNER_TABLE, true);
        registerFurniture("jungle_table_dinner", PaladinFurnitureModBlocksItems.JUNGLE_DINNER_TABLE, true);
        registerFurniture("dark_oak_table_dinner", PaladinFurnitureModBlocksItems.DARK_OAK_DINNER_TABLE, true);
        registerFurniture("crimson_table_dinner", PaladinFurnitureModBlocksItems.CRIMSON_DINNER_TABLE, true);
        registerFurniture("warped_table_dinner", PaladinFurnitureModBlocksItems.WARPED_DINNER_TABLE, true);
        registerFurniture("stripped_oak_table_dinner", PaladinFurnitureModBlocksItems.STRIPPED_OAK_DINNER_TABLE, true);
        registerFurniture("stripped_birch_table_dinner", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_DINNER_TABLE, true);
        registerFurniture("stripped_spruce_table_dinner", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_DINNER_TABLE, true);
        registerFurniture("stripped_acacia_table_dinner", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_DINNER_TABLE, true);
        registerFurniture("stripped_jungle_table_dinner", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_DINNER_TABLE, true);
        registerFurniture("stripped_dark_oak_table_dinner", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_DINNER_TABLE, true);
        registerFurniture("stripped_crimson_table_dinner", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_DINNER_TABLE, true);
        registerFurniture("stripped_warped_table_dinner", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_DINNER_TABLE, true);
        registerFurniture("quartz_table_dinner", PaladinFurnitureModBlocksItems.QUARTZ_DINNER_TABLE, true);
        registerFurniture("netherite_table_dinner", PaladinFurnitureModBlocksItems.NETHERITE_DINNER_TABLE, true);
        registerFurniture("light_wood_table_dinner", PaladinFurnitureModBlocksItems.LIGHT_WOOD_DINNER_TABLE, true);
        registerFurniture("dark_wood_table_dinner", PaladinFurnitureModBlocksItems.DARK_WOOD_DINNER_TABLE, true);
        registerFurniture("granite_table_dinner", PaladinFurnitureModBlocksItems.GRANITE_DINNER_TABLE, true);
        registerFurniture("calcite_table_dinner", PaladinFurnitureModBlocksItems.CALCITE_DINNER_TABLE, true);
        registerFurniture("andesite_table_dinner", PaladinFurnitureModBlocksItems.ANDESITE_DINNER_TABLE, true);
        registerFurniture("diorite_table_dinner", PaladinFurnitureModBlocksItems.DIORITE_DINNER_TABLE, true);
        registerFurniture("stone_table_dinner", PaladinFurnitureModBlocksItems.STONE_DINNER_TABLE, true);
        registerFurniture("blackstone_table_dinner", PaladinFurnitureModBlocksItems.BLACKSTONE_DINNER_TABLE, true);
        registerFurniture("deepslate_table_dinner", PaladinFurnitureModBlocksItems.DEEPSLATE_DINNER_TABLE, true);

        registerFurniture("oak_table_modern_dinner", PaladinFurnitureModBlocksItems.OAK_MODERN_DINNER_TABLE, true);
        registerFurniture("birch_table_modern_dinner", PaladinFurnitureModBlocksItems.BIRCH_MODERN_DINNER_TABLE, true);
        registerFurniture("spruce_table_modern_dinner", PaladinFurnitureModBlocksItems.SPRUCE_MODERN_DINNER_TABLE, true);
        registerFurniture("acacia_table_modern_dinner", PaladinFurnitureModBlocksItems.ACACIA_MODERN_DINNER_TABLE, true);
        registerFurniture("jungle_table_modern_dinner", PaladinFurnitureModBlocksItems.JUNGLE_MODERN_DINNER_TABLE, true);
        registerFurniture("dark_oak_table_modern_dinner", PaladinFurnitureModBlocksItems.DARK_OAK_MODERN_DINNER_TABLE, true);
        registerFurniture("crimson_table_modern_dinner", PaladinFurnitureModBlocksItems.CRIMSON_MODERN_DINNER_TABLE, true);
        registerFurniture("warped_table_modern_dinner", PaladinFurnitureModBlocksItems.WARPED_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_oak_table_modern_dinner", PaladinFurnitureModBlocksItems.STRIPPED_OAK_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_birch_table_modern_dinner", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_spruce_table_modern_dinner", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_acacia_table_modern_dinner", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_jungle_table_modern_dinner", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_dark_oak_table_modern_dinner", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_crimson_table_modern_dinner", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_warped_table_modern_dinner", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_MODERN_DINNER_TABLE, true);
        registerFurniture("quartz_table_modern_dinner", PaladinFurnitureModBlocksItems.QUARTZ_MODERN_DINNER_TABLE, true);
        registerFurniture("netherite_table_modern_dinner", PaladinFurnitureModBlocksItems.NETHERITE_MODERN_DINNER_TABLE, true);
        registerFurniture("light_wood_table_modern_dinner", PaladinFurnitureModBlocksItems.LIGHT_WOOD_MODERN_DINNER_TABLE, true);
        registerFurniture("dark_wood_table_modern_dinner", PaladinFurnitureModBlocksItems.DARK_WOOD_MODERN_DINNER_TABLE, true);
        registerFurniture("granite_table_modern_dinner", PaladinFurnitureModBlocksItems.GRANITE_MODERN_DINNER_TABLE, true);
        registerFurniture("calcite_table_modern_dinner", PaladinFurnitureModBlocksItems.CALCITE_MODERN_DINNER_TABLE, true);
        registerFurniture("andesite_table_modern_dinner", PaladinFurnitureModBlocksItems.ANDESITE_MODERN_DINNER_TABLE, true);
        registerFurniture("diorite_table_modern_dinner", PaladinFurnitureModBlocksItems.DIORITE_MODERN_DINNER_TABLE, true);
        registerFurniture("stone_table_modern_dinner", PaladinFurnitureModBlocksItems.STONE_MODERN_DINNER_TABLE, true);
        registerFurniture("blackstone_table_modern_dinner", PaladinFurnitureModBlocksItems.BLACKSTONE_MODERN_DINNER_TABLE, true);
        registerFurniture("deepslate_table_modern_dinner", PaladinFurnitureModBlocksItems.DEEPSLATE_MODERN_DINNER_TABLE, true);

        registerFurniture("oak_classic_nightstand", PaladinFurnitureModBlocksItems.OAK_CLASSIC_NIGHTSTAND, true);
        registerFurniture("birch_classic_nightstand", PaladinFurnitureModBlocksItems.BIRCH_CLASSIC_NIGHTSTAND, true);
        registerFurniture("spruce_classic_nightstand", PaladinFurnitureModBlocksItems.SPRUCE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("acacia_classic_nightstand", PaladinFurnitureModBlocksItems.ACACIA_CLASSIC_NIGHTSTAND, true);
        registerFurniture("jungle_classic_nightstand", PaladinFurnitureModBlocksItems.JUNGLE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("dark_oak_classic_nightstand", PaladinFurnitureModBlocksItems.DARK_OAK_CLASSIC_NIGHTSTAND, true);
        registerFurniture("crimson_classic_nightstand", PaladinFurnitureModBlocksItems.CRIMSON_CLASSIC_NIGHTSTAND, true);
        registerFurniture("warped_classic_nightstand", PaladinFurnitureModBlocksItems.WARPED_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_oak_classic_nightstand", PaladinFurnitureModBlocksItems.STRIPPED_OAK_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_birch_classic_nightstand", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_spruce_classic_nightstand", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_acacia_classic_nightstand", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_jungle_classic_nightstand", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_dark_oak_classic_nightstand", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_crimson_classic_nightstand", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_warped_classic_nightstand", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_CLASSIC_NIGHTSTAND, true);
        registerFurniture("quartz_classic_nightstand", PaladinFurnitureModBlocksItems.QUARTZ_CLASSIC_NIGHTSTAND, true);
        registerFurniture("netherite_classic_nightstand", PaladinFurnitureModBlocksItems.NETHERITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("light_wood_classic_nightstand", PaladinFurnitureModBlocksItems.LIGHT_WOOD_CLASSIC_NIGHTSTAND, true);
        registerFurniture("dark_wood_classic_nightstand", PaladinFurnitureModBlocksItems.DARK_WOOD_CLASSIC_NIGHTSTAND, true);
        registerFurniture("granite_classic_nightstand", PaladinFurnitureModBlocksItems.GRANITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("calcite_classic_nightstand", PaladinFurnitureModBlocksItems.CALCITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("andesite_classic_nightstand", PaladinFurnitureModBlocksItems.ANDESITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("diorite_classic_nightstand", PaladinFurnitureModBlocksItems.DIORITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stone_classic_nightstand", PaladinFurnitureModBlocksItems.STONE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("blackstone_classic_nightstand", PaladinFurnitureModBlocksItems.BLACKSTONE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("deepslate_classic_nightstand", PaladinFurnitureModBlocksItems.DEEPSLATE_CLASSIC_NIGHTSTAND, true);

        registerFurniture("oak_red_simple_bed", PaladinFurnitureModBlocksItems.OAK_RED_SIMPLE_BED, 1);
        registerFurniture("oak_orange_simple_bed", PaladinFurnitureModBlocksItems.OAK_ORANGE_SIMPLE_BED, 1);
        registerFurniture("oak_yellow_simple_bed", PaladinFurnitureModBlocksItems.OAK_YELLOW_SIMPLE_BED, 1);
        registerFurniture("oak_green_simple_bed", PaladinFurnitureModBlocksItems.OAK_GREEN_SIMPLE_BED, 1);
        registerFurniture("oak_lime_simple_bed", PaladinFurnitureModBlocksItems.OAK_LIME_SIMPLE_BED, 1);
        registerFurniture("oak_blue_simple_bed", PaladinFurnitureModBlocksItems.OAK_BLUE_SIMPLE_BED, 1);
        registerFurniture("oak_cyan_simple_bed", PaladinFurnitureModBlocksItems.OAK_CYAN_SIMPLE_BED, 1);
        registerFurniture("oak_light_blue_simple_bed", PaladinFurnitureModBlocksItems.OAK_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("oak_light_gray_simple_bed", PaladinFurnitureModBlocksItems.OAK_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("oak_purple_simple_bed", PaladinFurnitureModBlocksItems.OAK_PURPLE_SIMPLE_BED, 1);
        registerFurniture("oak_magenta_simple_bed", PaladinFurnitureModBlocksItems.OAK_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("oak_pink_simple_bed", PaladinFurnitureModBlocksItems.OAK_PINK_SIMPLE_BED, 1);
        registerFurniture("oak_brown_simple_bed", PaladinFurnitureModBlocksItems.OAK_BROWN_SIMPLE_BED, 1);
        registerFurniture("oak_gray_simple_bed", PaladinFurnitureModBlocksItems.OAK_GRAY_SIMPLE_BED, 1);
        registerFurniture("oak_black_simple_bed", PaladinFurnitureModBlocksItems.OAK_BLACK_SIMPLE_BED, 1);
        registerFurniture("oak_white_simple_bed", PaladinFurnitureModBlocksItems.OAK_WHITE_SIMPLE_BED, 1);
        registerFurniture("spruce_red_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_RED_SIMPLE_BED, 1);
        registerFurniture("spruce_orange_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_ORANGE_SIMPLE_BED, 1);
        registerFurniture("spruce_yellow_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_YELLOW_SIMPLE_BED, 1);
        registerFurniture("spruce_green_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_GREEN_SIMPLE_BED, 1);
        registerFurniture("spruce_lime_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_LIME_SIMPLE_BED, 1);
        registerFurniture("spruce_blue_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_BLUE_SIMPLE_BED, 1);
        registerFurniture("spruce_cyan_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_CYAN_SIMPLE_BED, 1);
        registerFurniture("spruce_light_blue_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("spruce_light_gray_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("spruce_purple_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_PURPLE_SIMPLE_BED, 1);
        registerFurniture("spruce_magenta_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("spruce_pink_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_PINK_SIMPLE_BED, 1);
        registerFurniture("spruce_brown_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_BROWN_SIMPLE_BED, 1);
        registerFurniture("spruce_gray_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_GRAY_SIMPLE_BED, 1);
        registerFurniture("spruce_black_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_BLACK_SIMPLE_BED, 1);
        registerFurniture("spruce_white_simple_bed", PaladinFurnitureModBlocksItems.SPRUCE_WHITE_SIMPLE_BED, 1);
        registerFurniture("birch_red_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_RED_SIMPLE_BED, 1);
        registerFurniture("birch_orange_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_ORANGE_SIMPLE_BED, 1);
        registerFurniture("birch_yellow_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_YELLOW_SIMPLE_BED, 1);
        registerFurniture("birch_green_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_GREEN_SIMPLE_BED, 1);
        registerFurniture("birch_lime_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_LIME_SIMPLE_BED, 1);
        registerFurniture("birch_blue_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_BLUE_SIMPLE_BED, 1);
        registerFurniture("birch_cyan_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_CYAN_SIMPLE_BED, 1);
        registerFurniture("birch_light_blue_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("birch_light_gray_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("birch_purple_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_PURPLE_SIMPLE_BED, 1);
        registerFurniture("birch_magenta_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("birch_pink_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_PINK_SIMPLE_BED, 1);
        registerFurniture("birch_brown_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_BROWN_SIMPLE_BED, 1);
        registerFurniture("birch_gray_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_GRAY_SIMPLE_BED, 1);
        registerFurniture("birch_black_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_BLACK_SIMPLE_BED, 1);
        registerFurniture("birch_white_simple_bed", PaladinFurnitureModBlocksItems.BIRCH_WHITE_SIMPLE_BED, 1);
        registerFurniture("acacia_red_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_RED_SIMPLE_BED, 1);
        registerFurniture("acacia_orange_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_ORANGE_SIMPLE_BED, 1);
        registerFurniture("acacia_yellow_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_YELLOW_SIMPLE_BED, 1);
        registerFurniture("acacia_green_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_GREEN_SIMPLE_BED, 1);
        registerFurniture("acacia_lime_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_LIME_SIMPLE_BED, 1);
        registerFurniture("acacia_blue_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_BLUE_SIMPLE_BED, 1);
        registerFurniture("acacia_cyan_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_CYAN_SIMPLE_BED, 1);
        registerFurniture("acacia_light_blue_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("acacia_light_gray_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("acacia_purple_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_PURPLE_SIMPLE_BED, 1);
        registerFurniture("acacia_magenta_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("acacia_pink_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_PINK_SIMPLE_BED, 1);
        registerFurniture("acacia_brown_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_BROWN_SIMPLE_BED, 1);
        registerFurniture("acacia_gray_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_GRAY_SIMPLE_BED, 1);
        registerFurniture("acacia_black_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_BLACK_SIMPLE_BED, 1);
        registerFurniture("acacia_white_simple_bed", PaladinFurnitureModBlocksItems.ACACIA_WHITE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_red_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_RED_SIMPLE_BED, 1);
        registerFurniture("dark_oak_orange_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_ORANGE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_yellow_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_YELLOW_SIMPLE_BED, 1);
        registerFurniture("dark_oak_green_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_GREEN_SIMPLE_BED, 1);
        registerFurniture("dark_oak_lime_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_LIME_SIMPLE_BED, 1);
        registerFurniture("dark_oak_blue_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_BLUE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_cyan_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_CYAN_SIMPLE_BED, 1);
        registerFurniture("dark_oak_light_blue_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_light_gray_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("dark_oak_purple_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_PURPLE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_magenta_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("dark_oak_pink_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_PINK_SIMPLE_BED, 1);
        registerFurniture("dark_oak_brown_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_BROWN_SIMPLE_BED, 1);
        registerFurniture("dark_oak_gray_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_GRAY_SIMPLE_BED, 1);
        registerFurniture("dark_oak_black_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_BLACK_SIMPLE_BED, 1);
        registerFurniture("dark_oak_white_simple_bed", PaladinFurnitureModBlocksItems.DARK_OAK_WHITE_SIMPLE_BED, 1);
        registerFurniture("jungle_red_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_RED_SIMPLE_BED, 1);
        registerFurniture("jungle_orange_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_ORANGE_SIMPLE_BED, 1);
        registerFurniture("jungle_yellow_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_YELLOW_SIMPLE_BED, 1);
        registerFurniture("jungle_green_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_GREEN_SIMPLE_BED, 1);
        registerFurniture("jungle_lime_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_LIME_SIMPLE_BED, 1);
        registerFurniture("jungle_blue_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_BLUE_SIMPLE_BED, 1);
        registerFurniture("jungle_cyan_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_CYAN_SIMPLE_BED, 1);
        registerFurniture("jungle_light_blue_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("jungle_light_gray_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("jungle_purple_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_PURPLE_SIMPLE_BED, 1);
        registerFurniture("jungle_magenta_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("jungle_pink_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_PINK_SIMPLE_BED, 1);
        registerFurniture("jungle_brown_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_BROWN_SIMPLE_BED, 1);
        registerFurniture("jungle_gray_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_GRAY_SIMPLE_BED, 1);
        registerFurniture("jungle_black_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_BLACK_SIMPLE_BED, 1);
        registerFurniture("jungle_white_simple_bed", PaladinFurnitureModBlocksItems.JUNGLE_WHITE_SIMPLE_BED, 1);
        registerFurniture("warped_red_simple_bed", PaladinFurnitureModBlocksItems.WARPED_RED_SIMPLE_BED, 1);
        registerFurniture("warped_orange_simple_bed", PaladinFurnitureModBlocksItems.WARPED_ORANGE_SIMPLE_BED, 1);
        registerFurniture("warped_yellow_simple_bed", PaladinFurnitureModBlocksItems.WARPED_YELLOW_SIMPLE_BED, 1);
        registerFurniture("warped_green_simple_bed", PaladinFurnitureModBlocksItems.WARPED_GREEN_SIMPLE_BED, 1);
        registerFurniture("warped_lime_simple_bed", PaladinFurnitureModBlocksItems.WARPED_LIME_SIMPLE_BED, 1);
        registerFurniture("warped_blue_simple_bed", PaladinFurnitureModBlocksItems.WARPED_BLUE_SIMPLE_BED, 1);
        registerFurniture("warped_cyan_simple_bed", PaladinFurnitureModBlocksItems.WARPED_CYAN_SIMPLE_BED, 1);
        registerFurniture("warped_light_blue_simple_bed", PaladinFurnitureModBlocksItems.WARPED_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("warped_light_gray_simple_bed", PaladinFurnitureModBlocksItems.WARPED_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("warped_purple_simple_bed", PaladinFurnitureModBlocksItems.WARPED_PURPLE_SIMPLE_BED, 1);
        registerFurniture("warped_magenta_simple_bed", PaladinFurnitureModBlocksItems.WARPED_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("warped_pink_simple_bed", PaladinFurnitureModBlocksItems.WARPED_PINK_SIMPLE_BED, 1);
        registerFurniture("warped_brown_simple_bed", PaladinFurnitureModBlocksItems.WARPED_BROWN_SIMPLE_BED, 1);
        registerFurniture("warped_gray_simple_bed", PaladinFurnitureModBlocksItems.WARPED_GRAY_SIMPLE_BED, 1);
        registerFurniture("warped_black_simple_bed", PaladinFurnitureModBlocksItems.WARPED_BLACK_SIMPLE_BED, 1);
        registerFurniture("warped_white_simple_bed", PaladinFurnitureModBlocksItems.WARPED_WHITE_SIMPLE_BED, 1);
        registerFurniture("crimson_red_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_RED_SIMPLE_BED, 1);
        registerFurniture("crimson_orange_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_ORANGE_SIMPLE_BED, 1);
        registerFurniture("crimson_yellow_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_YELLOW_SIMPLE_BED, 1);
        registerFurniture("crimson_green_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_GREEN_SIMPLE_BED, 1);
        registerFurniture("crimson_lime_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_LIME_SIMPLE_BED, 1);
        registerFurniture("crimson_blue_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_BLUE_SIMPLE_BED, 1);
        registerFurniture("crimson_cyan_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_CYAN_SIMPLE_BED, 1);
        registerFurniture("crimson_light_blue_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("crimson_light_gray_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("crimson_purple_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_PURPLE_SIMPLE_BED, 1);
        registerFurniture("crimson_magenta_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("crimson_pink_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_PINK_SIMPLE_BED, 1);
        registerFurniture("crimson_brown_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_BROWN_SIMPLE_BED, 1);
        registerFurniture("crimson_gray_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_GRAY_SIMPLE_BED, 1);
        registerFurniture("crimson_black_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_BLACK_SIMPLE_BED, 1);
        registerFurniture("crimson_white_simple_bed", PaladinFurnitureModBlocksItems.CRIMSON_WHITE_SIMPLE_BED, 1);

        registerFurniture("oak_red_classic_bed", PaladinFurnitureModBlocksItems.OAK_RED_CLASSIC_BED, 1);
        registerFurniture("oak_orange_classic_bed", PaladinFurnitureModBlocksItems.OAK_ORANGE_CLASSIC_BED, 1);
        registerFurniture("oak_yellow_classic_bed", PaladinFurnitureModBlocksItems.OAK_YELLOW_CLASSIC_BED, 1);
        registerFurniture("oak_green_classic_bed", PaladinFurnitureModBlocksItems.OAK_GREEN_CLASSIC_BED, 1);
        registerFurniture("oak_lime_classic_bed", PaladinFurnitureModBlocksItems.OAK_LIME_CLASSIC_BED, 1);
        registerFurniture("oak_blue_classic_bed", PaladinFurnitureModBlocksItems.OAK_BLUE_CLASSIC_BED, 1);
        registerFurniture("oak_cyan_classic_bed", PaladinFurnitureModBlocksItems.OAK_CYAN_CLASSIC_BED, 1);
        registerFurniture("oak_light_blue_classic_bed", PaladinFurnitureModBlocksItems.OAK_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("oak_light_gray_classic_bed", PaladinFurnitureModBlocksItems.OAK_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("oak_purple_classic_bed", PaladinFurnitureModBlocksItems.OAK_PURPLE_CLASSIC_BED, 1);
        registerFurniture("oak_magenta_classic_bed", PaladinFurnitureModBlocksItems.OAK_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("oak_pink_classic_bed", PaladinFurnitureModBlocksItems.OAK_PINK_CLASSIC_BED, 1);
        registerFurniture("oak_brown_classic_bed", PaladinFurnitureModBlocksItems.OAK_BROWN_CLASSIC_BED, 1);
        registerFurniture("oak_gray_classic_bed", PaladinFurnitureModBlocksItems.OAK_GRAY_CLASSIC_BED, 1);
        registerFurniture("oak_black_classic_bed", PaladinFurnitureModBlocksItems.OAK_BLACK_CLASSIC_BED, 1);
        registerFurniture("oak_white_classic_bed", PaladinFurnitureModBlocksItems.OAK_WHITE_CLASSIC_BED, 1);
        registerFurniture("spruce_red_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_RED_CLASSIC_BED, 1);
        registerFurniture("spruce_orange_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_ORANGE_CLASSIC_BED, 1);
        registerFurniture("spruce_yellow_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_YELLOW_CLASSIC_BED, 1);
        registerFurniture("spruce_green_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_GREEN_CLASSIC_BED, 1);
        registerFurniture("spruce_lime_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_LIME_CLASSIC_BED, 1);
        registerFurniture("spruce_blue_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_BLUE_CLASSIC_BED, 1);
        registerFurniture("spruce_cyan_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_CYAN_CLASSIC_BED, 1);
        registerFurniture("spruce_light_blue_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("spruce_light_gray_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("spruce_purple_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_PURPLE_CLASSIC_BED, 1);
        registerFurniture("spruce_magenta_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("spruce_pink_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_PINK_CLASSIC_BED, 1);
        registerFurniture("spruce_brown_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_BROWN_CLASSIC_BED, 1);
        registerFurniture("spruce_gray_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_GRAY_CLASSIC_BED, 1);
        registerFurniture("spruce_black_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_BLACK_CLASSIC_BED, 1);
        registerFurniture("spruce_white_classic_bed", PaladinFurnitureModBlocksItems.SPRUCE_WHITE_CLASSIC_BED, 1);
        registerFurniture("birch_red_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_RED_CLASSIC_BED, 1);
        registerFurniture("birch_orange_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_ORANGE_CLASSIC_BED, 1);
        registerFurniture("birch_yellow_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_YELLOW_CLASSIC_BED, 1);
        registerFurniture("birch_green_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_GREEN_CLASSIC_BED, 1);
        registerFurniture("birch_lime_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_LIME_CLASSIC_BED, 1);
        registerFurniture("birch_blue_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_BLUE_CLASSIC_BED, 1);
        registerFurniture("birch_cyan_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_CYAN_CLASSIC_BED, 1);
        registerFurniture("birch_light_blue_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("birch_light_gray_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("birch_purple_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_PURPLE_CLASSIC_BED, 1);
        registerFurniture("birch_magenta_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("birch_pink_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_PINK_CLASSIC_BED, 1);
        registerFurniture("birch_brown_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_BROWN_CLASSIC_BED, 1);
        registerFurniture("birch_gray_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_GRAY_CLASSIC_BED, 1);
        registerFurniture("birch_black_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_BLACK_CLASSIC_BED, 1);
        registerFurniture("birch_white_classic_bed", PaladinFurnitureModBlocksItems.BIRCH_WHITE_CLASSIC_BED, 1);
        registerFurniture("acacia_red_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_RED_CLASSIC_BED, 1);
        registerFurniture("acacia_orange_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_ORANGE_CLASSIC_BED, 1);
        registerFurniture("acacia_yellow_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_YELLOW_CLASSIC_BED, 1);
        registerFurniture("acacia_green_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_GREEN_CLASSIC_BED, 1);
        registerFurniture("acacia_lime_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_LIME_CLASSIC_BED, 1);
        registerFurniture("acacia_blue_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_BLUE_CLASSIC_BED, 1);
        registerFurniture("acacia_cyan_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_CYAN_CLASSIC_BED, 1);
        registerFurniture("acacia_light_blue_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("acacia_light_gray_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("acacia_purple_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_PURPLE_CLASSIC_BED, 1);
        registerFurniture("acacia_magenta_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("acacia_pink_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_PINK_CLASSIC_BED, 1);
        registerFurniture("acacia_brown_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_BROWN_CLASSIC_BED, 1);
        registerFurniture("acacia_gray_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_GRAY_CLASSIC_BED, 1);
        registerFurniture("acacia_black_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_BLACK_CLASSIC_BED, 1);
        registerFurniture("acacia_white_classic_bed", PaladinFurnitureModBlocksItems.ACACIA_WHITE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_red_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_RED_CLASSIC_BED, 1);
        registerFurniture("dark_oak_orange_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_ORANGE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_yellow_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_YELLOW_CLASSIC_BED, 1);
        registerFurniture("dark_oak_green_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_GREEN_CLASSIC_BED, 1);
        registerFurniture("dark_oak_lime_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_LIME_CLASSIC_BED, 1);
        registerFurniture("dark_oak_blue_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_BLUE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_cyan_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_CYAN_CLASSIC_BED, 1);
        registerFurniture("dark_oak_light_blue_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_light_gray_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("dark_oak_purple_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_PURPLE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_magenta_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("dark_oak_pink_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_PINK_CLASSIC_BED, 1);
        registerFurniture("dark_oak_brown_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_BROWN_CLASSIC_BED, 1);
        registerFurniture("dark_oak_gray_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_GRAY_CLASSIC_BED, 1);
        registerFurniture("dark_oak_black_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_BLACK_CLASSIC_BED, 1);
        registerFurniture("dark_oak_white_classic_bed", PaladinFurnitureModBlocksItems.DARK_OAK_WHITE_CLASSIC_BED, 1);
        registerFurniture("jungle_red_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_RED_CLASSIC_BED, 1);
        registerFurniture("jungle_orange_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_ORANGE_CLASSIC_BED, 1);
        registerFurniture("jungle_yellow_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_YELLOW_CLASSIC_BED, 1);
        registerFurniture("jungle_green_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_GREEN_CLASSIC_BED, 1);
        registerFurniture("jungle_lime_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_LIME_CLASSIC_BED, 1);
        registerFurniture("jungle_blue_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_BLUE_CLASSIC_BED, 1);
        registerFurniture("jungle_cyan_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_CYAN_CLASSIC_BED, 1);
        registerFurniture("jungle_light_blue_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("jungle_light_gray_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("jungle_purple_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_PURPLE_CLASSIC_BED, 1);
        registerFurniture("jungle_magenta_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("jungle_pink_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_PINK_CLASSIC_BED, 1);
        registerFurniture("jungle_brown_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_BROWN_CLASSIC_BED, 1);
        registerFurniture("jungle_gray_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_GRAY_CLASSIC_BED, 1);
        registerFurniture("jungle_black_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_BLACK_CLASSIC_BED, 1);
        registerFurniture("jungle_white_classic_bed", PaladinFurnitureModBlocksItems.JUNGLE_WHITE_CLASSIC_BED, 1);
        registerFurniture("warped_red_classic_bed", PaladinFurnitureModBlocksItems.WARPED_RED_CLASSIC_BED, 1);
        registerFurniture("warped_orange_classic_bed", PaladinFurnitureModBlocksItems.WARPED_ORANGE_CLASSIC_BED, 1);
        registerFurniture("warped_yellow_classic_bed", PaladinFurnitureModBlocksItems.WARPED_YELLOW_CLASSIC_BED, 1);
        registerFurniture("warped_green_classic_bed", PaladinFurnitureModBlocksItems.WARPED_GREEN_CLASSIC_BED, 1);
        registerFurniture("warped_lime_classic_bed", PaladinFurnitureModBlocksItems.WARPED_LIME_CLASSIC_BED, 1);
        registerFurniture("warped_blue_classic_bed", PaladinFurnitureModBlocksItems.WARPED_BLUE_CLASSIC_BED, 1);
        registerFurniture("warped_cyan_classic_bed", PaladinFurnitureModBlocksItems.WARPED_CYAN_CLASSIC_BED, 1);
        registerFurniture("warped_light_blue_classic_bed", PaladinFurnitureModBlocksItems.WARPED_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("warped_light_gray_classic_bed", PaladinFurnitureModBlocksItems.WARPED_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("warped_purple_classic_bed", PaladinFurnitureModBlocksItems.WARPED_PURPLE_CLASSIC_BED, 1);
        registerFurniture("warped_magenta_classic_bed", PaladinFurnitureModBlocksItems.WARPED_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("warped_pink_classic_bed", PaladinFurnitureModBlocksItems.WARPED_PINK_CLASSIC_BED, 1);
        registerFurniture("warped_brown_classic_bed", PaladinFurnitureModBlocksItems.WARPED_BROWN_CLASSIC_BED, 1);
        registerFurniture("warped_gray_classic_bed", PaladinFurnitureModBlocksItems.WARPED_GRAY_CLASSIC_BED, 1);
        registerFurniture("warped_black_classic_bed", PaladinFurnitureModBlocksItems.WARPED_BLACK_CLASSIC_BED, 1);
        registerFurniture("warped_white_classic_bed", PaladinFurnitureModBlocksItems.WARPED_WHITE_CLASSIC_BED, 1);
        registerFurniture("crimson_red_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_RED_CLASSIC_BED, 1);
        registerFurniture("crimson_orange_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_ORANGE_CLASSIC_BED, 1);
        registerFurniture("crimson_yellow_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_YELLOW_CLASSIC_BED, 1);
        registerFurniture("crimson_green_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_GREEN_CLASSIC_BED, 1);
        registerFurniture("crimson_lime_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_LIME_CLASSIC_BED, 1);
        registerFurniture("crimson_blue_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_BLUE_CLASSIC_BED, 1);
        registerFurniture("crimson_cyan_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_CYAN_CLASSIC_BED, 1);
        registerFurniture("crimson_light_blue_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("crimson_light_gray_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("crimson_purple_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_PURPLE_CLASSIC_BED, 1);
        registerFurniture("crimson_magenta_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("crimson_pink_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_PINK_CLASSIC_BED, 1);
        registerFurniture("crimson_brown_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_BROWN_CLASSIC_BED, 1);
        registerFurniture("crimson_gray_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_GRAY_CLASSIC_BED, 1);
        registerFurniture("crimson_black_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_BLACK_CLASSIC_BED, 1);
        registerFurniture("crimson_white_classic_bed", PaladinFurnitureModBlocksItems.CRIMSON_WHITE_CLASSIC_BED, 1);

        registerFurniture("oak_simple_bunk_ladder", PaladinFurnitureModBlocksItems.OAK_SIMPLE_BUNK_LADDER, true);
        registerFurniture("birch_simple_bunk_ladder", PaladinFurnitureModBlocksItems.BIRCH_SIMPLE_BUNK_LADDER, true);
        registerFurniture("spruce_simple_bunk_ladder", PaladinFurnitureModBlocksItems.SPRUCE_SIMPLE_BUNK_LADDER, true);
        registerFurniture("acacia_simple_bunk_ladder", PaladinFurnitureModBlocksItems.ACACIA_SIMPLE_BUNK_LADDER, true);
        registerFurniture("jungle_simple_bunk_ladder", PaladinFurnitureModBlocksItems.JUNGLE_SIMPLE_BUNK_LADDER, true);
        registerFurniture("dark_oak_simple_bunk_ladder", PaladinFurnitureModBlocksItems.DARK_OAK_SIMPLE_BUNK_LADDER, true);
        registerFurniture("crimson_simple_bunk_ladder", PaladinFurnitureModBlocksItems.CRIMSON_SIMPLE_BUNK_LADDER, true);
        registerFurniture("warped_simple_bunk_ladder", PaladinFurnitureModBlocksItems.WARPED_SIMPLE_BUNK_LADDER, true);

        registerFurniture("oak_log_stool", PaladinFurnitureModBlocksItems.OAK_LOG_STOOL, true);
        registerFurniture("birch_log_stool", PaladinFurnitureModBlocksItems.BIRCH_LOG_STOOL, true);
        registerFurniture("spruce_log_stool", PaladinFurnitureModBlocksItems.SPRUCE_LOG_STOOL, true);
        registerFurniture("acacia_log_stool", PaladinFurnitureModBlocksItems.ACACIA_LOG_STOOL, true);
        registerFurniture("jungle_log_stool", PaladinFurnitureModBlocksItems.JUNGLE_LOG_STOOL, true);
        registerFurniture("dark_oak_log_stool", PaladinFurnitureModBlocksItems.DARK_OAK_LOG_STOOL, true);
        registerFurniture("crimson_stem_stool", PaladinFurnitureModBlocksItems.CRIMSON_STEM_STOOL, true);
        registerFurniture("warped_stem_stool", PaladinFurnitureModBlocksItems.WARPED_STEM_STOOL, true);

        registerFurniture("oak_simple_stool", PaladinFurnitureModBlocksItems.OAK_SIMPLE_STOOL, true);
        registerFurniture("birch_simple_stool", PaladinFurnitureModBlocksItems.BIRCH_SIMPLE_STOOL, true);
        registerFurniture("spruce_simple_stool", PaladinFurnitureModBlocksItems.SPRUCE_SIMPLE_STOOL, true);
        registerFurniture("acacia_simple_stool", PaladinFurnitureModBlocksItems.ACACIA_SIMPLE_STOOL, true);
        registerFurniture("jungle_simple_stool", PaladinFurnitureModBlocksItems.JUNGLE_SIMPLE_STOOL, true);
        registerFurniture("dark_oak_simple_stool", PaladinFurnitureModBlocksItems.DARK_OAK_SIMPLE_STOOL, true);
        registerFurniture("crimson_simple_stool", PaladinFurnitureModBlocksItems.CRIMSON_SIMPLE_STOOL, true);
        registerFurniture("warped_simple_stool", PaladinFurnitureModBlocksItems.WARPED_SIMPLE_STOOL, true);
        registerFurniture("stripped_oak_simple_stool", PaladinFurnitureModBlocksItems.STRIPPED_OAK_SIMPLE_STOOL, true);
        registerFurniture("stripped_birch_simple_stool", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_SIMPLE_STOOL, true);
        registerFurniture("stripped_spruce_simple_stool", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_SIMPLE_STOOL, true);
        registerFurniture("stripped_acacia_simple_stool", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_SIMPLE_STOOL, true);
        registerFurniture("stripped_jungle_simple_stool", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_SIMPLE_STOOL, true);
        registerFurniture("stripped_dark_oak_simple_stool", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_SIMPLE_STOOL, true);
        registerFurniture("stripped_crimson_simple_stool", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_SIMPLE_STOOL, true);
        registerFurniture("stripped_warped_simple_stool", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_SIMPLE_STOOL, true);
        registerFurniture("quartz_simple_stool", PaladinFurnitureModBlocksItems.QUARTZ_SIMPLE_STOOL, true);
        registerFurniture("netherite_simple_stool", PaladinFurnitureModBlocksItems.NETHERITE_SIMPLE_STOOL, true);
        registerFurniture("light_wood_simple_stool", PaladinFurnitureModBlocksItems.LIGHT_WOOD_SIMPLE_STOOL, true);
        registerFurniture("dark_wood_simple_stool", PaladinFurnitureModBlocksItems.DARK_WOOD_SIMPLE_STOOL, true);
        registerFurniture("granite_simple_stool", PaladinFurnitureModBlocksItems.GRANITE_SIMPLE_STOOL, true);
        registerFurniture("calcite_simple_stool", PaladinFurnitureModBlocksItems.CALCITE_SIMPLE_STOOL, true);
        registerFurniture("andesite_simple_stool", PaladinFurnitureModBlocksItems.ANDESITE_SIMPLE_STOOL, true);
        registerFurniture("diorite_simple_stool", PaladinFurnitureModBlocksItems.DIORITE_SIMPLE_STOOL, true);
        registerFurniture("stone_simple_stool", PaladinFurnitureModBlocksItems.STONE_SIMPLE_STOOL, true);
        registerFurniture("blackstone_simple_stool", PaladinFurnitureModBlocksItems.BLACKSTONE_SIMPLE_STOOL, true);
        registerFurniture("deepslate_simple_stool", PaladinFurnitureModBlocksItems.DEEPSLATE_SIMPLE_STOOL, true);

        registerFurniture("oak_classic_stool", PaladinFurnitureModBlocksItems.OAK_CLASSIC_STOOL, true);
        registerFurniture("birch_classic_stool", PaladinFurnitureModBlocksItems.BIRCH_CLASSIC_STOOL, true);
        registerFurniture("spruce_classic_stool", PaladinFurnitureModBlocksItems.SPRUCE_CLASSIC_STOOL, true);
        registerFurniture("acacia_classic_stool", PaladinFurnitureModBlocksItems.ACACIA_CLASSIC_STOOL, true);
        registerFurniture("jungle_classic_stool", PaladinFurnitureModBlocksItems.JUNGLE_CLASSIC_STOOL, true);
        registerFurniture("dark_oak_classic_stool", PaladinFurnitureModBlocksItems.DARK_OAK_CLASSIC_STOOL, true);
        registerFurniture("crimson_classic_stool", PaladinFurnitureModBlocksItems.CRIMSON_CLASSIC_STOOL, true);
        registerFurniture("warped_classic_stool", PaladinFurnitureModBlocksItems.WARPED_CLASSIC_STOOL, true);
        registerFurniture("stripped_oak_classic_stool", PaladinFurnitureModBlocksItems.STRIPPED_OAK_CLASSIC_STOOL, true);
        registerFurniture("stripped_birch_classic_stool", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_CLASSIC_STOOL, true);
        registerFurniture("stripped_spruce_classic_stool", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_CLASSIC_STOOL, true);
        registerFurniture("stripped_acacia_classic_stool", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_CLASSIC_STOOL, true);
        registerFurniture("stripped_jungle_classic_stool", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_CLASSIC_STOOL, true);
        registerFurniture("stripped_dark_oak_classic_stool", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_CLASSIC_STOOL, true);
        registerFurniture("stripped_crimson_classic_stool", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_CLASSIC_STOOL, true);
        registerFurniture("stripped_warped_classic_stool", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_CLASSIC_STOOL, true);
        registerFurniture("quartz_classic_stool", PaladinFurnitureModBlocksItems.QUARTZ_CLASSIC_STOOL, true);
        registerFurniture("netherite_classic_stool", PaladinFurnitureModBlocksItems.NETHERITE_CLASSIC_STOOL, true);
        registerFurniture("light_wood_classic_stool", PaladinFurnitureModBlocksItems.LIGHT_WOOD_CLASSIC_STOOL, true);
        registerFurniture("dark_wood_classic_stool", PaladinFurnitureModBlocksItems.DARK_WOOD_CLASSIC_STOOL, true);
        registerFurniture("granite_classic_stool", PaladinFurnitureModBlocksItems.GRANITE_CLASSIC_STOOL, true);
        registerFurniture("calcite_classic_stool", PaladinFurnitureModBlocksItems.CALCITE_CLASSIC_STOOL, true);
        registerFurniture("andesite_classic_stool", PaladinFurnitureModBlocksItems.ANDESITE_CLASSIC_STOOL, true);
        registerFurniture("diorite_classic_stool", PaladinFurnitureModBlocksItems.DIORITE_CLASSIC_STOOL, true);
        registerFurniture("stone_classic_stool", PaladinFurnitureModBlocksItems.STONE_CLASSIC_STOOL, true);
        registerFurniture("blackstone_classic_stool", PaladinFurnitureModBlocksItems.BLACKSTONE_CLASSIC_STOOL, true);
        registerFurniture("deepslate_classic_stool", PaladinFurnitureModBlocksItems.DEEPSLATE_CLASSIC_STOOL, true);

        registerFurniture("oak_modern_stool", PaladinFurnitureModBlocksItems.OAK_MODERN_STOOL, true);
        registerFurniture("birch_modern_stool", PaladinFurnitureModBlocksItems.BIRCH_MODERN_STOOL, true);
        registerFurniture("spruce_modern_stool", PaladinFurnitureModBlocksItems.SPRUCE_MODERN_STOOL, true);
        registerFurniture("acacia_modern_stool", PaladinFurnitureModBlocksItems.ACACIA_MODERN_STOOL, true);
        registerFurniture("jungle_modern_stool", PaladinFurnitureModBlocksItems.JUNGLE_MODERN_STOOL, true);
        registerFurniture("dark_oak_modern_stool", PaladinFurnitureModBlocksItems.DARK_OAK_MODERN_STOOL, true);
        registerFurniture("crimson_modern_stool", PaladinFurnitureModBlocksItems.CRIMSON_MODERN_STOOL, true);
        registerFurniture("warped_modern_stool", PaladinFurnitureModBlocksItems.WARPED_MODERN_STOOL, true);
        registerFurniture("stripped_oak_modern_stool", PaladinFurnitureModBlocksItems.STRIPPED_OAK_MODERN_STOOL, true);
        registerFurniture("stripped_birch_modern_stool", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_MODERN_STOOL, true);
        registerFurniture("stripped_spruce_modern_stool", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_MODERN_STOOL, true);
        registerFurniture("stripped_acacia_modern_stool", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_MODERN_STOOL, true);
        registerFurniture("stripped_jungle_modern_stool", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_MODERN_STOOL, true);
        registerFurniture("stripped_dark_oak_modern_stool", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("stripped_crimson_modern_stool", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_MODERN_STOOL, true);
        registerFurniture("stripped_warped_modern_stool", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_MODERN_STOOL, true);
        registerFurniture("white_modern_stool", PaladinFurnitureModBlocksItems.WHITE_MODERN_STOOL, true);
        registerFurniture("gray_modern_stool", PaladinFurnitureModBlocksItems.GRAY_MODERN_STOOL, true);
        registerFurniture("dark_wood_modern_stool", PaladinFurnitureModBlocksItems.DARK_WOOD_MODERN_STOOL, true);
        registerFurniture("gray_dark_oak_modern_stool", PaladinFurnitureModBlocksItems.GRAY_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("light_gray_dark_oak_modern_stool", PaladinFurnitureModBlocksItems.LIGHT_GRAY_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("light_wood_modern_stool", PaladinFurnitureModBlocksItems.LIGHT_WOOD_MODERN_STOOL, true);
        registerFurniture("quartz_modern_stool", PaladinFurnitureModBlocksItems.QUARTZ_MODERN_STOOL, true);
        registerFurniture("netherite_modern_stool", PaladinFurnitureModBlocksItems.NETHERITE_MODERN_STOOL, true);
        registerFurniture("granite_modern_stool", PaladinFurnitureModBlocksItems.GRANITE_MODERN_STOOL, true);
        registerFurniture("calcite_modern_stool", PaladinFurnitureModBlocksItems.CALCITE_MODERN_STOOL, true);
        registerFurniture("andesite_modern_stool", PaladinFurnitureModBlocksItems.ANDESITE_MODERN_STOOL, true);
        registerFurniture("diorite_modern_stool", PaladinFurnitureModBlocksItems.DIORITE_MODERN_STOOL, true);
        registerFurniture("stone_modern_stool", PaladinFurnitureModBlocksItems.STONE_MODERN_STOOL, true);
        registerFurniture("blackstone_modern_stool", PaladinFurnitureModBlocksItems.BLACKSTONE_MODERN_STOOL, true);
        registerFurniture("deepslate_modern_stool", PaladinFurnitureModBlocksItems.DEEPSLATE_MODERN_STOOL, true);

        //Counter time
        registerFurniture("oak_kitchen_counter", PaladinFurnitureModBlocksItems.OAK_KITCHEN_COUNTER, true);
        registerFurniture("oak_kitchen_drawer", PaladinFurnitureModBlocksItems.OAK_KITCHEN_DRAWER, true);
        registerFurniture("oak_kitchen_cabinet", PaladinFurnitureModBlocksItems.OAK_KITCHEN_CABINET, true);
        registerFurniture("oak_kitchen_sink", PaladinFurnitureModBlocksItems.OAK_KITCHEN_SINK, true);
        registerFurniture("oak_kitchen_counter_oven", PaladinFurnitureModBlocksItems.OAK_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("oak_kitchen_wall_counter", PaladinFurnitureModBlocksItems.OAK_KITCHEN_WALL_COUNTER, true);
        registerFurniture("oak_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.OAK_KITCHEN_WALL_DRAWER, true);
        registerFurniture("oak_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.OAK_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("spruce_kitchen_counter", PaladinFurnitureModBlocksItems.SPRUCE_KITCHEN_COUNTER, true);
        registerFurniture("spruce_kitchen_drawer", PaladinFurnitureModBlocksItems.SPRUCE_KITCHEN_DRAWER, true);
        registerFurniture("spruce_kitchen_cabinet", PaladinFurnitureModBlocksItems.SPRUCE_KITCHEN_CABINET, true);
        registerFurniture("spruce_kitchen_sink", PaladinFurnitureModBlocksItems.SPRUCE_KITCHEN_SINK, true);
        registerFurniture("spruce_kitchen_counter_oven", PaladinFurnitureModBlocksItems.SPRUCE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("spruce_kitchen_wall_counter", PaladinFurnitureModBlocksItems.SPRUCE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("spruce_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.SPRUCE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("spruce_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.SPRUCE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("birch_kitchen_counter", PaladinFurnitureModBlocksItems.BIRCH_KITCHEN_COUNTER, true);
        registerFurniture("birch_kitchen_drawer", PaladinFurnitureModBlocksItems.BIRCH_KITCHEN_DRAWER, true);
        registerFurniture("birch_kitchen_cabinet", PaladinFurnitureModBlocksItems.BIRCH_KITCHEN_CABINET, true);
        registerFurniture("birch_kitchen_sink", PaladinFurnitureModBlocksItems.BIRCH_KITCHEN_SINK, true);
        registerFurniture("birch_kitchen_counter_oven", PaladinFurnitureModBlocksItems.BIRCH_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("birch_kitchen_wall_counter", PaladinFurnitureModBlocksItems.BIRCH_KITCHEN_WALL_COUNTER, true);
        registerFurniture("birch_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.BIRCH_KITCHEN_WALL_DRAWER, true);
        registerFurniture("birch_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.BIRCH_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("acacia_kitchen_counter", PaladinFurnitureModBlocksItems.ACACIA_KITCHEN_COUNTER, true);
        registerFurniture("acacia_kitchen_drawer", PaladinFurnitureModBlocksItems.ACACIA_KITCHEN_DRAWER, true);
        registerFurniture("acacia_kitchen_cabinet", PaladinFurnitureModBlocksItems.ACACIA_KITCHEN_CABINET, true);
        registerFurniture("acacia_kitchen_sink", PaladinFurnitureModBlocksItems.ACACIA_KITCHEN_SINK, true);
        registerFurniture("acacia_kitchen_counter_oven", PaladinFurnitureModBlocksItems.ACACIA_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("acacia_kitchen_wall_counter", PaladinFurnitureModBlocksItems.ACACIA_KITCHEN_WALL_COUNTER, true);
        registerFurniture("acacia_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.ACACIA_KITCHEN_WALL_DRAWER, true);
        registerFurniture("acacia_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.ACACIA_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("jungle_kitchen_counter", PaladinFurnitureModBlocksItems.JUNGLE_KITCHEN_COUNTER, true);
        registerFurniture("jungle_kitchen_drawer", PaladinFurnitureModBlocksItems.JUNGLE_KITCHEN_DRAWER, true);
        registerFurniture("jungle_kitchen_cabinet", PaladinFurnitureModBlocksItems.JUNGLE_KITCHEN_CABINET, true);
        registerFurniture("jungle_kitchen_sink", PaladinFurnitureModBlocksItems.JUNGLE_KITCHEN_SINK, true);
        registerFurniture("jungle_kitchen_counter_oven", PaladinFurnitureModBlocksItems.JUNGLE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("jungle_kitchen_wall_counter", PaladinFurnitureModBlocksItems.JUNGLE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("jungle_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.JUNGLE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("jungle_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.JUNGLE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("dark_oak_kitchen_counter", PaladinFurnitureModBlocksItems.DARK_OAK_KITCHEN_COUNTER, true);
        registerFurniture("dark_oak_kitchen_drawer", PaladinFurnitureModBlocksItems.DARK_OAK_KITCHEN_DRAWER, true);
        registerFurniture("dark_oak_kitchen_cabinet", PaladinFurnitureModBlocksItems.DARK_OAK_KITCHEN_CABINET, true);
        registerFurniture("dark_oak_kitchen_sink", PaladinFurnitureModBlocksItems.DARK_OAK_KITCHEN_SINK, true);
        registerFurniture("dark_oak_kitchen_counter_oven", PaladinFurnitureModBlocksItems.DARK_OAK_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("dark_oak_kitchen_wall_counter", PaladinFurnitureModBlocksItems.DARK_OAK_KITCHEN_WALL_COUNTER, true);
        registerFurniture("dark_oak_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.DARK_OAK_KITCHEN_WALL_DRAWER, true);
        registerFurniture("dark_oak_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.DARK_OAK_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("crimson_kitchen_counter", PaladinFurnitureModBlocksItems.CRIMSON_KITCHEN_COUNTER, true);
        registerFurniture("crimson_kitchen_drawer", PaladinFurnitureModBlocksItems.CRIMSON_KITCHEN_DRAWER, true);
        registerFurniture("crimson_kitchen_cabinet", PaladinFurnitureModBlocksItems.CRIMSON_KITCHEN_CABINET, true);
        registerFurniture("crimson_kitchen_sink", PaladinFurnitureModBlocksItems.CRIMSON_KITCHEN_SINK, true);
        registerFurniture("crimson_kitchen_counter_oven", PaladinFurnitureModBlocksItems.CRIMSON_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("crimson_kitchen_wall_counter", PaladinFurnitureModBlocksItems.CRIMSON_KITCHEN_WALL_COUNTER, true);
        registerFurniture("crimson_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.CRIMSON_KITCHEN_WALL_DRAWER, true);
        registerFurniture("crimson_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.CRIMSON_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("warped_kitchen_counter", PaladinFurnitureModBlocksItems.WARPED_KITCHEN_COUNTER, true);
        registerFurniture("warped_kitchen_drawer", PaladinFurnitureModBlocksItems.WARPED_KITCHEN_DRAWER, true);
        registerFurniture("warped_kitchen_cabinet", PaladinFurnitureModBlocksItems.WARPED_KITCHEN_CABINET, true);
        registerFurniture("warped_kitchen_sink", PaladinFurnitureModBlocksItems.WARPED_KITCHEN_SINK, true);
        registerFurniture("warped_kitchen_counter_oven", PaladinFurnitureModBlocksItems.WARPED_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("warped_kitchen_wall_counter", PaladinFurnitureModBlocksItems.WARPED_KITCHEN_WALL_COUNTER, true);
        registerFurniture("warped_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.WARPED_KITCHEN_WALL_DRAWER, true);
        registerFurniture("warped_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.WARPED_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("stripped_oak_kitchen_counter", PaladinFurnitureModBlocksItems.STRIPPED_OAK_KITCHEN_COUNTER, true);
        registerFurniture("stripped_oak_kitchen_drawer", PaladinFurnitureModBlocksItems.STRIPPED_OAK_KITCHEN_DRAWER, true);
        registerFurniture("stripped_oak_kitchen_cabinet", PaladinFurnitureModBlocksItems.STRIPPED_OAK_KITCHEN_CABINET, true);
        registerFurniture("stripped_oak_kitchen_sink", PaladinFurnitureModBlocksItems.STRIPPED_OAK_KITCHEN_SINK, true);
        registerFurniture("stripped_oak_kitchen_counter_oven", PaladinFurnitureModBlocksItems.STRIPPED_OAK_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("stripped_oak_kitchen_wall_counter", PaladinFurnitureModBlocksItems.STRIPPED_OAK_KITCHEN_WALL_COUNTER, true);
        registerFurniture("stripped_oak_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.STRIPPED_OAK_KITCHEN_WALL_DRAWER, true);
        registerFurniture("stripped_oak_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.STRIPPED_OAK_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("stripped_spruce_kitchen_counter", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_KITCHEN_COUNTER, true);
        registerFurniture("stripped_spruce_kitchen_drawer", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_KITCHEN_DRAWER, true);
        registerFurniture("stripped_spruce_kitchen_cabinet", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_KITCHEN_CABINET, true);
        registerFurniture("stripped_spruce_kitchen_sink", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_KITCHEN_SINK, true);
        registerFurniture("stripped_spruce_kitchen_counter_oven", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("stripped_spruce_kitchen_wall_counter", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("stripped_spruce_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("stripped_spruce_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("stripped_birch_kitchen_counter", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_KITCHEN_COUNTER, true);
        registerFurniture("stripped_birch_kitchen_drawer", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_KITCHEN_DRAWER, true);
        registerFurniture("stripped_birch_kitchen_cabinet", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_KITCHEN_CABINET, true);
        registerFurniture("stripped_birch_kitchen_sink", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_KITCHEN_SINK, true);
        registerFurniture("stripped_birch_kitchen_counter_oven", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("stripped_birch_kitchen_wall_counter", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_KITCHEN_WALL_COUNTER, true);
        registerFurniture("stripped_birch_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_KITCHEN_WALL_DRAWER, true);
        registerFurniture("stripped_birch_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("stripped_acacia_kitchen_counter", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_KITCHEN_COUNTER, true);
        registerFurniture("stripped_acacia_kitchen_drawer", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_KITCHEN_DRAWER, true);
        registerFurniture("stripped_acacia_kitchen_cabinet", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_KITCHEN_CABINET, true);
        registerFurniture("stripped_acacia_kitchen_sink", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_KITCHEN_SINK, true);
        registerFurniture("stripped_acacia_kitchen_counter_oven", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("stripped_acacia_kitchen_wall_counter", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_KITCHEN_WALL_COUNTER, true);
        registerFurniture("stripped_acacia_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_KITCHEN_WALL_DRAWER, true);
        registerFurniture("stripped_acacia_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("stripped_jungle_kitchen_counter", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_KITCHEN_COUNTER, true);
        registerFurniture("stripped_jungle_kitchen_drawer", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_KITCHEN_DRAWER, true);
        registerFurniture("stripped_jungle_kitchen_cabinet", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_KITCHEN_CABINET, true);
        registerFurniture("stripped_jungle_kitchen_sink", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_KITCHEN_SINK, true);
        registerFurniture("stripped_jungle_kitchen_counter_oven", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("stripped_jungle_kitchen_wall_counter", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("stripped_jungle_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("stripped_jungle_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("stripped_dark_oak_kitchen_counter", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_KITCHEN_COUNTER, true);
        registerFurniture("stripped_dark_oak_kitchen_drawer", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_KITCHEN_DRAWER, true);
        registerFurniture("stripped_dark_oak_kitchen_cabinet", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_KITCHEN_CABINET, true);
        registerFurniture("stripped_dark_oak_kitchen_sink", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_KITCHEN_SINK, true);
        registerFurniture("stripped_dark_oak_kitchen_counter_oven", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("stripped_dark_oak_kitchen_wall_counter", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_KITCHEN_WALL_COUNTER, true);
        registerFurniture("stripped_dark_oak_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_KITCHEN_WALL_DRAWER, true);
        registerFurniture("stripped_dark_oak_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("stripped_crimson_kitchen_counter", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_KITCHEN_COUNTER, true);
        registerFurniture("stripped_crimson_kitchen_drawer", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_KITCHEN_DRAWER, true);
        registerFurniture("stripped_crimson_kitchen_cabinet", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_KITCHEN_CABINET, true);
        registerFurniture("stripped_crimson_kitchen_sink", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_KITCHEN_SINK, true);
        registerFurniture("stripped_crimson_kitchen_counter_oven", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("stripped_crimson_kitchen_wall_counter", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_KITCHEN_WALL_COUNTER, true);
        registerFurniture("stripped_crimson_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_KITCHEN_WALL_DRAWER, true);
        registerFurniture("stripped_crimson_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("stripped_warped_kitchen_counter", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_KITCHEN_COUNTER, true);
        registerFurniture("stripped_warped_kitchen_drawer", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_KITCHEN_DRAWER, true);
        registerFurniture("stripped_warped_kitchen_cabinet", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_KITCHEN_CABINET, true);
        registerFurniture("stripped_warped_kitchen_sink", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_KITCHEN_SINK, true);
        registerFurniture("stripped_warped_kitchen_counter_oven", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("stripped_warped_kitchen_wall_counter", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_KITCHEN_WALL_COUNTER, true);
        registerFurniture("stripped_warped_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_KITCHEN_WALL_DRAWER, true);
        registerFurniture("stripped_warped_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("concrete_kitchen_counter", PaladinFurnitureModBlocksItems.CONCRETE_KITCHEN_COUNTER, true);
        registerFurniture("concrete_kitchen_drawer", PaladinFurnitureModBlocksItems.CONCRETE_KITCHEN_DRAWER, true);
        registerFurniture("concrete_kitchen_cabinet", PaladinFurnitureModBlocksItems.CONCRETE_KITCHEN_CABINET, true);
        registerFurniture("concrete_kitchen_sink", PaladinFurnitureModBlocksItems.CONCRETE_KITCHEN_SINK, true);
        registerFurniture("concrete_kitchen_counter_oven", PaladinFurnitureModBlocksItems.CONCRETE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("concrete_kitchen_wall_counter", PaladinFurnitureModBlocksItems.CONCRETE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("concrete_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.CONCRETE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("concrete_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.CONCRETE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("dark_concrete_kitchen_counter", PaladinFurnitureModBlocksItems.DARK_CONCRETE_KITCHEN_COUNTER, true);
        registerFurniture("dark_concrete_kitchen_drawer", PaladinFurnitureModBlocksItems.DARK_CONCRETE_KITCHEN_DRAWER, true);
        registerFurniture("dark_concrete_kitchen_cabinet", PaladinFurnitureModBlocksItems.DARK_CONCRETE_KITCHEN_CABINET, true);
        registerFurniture("dark_concrete_kitchen_sink", PaladinFurnitureModBlocksItems.DARK_CONCRETE_KITCHEN_SINK, true);
        registerFurniture("dark_concrete_kitchen_counter_oven", PaladinFurnitureModBlocksItems.DARK_CONCRETE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("dark_concrete_kitchen_wall_counter", PaladinFurnitureModBlocksItems.DARK_CONCRETE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("dark_concrete_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.DARK_CONCRETE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("dark_concrete_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.DARK_CONCRETE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("light_wood_kitchen_counter", PaladinFurnitureModBlocksItems.LIGHT_WOOD_KITCHEN_COUNTER, true);
        registerFurniture("light_wood_kitchen_drawer", PaladinFurnitureModBlocksItems.LIGHT_WOOD_KITCHEN_DRAWER, true);
        registerFurniture("light_wood_kitchen_cabinet", PaladinFurnitureModBlocksItems.LIGHT_WOOD_KITCHEN_CABINET, true);
        registerFurniture("light_wood_kitchen_sink", PaladinFurnitureModBlocksItems.LIGHT_WOOD_KITCHEN_SINK, true);
        registerFurniture("light_wood_kitchen_counter_oven", PaladinFurnitureModBlocksItems.LIGHT_WOOD_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("light_wood_kitchen_wall_counter", PaladinFurnitureModBlocksItems.LIGHT_WOOD_KITCHEN_WALL_COUNTER, true);
        registerFurniture("light_wood_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.LIGHT_WOOD_KITCHEN_WALL_DRAWER, true);
        registerFurniture("light_wood_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.LIGHT_WOOD_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("dark_wood_kitchen_counter", PaladinFurnitureModBlocksItems.DARK_WOOD_KITCHEN_COUNTER, true);
        registerFurniture("dark_wood_kitchen_drawer", PaladinFurnitureModBlocksItems.DARK_WOOD_KITCHEN_DRAWER, true);
        registerFurniture("dark_wood_kitchen_cabinet", PaladinFurnitureModBlocksItems.DARK_WOOD_KITCHEN_CABINET, true);
        registerFurniture("dark_wood_kitchen_sink", PaladinFurnitureModBlocksItems.DARK_WOOD_KITCHEN_SINK, true);
        registerFurniture("dark_wood_kitchen_counter_oven", PaladinFurnitureModBlocksItems.DARK_WOOD_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("dark_wood_kitchen_wall_counter", PaladinFurnitureModBlocksItems.DARK_WOOD_KITCHEN_WALL_COUNTER, true);
        registerFurniture("dark_wood_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.DARK_WOOD_KITCHEN_WALL_DRAWER, true);
        registerFurniture("dark_wood_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.DARK_WOOD_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("granite_kitchen_counter", PaladinFurnitureModBlocksItems.GRANITE_KITCHEN_COUNTER, true);
        registerFurniture("granite_kitchen_drawer", PaladinFurnitureModBlocksItems.GRANITE_KITCHEN_DRAWER, true);
        registerFurniture("granite_kitchen_cabinet", PaladinFurnitureModBlocksItems.GRANITE_KITCHEN_CABINET, true);
        registerFurniture("granite_kitchen_sink", PaladinFurnitureModBlocksItems.GRANITE_KITCHEN_SINK, true);
        registerFurniture("granite_kitchen_counter_oven", PaladinFurnitureModBlocksItems.GRANITE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("granite_kitchen_wall_counter", PaladinFurnitureModBlocksItems.GRANITE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("granite_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.GRANITE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("granite_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.GRANITE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("calcite_kitchen_counter", PaladinFurnitureModBlocksItems.CALCITE_KITCHEN_COUNTER, true);
        registerFurniture("calcite_kitchen_drawer", PaladinFurnitureModBlocksItems.CALCITE_KITCHEN_DRAWER, true);
        registerFurniture("calcite_kitchen_cabinet", PaladinFurnitureModBlocksItems.CALCITE_KITCHEN_CABINET, true);
        registerFurniture("calcite_kitchen_sink", PaladinFurnitureModBlocksItems.CALCITE_KITCHEN_SINK, true);
        registerFurniture("calcite_kitchen_counter_oven", PaladinFurnitureModBlocksItems.CALCITE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("calcite_kitchen_wall_counter", PaladinFurnitureModBlocksItems.CALCITE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("calcite_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.CALCITE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("calcite_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.CALCITE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("netherite_kitchen_counter", PaladinFurnitureModBlocksItems.NETHERITE_KITCHEN_COUNTER, true);
        registerFurniture("netherite_kitchen_drawer", PaladinFurnitureModBlocksItems.NETHERITE_KITCHEN_DRAWER, true);
        registerFurniture("netherite_kitchen_cabinet", PaladinFurnitureModBlocksItems.NETHERITE_KITCHEN_CABINET, true);
        registerFurniture("netherite_kitchen_sink", PaladinFurnitureModBlocksItems.NETHERITE_KITCHEN_SINK, true);
        registerFurniture("netherite_kitchen_counter_oven", PaladinFurnitureModBlocksItems.NETHERITE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("netherite_kitchen_wall_counter", PaladinFurnitureModBlocksItems.NETHERITE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("netherite_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.NETHERITE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("netherite_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.NETHERITE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("andesite_kitchen_counter", PaladinFurnitureModBlocksItems.ANDESITE_KITCHEN_COUNTER, true);
        registerFurniture("andesite_kitchen_drawer", PaladinFurnitureModBlocksItems.ANDESITE_KITCHEN_DRAWER, true);
        registerFurniture("andesite_kitchen_cabinet", PaladinFurnitureModBlocksItems.ANDESITE_KITCHEN_CABINET, true);
        registerFurniture("andesite_kitchen_sink", PaladinFurnitureModBlocksItems.ANDESITE_KITCHEN_SINK, true);
        registerFurniture("andesite_kitchen_counter_oven", PaladinFurnitureModBlocksItems.ANDESITE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("andesite_kitchen_wall_counter", PaladinFurnitureModBlocksItems.ANDESITE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("andesite_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.ANDESITE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("andesite_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.ANDESITE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("diorite_kitchen_counter", PaladinFurnitureModBlocksItems.DIORITE_KITCHEN_COUNTER, true);
        registerFurniture("diorite_kitchen_drawer", PaladinFurnitureModBlocksItems.DIORITE_KITCHEN_DRAWER, true);
        registerFurniture("diorite_kitchen_cabinet", PaladinFurnitureModBlocksItems.DIORITE_KITCHEN_CABINET, true);
        registerFurniture("diorite_kitchen_sink", PaladinFurnitureModBlocksItems.DIORITE_KITCHEN_SINK, true);
        registerFurniture("diorite_kitchen_counter_oven", PaladinFurnitureModBlocksItems.DIORITE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("diorite_kitchen_wall_counter", PaladinFurnitureModBlocksItems.DIORITE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("diorite_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.DIORITE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("diorite_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.DIORITE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("smooth_stone_kitchen_counter", PaladinFurnitureModBlocksItems.SMOOTH_STONE_KITCHEN_COUNTER, true);
        registerFurniture("smooth_stone_kitchen_drawer", PaladinFurnitureModBlocksItems.SMOOTH_STONE_KITCHEN_DRAWER, true);
        registerFurniture("smooth_stone_kitchen_cabinet", PaladinFurnitureModBlocksItems.SMOOTH_STONE_KITCHEN_CABINET, true);
        registerFurniture("smooth_stone_kitchen_sink", PaladinFurnitureModBlocksItems.SMOOTH_STONE_KITCHEN_SINK, true);
        registerFurniture("smooth_stone_kitchen_counter_oven", PaladinFurnitureModBlocksItems.SMOOTH_STONE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("smooth_stone_kitchen_wall_counter", PaladinFurnitureModBlocksItems.SMOOTH_STONE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("smooth_stone_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.SMOOTH_STONE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("smooth_stone_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.SMOOTH_STONE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("stone_kitchen_counter", PaladinFurnitureModBlocksItems.STONE_KITCHEN_COUNTER, true);
        registerFurniture("stone_kitchen_drawer", PaladinFurnitureModBlocksItems.STONE_KITCHEN_DRAWER, true);
        registerFurniture("stone_kitchen_cabinet", PaladinFurnitureModBlocksItems.STONE_KITCHEN_CABINET, true);
        registerFurniture("stone_kitchen_sink", PaladinFurnitureModBlocksItems.STONE_KITCHEN_SINK, true);
        registerFurniture("stone_kitchen_counter_oven", PaladinFurnitureModBlocksItems.STONE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("stone_kitchen_wall_counter", PaladinFurnitureModBlocksItems.STONE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("stone_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.STONE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("stone_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.STONE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("deepslate_tile_kitchen_counter", PaladinFurnitureModBlocksItems.DEEPSLATE_TILE_KITCHEN_COUNTER, true);
        registerFurniture("deepslate_tile_kitchen_drawer", PaladinFurnitureModBlocksItems.DEEPSLATE_TILE_KITCHEN_DRAWER, true);
        registerFurniture("deepslate_tile_kitchen_cabinet", PaladinFurnitureModBlocksItems.DEEPSLATE_TILE_KITCHEN_CABINET, true);
        registerFurniture("deepslate_tile_kitchen_sink", PaladinFurnitureModBlocksItems.DEEPSLATE_TILE_KITCHEN_SINK, true);
        registerFurniture("deepslate_tile_kitchen_counter_oven", PaladinFurnitureModBlocksItems.DEEPSLATE_TILE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("deepslate_tile_kitchen_wall_counter", PaladinFurnitureModBlocksItems.DEEPSLATE_TILE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("deepslate_tile_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.DEEPSLATE_TILE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("deepslate_tile_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.DEEPSLATE_TILE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("blackstone_kitchen_counter", PaladinFurnitureModBlocksItems.BLACKSTONE_KITCHEN_COUNTER, true);
        registerFurniture("blackstone_kitchen_drawer", PaladinFurnitureModBlocksItems.BLACKSTONE_KITCHEN_DRAWER, true);
        registerFurniture("blackstone_kitchen_cabinet", PaladinFurnitureModBlocksItems.BLACKSTONE_KITCHEN_CABINET, true);
        registerFurniture("blackstone_kitchen_sink", PaladinFurnitureModBlocksItems.BLACKSTONE_KITCHEN_SINK, true);
        registerFurniture("blackstone_kitchen_counter_oven", PaladinFurnitureModBlocksItems.BLACKSTONE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("blackstone_kitchen_wall_counter", PaladinFurnitureModBlocksItems.BLACKSTONE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("blackstone_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.BLACKSTONE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("blackstone_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.BLACKSTONE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("deepslate_kitchen_counter", PaladinFurnitureModBlocksItems.DEEPSLATE_KITCHEN_COUNTER, true);
        registerFurniture("deepslate_kitchen_drawer", PaladinFurnitureModBlocksItems.DEEPSLATE_KITCHEN_DRAWER, true);
        registerFurniture("deepslate_kitchen_cabinet", PaladinFurnitureModBlocksItems.DEEPSLATE_KITCHEN_CABINET, true);
        registerFurniture("deepslate_kitchen_sink", PaladinFurnitureModBlocksItems.DEEPSLATE_KITCHEN_SINK, true);
        registerFurniture("deepslate_kitchen_counter_oven", PaladinFurnitureModBlocksItems.DEEPSLATE_KITCHEN_COUNTER_OVEN, true);
        registerFurniture("deepslate_kitchen_wall_counter", PaladinFurnitureModBlocksItems.DEEPSLATE_KITCHEN_WALL_COUNTER, true);
        registerFurniture("deepslate_kitchen_wall_drawer", PaladinFurnitureModBlocksItems.DEEPSLATE_KITCHEN_WALL_DRAWER, true);
        registerFurniture("deepslate_kitchen_wall_small_drawer", PaladinFurnitureModBlocksItems.DEEPSLATE_KITCHEN_WALL_SMALL_DRAWER, true);

        registerFurniture("white_fridge", PaladinFurnitureModBlocksItems.WHITE_FRIDGE, true);
        registerFurniture("white_freezer", PaladinFurnitureModBlocksItems.WHITE_FREEZER, false);
        registerFurniture("gray_fridge", PaladinFurnitureModBlocksItems.GRAY_FRIDGE, true);
        registerFurniture("gray_freezer", PaladinFurnitureModBlocksItems.GRAY_FREEZER, false);
        registerFurniture("iron_fridge", PaladinFurnitureModBlocksItems.IRON_FRIDGE, true);
        registerFurniture("iron_freezer", PaladinFurnitureModBlocksItems.IRON_FREEZER, false);
        registerFurniture("xbox_fridge", PaladinFurnitureModBlocksItems.XBOX_FRIDGE, true);
        registerFurniture("iron_microwave", PaladinFurnitureModBlocksItems.IRON_MICROWAVE, true);
        registerFurniture("white_stove", PaladinFurnitureModBlocksItems.WHITE_STOVE, true);
        registerFurniture("white_oven_range_hood", PaladinFurnitureModBlocksItems.WHITE_OVEN_RANGEHOOD, true);
        registerFurniture("gray_stove", PaladinFurnitureModBlocksItems.GRAY_STOVE, true);
        registerFurniture("gray_oven_range_hood", PaladinFurnitureModBlocksItems.GRAY_OVEN_RANGEHOOD, true);
        registerFurniture("iron_stove", PaladinFurnitureModBlocksItems.IRON_STOVE, true);
        registerFurniture("iron_oven_range_hood", PaladinFurnitureModBlocksItems.IRON_OVEN_RANGEHOOD, true);
        registerFurniture("kitchen_stovetop", PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP, true);
        registerFurniture("basic_plate", PaladinFurnitureModBlocksItems.BASIC_PLATE, true);
        registerFurniture("basic_cutlery", PaladinFurnitureModBlocksItems.BASIC_CUTLERY, true);

        registerBlock("oak_herringbone_planks", PaladinFurnitureModBlocksItems.OAK_HERRINGBONE_PLANKS, true);
        registerBlock("spruce_herringbone_planks", PaladinFurnitureModBlocksItems.SPRUCE_HERRINGBONE_PLANKS, true);
        registerBlock("birch_herringbone_planks", PaladinFurnitureModBlocksItems.BIRCH_HERRINGBONE_PLANKS, true);
        registerBlock("jungle_herringbone_planks", PaladinFurnitureModBlocksItems.JUNGLE_HERRINGBONE_PLANKS, true);
        registerBlock("acacia_herringbone_planks", PaladinFurnitureModBlocksItems.ACACIA_HERRINGBONE_PLANKS, true);
        registerBlock("dark_oak_herringbone_planks", PaladinFurnitureModBlocksItems.DARK_OAK_HERRINGBONE_PLANKS, true);
        registerBlock("crimson_herringbone_planks", PaladinFurnitureModBlocksItems.CRIMSON_HERRINGBONE_PLANKS, true);
        registerBlock("warped_herringbone_planks", PaladinFurnitureModBlocksItems.WARPED_HERRINGBONE_PLANKS, true);

        registerBlock("raw_concrete", PaladinFurnitureModBlocksItems.RAW_CONCRETE, true);
        registerBlock("raw_concrete_powder", PaladinFurnitureModBlocksItems.RAW_CONCRETE_POWDER, true);
        registerBlock("iron_chain", PaladinFurnitureModBlocksItems.IRON_CHAIN, true);
        registerBlock("leather_block", PaladinFurnitureModBlocksItems.LEATHER_BLOCK, true);

        registerFurniture("gray_modern_pendant", PaladinFurnitureModBlocksItems.GRAY_MODERN_PENDANT, true);
        registerFurniture("white_modern_pendant", PaladinFurnitureModBlocksItems.WHITE_MODERN_PENDANT, true);
        registerFurniture("glass_modern_pendant", PaladinFurnitureModBlocksItems.GLASS_MODERN_PENDANT, true);
        registerFurniture("simple_light", PaladinFurnitureModBlocksItems.SIMPLE_LIGHT, true);

        PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM = new LightSwitchItem(PaladinFurnitureModBlocksItems.LIGHT_SWITCH, new Item.Settings().group(PaladinFurnitureMod.FURNITURE_GROUP));
        registerBlock("light_switch", PaladinFurnitureModBlocksItems.LIGHT_SWITCH, PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM);
        registerFurniture("basic_toilet", PaladinFurnitureModBlocksItems.BASIC_TOILET, true);
        registerFurniture("wall_toilet_paper", PaladinFurnitureModBlocksItems.WALL_TOILET_PAPER, true);
        registerFurniture("basic_sink", PaladinFurnitureModBlocksItems.BASIC_SINK, true);
        registerFurniture("basic_bathtub", PaladinFurnitureModBlocksItems.BASIC_BATHTUB, true);
        registerFurniture("basic_shower_head", PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD, true);
        PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE_ITEM = new ShowerHandleItem(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE, new Item.Settings().group(PaladinFurnitureMod.FURNITURE_GROUP));
        registerBlock("basic_shower_handle", PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE, PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE_ITEM);
        registerFurniture("trashcan", PaladinFurnitureModBlocksItems.TRASHCAN, true);
        registerFurniture("mesh_trashcan", PaladinFurnitureModBlocksItems.MESH_TRASHCAN, true);

        registerFurniture("white_shower_towel", PaladinFurnitureModBlocksItems.WHITE_SHOWER_TOWEL, true);
        registerFurniture("orange_shower_towel", PaladinFurnitureModBlocksItems.ORANGE_SHOWER_TOWEL, true);
        registerFurniture("magenta_shower_towel", PaladinFurnitureModBlocksItems.MAGENTA_SHOWER_TOWEL, true);
        registerFurniture("light_blue_shower_towel", PaladinFurnitureModBlocksItems.LIGHT_BLUE_SHOWER_TOWEL, true);
        registerFurniture("yellow_shower_towel", PaladinFurnitureModBlocksItems.YELLOW_SHOWER_TOWEL, true);
        registerFurniture("lime_shower_towel", PaladinFurnitureModBlocksItems.LIME_SHOWER_TOWEL, true);
        registerFurniture("pink_shower_towel", PaladinFurnitureModBlocksItems.PINK_SHOWER_TOWEL, true);
        registerFurniture("gray_shower_towel", PaladinFurnitureModBlocksItems.GRAY_SHOWER_TOWEL, true);
        registerFurniture("light_gray_shower_towel", PaladinFurnitureModBlocksItems.LIGHT_GRAY_SHOWER_TOWEL, true);
        registerFurniture("cyan_shower_towel", PaladinFurnitureModBlocksItems.CYAN_SHOWER_TOWEL, true);
        registerFurniture("purple_shower_towel", PaladinFurnitureModBlocksItems.PURPLE_SHOWER_TOWEL, true);
        registerFurniture("blue_shower_towel", PaladinFurnitureModBlocksItems.BLUE_SHOWER_TOWEL, true);
        registerFurniture("brown_shower_towel", PaladinFurnitureModBlocksItems.BROWN_SHOWER_TOWEL, true);
        registerFurniture("green_shower_towel", PaladinFurnitureModBlocksItems.GREEN_SHOWER_TOWEL, true);
        registerFurniture("red_shower_towel", PaladinFurnitureModBlocksItems.RED_SHOWER_TOWEL, true);
        registerFurniture("black_shower_towel", PaladinFurnitureModBlocksItems.BLACK_SHOWER_TOWEL, true);


        if (!isModLoaded("imm_ptl_core")) {
            PaladinFurnitureModBlocksItems.WHITE_MIRROR = new MirrorBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.WHITE).nonOpaque());
            registerFurniture("white_mirror", PaladinFurnitureModBlocksItems.WHITE_MIRROR, true);
        }
    }

    public static void registerBlock(String blockName, Block block, BlockItem item) {
        PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        registerBlockPlatformSpecific(blockName, block, false);
        registerItemPlatformSpecific(blockName, item);
    }

    public static void registerFurniture(String blockName, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockName, block, PaladinFurnitureMod.FURNITURE_GROUP);
        }
        registerBlockPlatformSpecific(blockName, block, false);
    }

    public static void registerFurniture(String blockName, Block block, int count) {
        PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        registerBlockPlatformSpecific(blockName, block, false);
        registerItemPlatformSpecific(blockName, new BlockItem(block, new Item.Settings().group(PaladinFurnitureMod.FURNITURE_GROUP).maxCount(count)));
    }

    public static void registerBlock(String blockName, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockName, block, ItemGroup.BUILDING_BLOCKS);
        }
        registerBlockPlatformSpecific(blockName, block, false);
    }

    @ExpectPlatform
    public static void registerBlockItemPlatformSpecific(String itemName, Block block, ItemGroup group) {
        throw new UnsupportedOperationException();
    }

    @ExpectPlatform
    public static void registerBlockPlatformSpecific(String blockName, Block block, boolean registerItem) {
        throw new UnsupportedOperationException();
    }

    @ExpectPlatform
    public static void registerItemPlatformSpecific(String itemName, Item item) {
        throw new UnsupportedOperationException();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new UnsupportedOperationException();
    }

    public static void registerCommonItems() {
        //Dye Kits
        registerItemPlatformSpecific("furniture_book", PaladinFurnitureModBlocksItems.FURNITURE_BOOK);
        registerItemPlatformSpecific("dye_kit_red", PaladinFurnitureModBlocksItems.DYE_KIT_RED);
        registerItemPlatformSpecific("dye_kit_orange", PaladinFurnitureModBlocksItems.DYE_KIT_ORANGE);
        registerItemPlatformSpecific("dye_kit_yellow", PaladinFurnitureModBlocksItems.DYE_KIT_YELLOW);
        registerItemPlatformSpecific("dye_kit_lime", PaladinFurnitureModBlocksItems.DYE_KIT_LIME);
        registerItemPlatformSpecific("dye_kit_green", PaladinFurnitureModBlocksItems.DYE_KIT_GREEN);
        registerItemPlatformSpecific("dye_kit_cyan", PaladinFurnitureModBlocksItems.DYE_KIT_CYAN);
        registerItemPlatformSpecific("dye_kit_light_blue", PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_BLUE);
        registerItemPlatformSpecific("dye_kit_blue", PaladinFurnitureModBlocksItems.DYE_KIT_BLUE);
        registerItemPlatformSpecific("dye_kit_purple", PaladinFurnitureModBlocksItems.DYE_KIT_PURPLE);
        registerItemPlatformSpecific("dye_kit_magenta", PaladinFurnitureModBlocksItems.DYE_KIT_MAGENTA);
        registerItemPlatformSpecific("dye_kit_pink", PaladinFurnitureModBlocksItems.DYE_KIT_PINK);
        registerItemPlatformSpecific("dye_kit_brown", PaladinFurnitureModBlocksItems.DYE_KIT_BROWN);
        registerItemPlatformSpecific("dye_kit_black", PaladinFurnitureModBlocksItems.DYE_KIT_BLACK);
        registerItemPlatformSpecific("dye_kit_gray", PaladinFurnitureModBlocksItems.DYE_KIT_GRAY);
        registerItemPlatformSpecific("dye_kit_light_gray", PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_GRAY);
        registerItemPlatformSpecific("dye_kit_white", PaladinFurnitureModBlocksItems.DYE_KIT_WHITE);
    }
}
