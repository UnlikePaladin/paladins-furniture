package com.unlikepaladin.pfm.registry.dynamic.forge;

import com.google.common.collect.ImmutableSet;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.forge.PaladinFurnitureModForge;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

public class LateBlockRegistryImpl {

    public static List<Block> blocks = new ArrayList<>();
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
        block.setRegistryName(blockId);
        blocks.add(block);
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


    public static void registerBlocks(RegistryEvent.Register<Block> blockRegisterEvent) {
        try {
            LateBlockRegistry.registerBlocks();
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        blockRegisterEvent.getRegistry().registerAll(blocks.toArray(new Block[0]));
        PaladinFurnitureModForge.replaceHomePOIStates();
    }

    public static void registerItems(RegistryEvent.Register<Item> itemRegisterEvent) {
        items.forEach((itemName, itemSup) -> {
            Item item = itemSup.get();
            item.setRegistryName(itemName);
            itemRegisterEvent.getRegistry().register(item);
        });
    }

    public static <T extends Block> T registerLateBlockClassic(String blockId, T block, boolean registerItem, ItemGroup group) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockId, block, group);
        }
        block.setRegistryName(blockId);
        blocks.add(block);
        return block;
    }
}
