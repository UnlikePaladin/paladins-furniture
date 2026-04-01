package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.items.LightSwitchItem;
import com.unlikepaladin.pfm.items.ShowerHandleItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class BlockItemRegistry {
    public static void registerCommonBlocks() {
        registerFurniture("working_table", PaladinFurnitureModBlocksItems.WORKING_TABLE, true);

        registerBlock("raw_concrete", PaladinFurnitureModBlocksItems.RAW_CONCRETE, true);
        registerBlock("raw_concrete_powder", PaladinFurnitureModBlocksItems.RAW_CONCRETE_POWDER, true);
        registerBlock("iron_chain", PaladinFurnitureModBlocksItems.IRON_CHAIN, true);
        registerBlock("leather_block", PaladinFurnitureModBlocksItems.LEATHER_BLOCK, true);
    }

    public static void registerBlock(String blockName, Block block, BlockItem item, Tuple<String, CreativeModeTab> group) {
        PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        registerBlockPlatformSpecific(blockName, block, false);
        registerItemPlatformSpecific(blockName, ()-> item, group);
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
        registerItemPlatformSpecific(blockName, () -> new BlockItem(block, new Item.Properties().stacksTo(count)), PaladinFurnitureMod.FURNITURE_GROUP);
    }

    public static void registerBlock(String blockName, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockName, block, new Pair<>("building_blocks", BuiltInRegistries.ITEM_GROUP.get(CreativeModeTabs.BUILDING_BLOCKS)));
        }
        registerBlockPlatformSpecific(blockName, block, false);
    }

    @ExpectPlatform
    public static void registerBlockItemPlatformSpecific(String itemName, Block block, Tuple<String, CreativeModeTab> group) {
        throw new UnsupportedOperationException();
    }

    @ExpectPlatform
    public static void registerBlockPlatformSpecific(String blockName, Block block, boolean registerItem) {
        throw new UnsupportedOperationException();
    }

    @ExpectPlatform
    public static void registerItemPlatformSpecific(String itemName, Supplier<Item> itemSupplier, Tuple<String, CreativeModeTab> group) {
        throw new UnsupportedOperationException();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new UnsupportedOperationException();
    }

    public static void registerCommonItems() {
        //Dye Kits
        registerItemPlatformSpecific("furniture_book", ()-> PaladinFurnitureModBlocksItems.FURNITURE_BOOK, PaladinFurnitureMod.FURNITURE_GROUP);
        registerItemPlatformSpecific("dye_kit_red", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_RED, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_orange", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_ORANGE, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_yellow", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_YELLOW, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_lime", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_LIME, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_green", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_GREEN, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_cyan", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_CYAN, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_light_blue", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_BLUE, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_blue", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_BLUE, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_purple", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_PURPLE, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_magenta", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_MAGENTA, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_pink", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_PINK, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_brown", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_BROWN, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_black", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_BLACK, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_gray", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_GRAY, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_light_gray", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_GRAY, PaladinFurnitureMod.DYE_KITS);
        registerItemPlatformSpecific("dye_kit_white", ()-> PaladinFurnitureModBlocksItems.DYE_KIT_WHITE, PaladinFurnitureMod.DYE_KITS);
    }
}
