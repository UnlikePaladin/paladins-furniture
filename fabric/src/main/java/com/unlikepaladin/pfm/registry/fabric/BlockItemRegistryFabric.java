package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.items.fabric.FurnitureGuideBookImpl;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

public class BlockItemRegistryFabric {

    public static void registerBlocks(){
        //Block Registry
        BlockItemRegistry.registerCommonBlocks();
    }

    public static void registerItems() {
        PaladinFurnitureModBlocksItems.FURNITURE_BOOK = new FurnitureGuideBookImpl(new Item.Properties().tab(PaladinFurnitureMod.FURNITURE_GROUP).rarity(Rarity.RARE).stacksTo(1));
        BlockItemRegistry.registerCommonItems();
    }

}
