package com.unlikepaladin.pfm.registry.dynamic.forge;

import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.forge.BlockItemRegistryImpl;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LateBlockRegistryImpl {

    public static List<Block> blocks = new ArrayList<>();
    public static Map<String, Item> items = new LinkedHashMap<>();
    public static Item registerLateItem(String itemName, Supplier<Item> itemSup) {
        Item item = itemSup.get();
        item.setRegistryName(itemName);
        items.put(itemName, item);
        return item;
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
        blockRegisterEvent.getRegistry().registerAll(blocks.toArray(new Block[0]));
    }

    public static void registerItems(RegistryEvent.Register<Item> blockRegisterEvent) {
        blockRegisterEvent.getRegistry().registerAll(items.values().toArray(new Item[0]));
    }
}
