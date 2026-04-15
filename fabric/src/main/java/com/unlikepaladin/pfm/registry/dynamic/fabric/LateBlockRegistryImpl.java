package com.unlikepaladin.pfm.registry.dynamic.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.AbstractSittableBlock;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Supplier;

public class LateBlockRegistryImpl {

    public static <T extends Block> T registerLateBlock(String blockName, Supplier<T> blockSupplier, boolean registerItem, Tuple<String, CreativeModeTab> group) {
        T block = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, blockName), blockSupplier.get());
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerLateBlockItem(blockName, block, group);
        }
        return block;
    }
    public static void registerLateBlockItem(String itemName, Block block, Tuple<String, CreativeModeTab> group) {
        registerLateItem(itemName, () -> new BlockItem(block, new Item.Properties().setId(LateBlockRegistry.getItemRegistryKey(itemName)).useBlockDescriptionPrefix()), group);
        if (AbstractSittableBlock.isWoodBased(block.defaultBlockState())) {
            FlammableBlockRegistry.getDefaultInstance().add(block, 20, 5);
            FuelRegistryEvents.BUILD.register((builder, context) -> {
                builder.add(block, 300);
            });

        }
    }
    public static void registerLateItem(String itemName, Supplier<Item> itemSup, Tuple<String, CreativeModeTab> group) {
        Item item = itemSup.get();
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, itemName), item);
        if (!PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.containsKey(group)) {
            PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.put(group, new LinkedHashSet<>());
        }
        PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.get(group).add(item);
        if (item == PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM) {
            ItemGroupEvents.modifyEntriesEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group.getB()).get()).register(entries -> {
                List<ItemStack> stacks = new ArrayList<>();
                for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                    if (!variant.isEnabled(entries.getEnabledFeatures())) continue;

                    for (DyeColor color : DyeColor.values()) {
                        ItemStack stack = new ItemStack(item);
                        stack.set(PFMComponents.VARIANT_COMPONENT, variant.getIdentifier());
                        stack.set(PFMComponents.COLOR_COMPONENT, color);
                        stacks.add(stack);
                    }
                }
                entries.acceptAll(stacks);
            } );
        } else if (item == PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM) {
            ItemGroupEvents.modifyEntriesEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group.getB()).get()).register(entries -> {
                List<ItemStack> stacks = new ArrayList<>();
                for (DyeColor color : DyeColor.values()) {
                    ItemStack stack = new ItemStack(item);
                    stack.set(PFMComponents.COLOR_COMPONENT, color);
                    stacks.add(stack);
                }
                entries.acceptAll(stacks);
            } );
        } else {
            ItemGroupEvents.modifyEntriesEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group.getB()).get()).register(entries -> entries.accept(item));
        }
    }

    public static <T extends Block> T registerLateBlockClassic(String blockName, T block, boolean registerItem, Tuple<String, CreativeModeTab> group) {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, blockName), block);
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerLateBlockItem(blockName, block, group);
        }
        return block;
    }
}
