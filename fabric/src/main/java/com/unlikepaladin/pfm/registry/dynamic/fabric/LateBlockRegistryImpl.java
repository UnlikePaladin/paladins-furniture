package com.unlikepaladin.pfm.registry.dynamic.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.AbstractSittableBlock;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import com.unlikepaladin.pfm.utilities.Tuple;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.block.Block;

import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Supplier;

public class LateBlockRegistryImpl {

    public static <T extends Block> T registerLateBlock(String blockName, Supplier<T> blockSupplier, boolean registerItem, Tuple<String, CreativeModeTab> group) {
        T block = Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, blockName), blockSupplier.get());
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
            DefaultItemComponentEvents.MODIFY.register((modifyContext) -> {
                modifyContext.modify(block.asItem(), builder -> {
                    builder.set(DataComponents.COOKING_FUEL,
                            new CookingFuel(new ResolvableInt.Constant(300), new ResolvableFloat.Constant(1)));
                });

            });

        }
    }
    public static void registerLateItem(String itemName, Supplier<Item> itemSup, Tuple<String, CreativeModeTab> group) {
        Item item = itemSup.get();
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, itemName), item);
        if (!PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.containsKey(group)) {
            PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.put(group, new LinkedHashSet<>());
        }
        PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.get(group).add(item);
        if (item == PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM) {
            CreativeModeTabEvents.modifyOutputEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group.getB()).get()).register(entries -> {
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
            CreativeModeTabEvents.modifyOutputEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group.getB()).get()).register(entries -> {
                List<ItemStack> stacks = new ArrayList<>();
                for (DyeColor color : DyeColor.values()) {
                    ItemStack stack = new ItemStack(item);
                    stack.set(PFMComponents.COLOR_COMPONENT, color);
                    stacks.add(stack);
                }
                entries.acceptAll(stacks);
            } );
        } else {
            CreativeModeTabEvents.modifyOutputEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group.getB()).get()).register(entries -> entries.accept(item));
        }
    }

    public static <T extends Block> T registerLateBlockClassic(String blockName, T block, boolean registerItem, Tuple<String, CreativeModeTab> group) {
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, blockName), block);
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerLateBlockItem(blockName, block, group);
        }
        return block;
    }
}
