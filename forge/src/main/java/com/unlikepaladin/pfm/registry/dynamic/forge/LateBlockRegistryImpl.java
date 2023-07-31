package com.unlikepaladin.pfm.registry.dynamic.forge;

import com.google.common.collect.ImmutableSet;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.registry.forge.BlockItemRegistryImpl;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.enums.BedPart;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

public class LateBlockRegistryImpl {

    public static Map<String, Block> blocks = new LinkedHashMap<>();
    public static Map<String, Supplier<Item>> items = new LinkedHashMap<>();
    public static void registerLateItem(String itemName, Supplier<Item> itemSup) {
        items.put(itemName, itemSup);
    }

    public static <T extends Block> T registerLateBlock(String blockId, Supplier<T> blockSup, boolean registerItem, ItemGroup group) {
        T block = blockSup.get();
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockId, block, group);
        }
        blocks.put(blockId, block);
        return block;
    }

    public static void registerBlockItemPlatformSpecific(String itemName, Block block, ItemGroup group) {
        if (block.getDefaultState().getMaterial() == Material.WOOD || block.getDefaultState().getMaterial() == Material.WOOL) {
            registerLateItem(itemName, () -> new BlockItem(block, new Item.Settings().group(group)) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 300;
                }
            });
        }
        registerLateItem(itemName, () -> new BlockItem(block, new Item.Settings().group(group)));
    }


    public static void registerBlocks(IForgeRegistry<Block> blockRegisterEvent) {
        try {
            LateBlockRegistry.registerBlocks();
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        blocks.forEach(blockRegisterEvent::register);

        Set<BlockState> originalBedStates = ForgeRegistries.POI_TYPES.getValue(PointOfInterestTypes.HOME.getValue()).blockStates();
        Set<BlockState> addedBedStates = Arrays.stream(PaladinFurnitureModBlocksItems.getBeds()).flatMap(block -> block.getStateManager().getStates().stream().filter(state -> state.get(SimpleBedBlock.PART) == BedPart.HEAD)).collect(ImmutableSet.toImmutableSet());
        Set<BlockState> newBedStates = new HashSet<>();
        newBedStates.addAll(originalBedStates);
        newBedStates.addAll(addedBedStates);
        PointOfInterestType pointOfInterestType = new PointOfInterestType(newBedStates, 1, 1);
        ForgeRegistries.POI_TYPES.register("minecraft:home", pointOfInterestType);
        PointOfInterestTypes.HOME = ForgeRegistries.POI_TYPES.getHolder(pointOfInterestType).get().getKey().get();
    }

    public static void registerItems(IForgeRegistry<Item> itemIForgeRegistry) {
        items.forEach((itemName, itemSup) -> {
            Item item = itemSup.get();
            itemIForgeRegistry.register(itemName, item);
        });
    }

    public static <T extends Block> T registerLateBlockClassic(String blockId, T block, boolean registerItem, ItemGroup group) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockId, block, group);
        }
        blocks.put(blockId, block);
        return block;
    }
}
