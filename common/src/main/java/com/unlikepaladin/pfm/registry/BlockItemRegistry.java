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
