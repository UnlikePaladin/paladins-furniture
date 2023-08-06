package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.function.Supplier;

public class BlockItemRegistryImpl {

    public static void registerItemPlatformSpecific(String itemName, Supplier<Item> itemSupplier, Pair<String, ItemGroup> group) {
        Item item = itemSupplier.get();
        Registry.register(Registries.ITEM, new Identifier(PaladinFurnitureMod.MOD_ID, itemName), item);
        if (!PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.containsKey(group)) {
            PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.put(group, new ArrayList<>());
        }
        PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.get(group).add(item);
        ItemGroupEvents.modifyEntriesEvent(group.getRight()).register(entries -> entries.add(item));
    }

    public static void registerBlockItemPlatformSpecific(String itemName, Block block, Pair<String, ItemGroup> group) {
        PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        registerItemPlatformSpecific(itemName, () -> new BlockItem(block, new FabricItemSettings()), group);
        if (block.getDefaultState().getMaterial() == Material.WOOD || block.getDefaultState().getMaterial() == Material.WOOL) {
            FlammableBlockRegistry.getDefaultInstance().add(block, 20, 5);
            FuelRegistry.INSTANCE.add(block, 300);
        }
    }

    public static void registerBlockPlatformSpecific(String blockName, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockName, block, new Pair<>("building_blocks", ItemGroups.BUILDING_BLOCKS));
        }
        Registry.register(Registries.BLOCK, new Identifier(PaladinFurnitureMod.MOD_ID, blockName),  block);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
