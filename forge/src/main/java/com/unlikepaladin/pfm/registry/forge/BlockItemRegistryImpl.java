package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Pair;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class BlockItemRegistryImpl {
    public static Map<String, Supplier<Item>> items = new LinkedHashMap<>();
    public static Map<String, Block> blocks = new HashMap<>();
    public static Map<String, Pair<String, ItemGroup>> itemNameToGroup = new HashMap<>();

    public static void registerItemPlatformSpecific(String itemId, Supplier<Item> item, Pair<String, ItemGroup> group) {
        items.put(itemId,item);
        itemNameToGroup.put(itemId, group);
    }

    public static void registerBlockPlatformSpecific(String blockId, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockId, block, new Pair<>("building_blocks", CreativeModeTab.TAB_BUILDING_BLOCKS));
        }
        blocks.put(blockId, block);
    }

    public static void registerBlockItemPlatformSpecific(String itemName, Block block, Pair<String, CreativeModeTab> group) {
        if (block.getDefaultState().getMaterial() == Material.WOOD || block.getDefaultState().getMaterial() == Material.WOOL) {
            registerItemPlatformSpecific(itemName, () -> new BlockItem(block, new Item.Settings()) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 300;
                }
            }, group);
        }
        registerItemPlatformSpecific(itemName, () -> new BlockItem(block, new Item.Properties()), group);
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
