package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockItemRegistryImpl {

    public static void registerItemPlatformSpecific(String itemName, Item item) {
        Registry.register(Registry.ITEM, new Identifier(PaladinFurnitureMod.MOD_ID, itemName), item);
    }

    public static void registerBlockItemPlatformSpecific(String itemName, Block block, ItemGroup group) {
        PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        registerItemPlatformSpecific(itemName, new BlockItem(block, new FabricItemSettings().group(group)));

        if (block.getDefaultState().getMaterial() == Material.WOOD || block.getDefaultState().getMaterial() == Material.WOOL) {
            FlammableBlockRegistry.getDefaultInstance().add(block, 20, 5);
            FuelRegistry.INSTANCE.add(block, 300);
        }
    }

    public static void registerBlockPlatformSpecific(String blockName, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockName, block, ItemGroup.BUILDING_BLOCKS);
        }
        Registry.register(Registry.BLOCK, new Identifier(PaladinFurnitureMod.MOD_ID, blockName),  block);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
