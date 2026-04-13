package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.AbstractSittableBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class BlockItemRegistryImpl {

    public static void registerItemPlatformSpecific(String itemName, Supplier<Item> itemSupplier, Tuple<String, CreativeModeTab> group) {
        Item item = itemSupplier.get();
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, itemName), item);
        if (!PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.containsKey(group)) {
            PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.put(group, new LinkedHashSet<>());
        }
        PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.get(group).add(item);
        ItemGroupEvents.modifyEntriesEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group.getB()).get()).register(entries -> entries.accept(item));
    }

    public static void registerBlockItemPlatformSpecific(String itemName, Block block, Tuple<String, CreativeModeTab> group) {
        PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        registerItemPlatformSpecific(itemName, () -> new BlockItem(block, new Item.Properties().registryKey(LateBlockRegistry.getItemRegistryKey(itemName)).useBlockPrefixedTranslationKey()), group);
        if (AbstractSittableBlock.isWoodBased(block.defaultBlockState())) {
            FlammableBlockRegistry.getDefaultInstance().add(block, 20, 5);
            FuelRegistryEvents.BUILD.register((builder, context) -> {
                builder.add(block, 300);
            });
        }
    }

    public static void registerBlockPlatformSpecific(String blockName, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockName, block, new Tuple<>("building_blocks", BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.BUILDING_BLOCKS)));
        }
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, blockName),  block);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
