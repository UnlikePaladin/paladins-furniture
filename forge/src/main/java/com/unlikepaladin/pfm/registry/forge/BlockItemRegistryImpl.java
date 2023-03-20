package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlockItemRegistryImpl {
    public static Map<String, Item> items = new LinkedHashMap<>();
    public static List<Block> blocks = new ArrayList<>();

    public static void registerItemPlatformSpecific(String itemId, Item item) {
        item.setRegistryName(itemId);
        items.put(itemId,item);
    }

    public static void registerBlockPlatformSpecific(String blockId, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockId, block, ItemGroup.BUILDING_BLOCKS);
        }
        block.setRegistryName(blockId);
        blocks.add(block);
    }

    public static void registerBlockItemPlatformSpecific(String itemName, Block block, ItemGroup group) {
        if (block.getDefaultState().getMaterial() == Material.WOOD || block.getDefaultState().getMaterial() == Material.WOOL) {
            registerItemPlatformSpecific(itemName, new BlockItem(block, new Item.Settings().group(group)) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 300;
                }
            });
        }
        registerItemPlatformSpecific(itemName, new BlockItem(block, new Item.Settings().group(group)));
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
