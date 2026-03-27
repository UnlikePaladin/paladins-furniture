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
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BlockItemRegistryImpl {
    public static Map<String, Supplier<Item>> items = new LinkedHashMap<>();
    public static List<Block> blocks = new ArrayList<>();

    public static void registerItemPlatformSpecific(String itemId, Supplier<Item> item) {
        items.put(itemId,item);
    }

    public static void registerBlockPlatformSpecific(String blockId, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockId, block, CreativeModeTab.TAB_BUILDING_BLOCKS);
        }
        block.setRegistryName(blockId);
        blocks.add(block);
    }

    public static void registerBlockItemPlatformSpecific(String itemName, Block block, CreativeModeTab group) {
        if (block.defaultBlockState().getMaterial() == Material.WOOD || block.defaultBlockState().getMaterial() == Material.WOOL) {
            registerItemPlatformSpecific(itemName, () -> new BlockItem(block, new Item.Properties().tab(group)) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 300;
                }
            });
        }
        registerItemPlatformSpecific(itemName, () -> new BlockItem(block, new Item.Properties().tab(group)));
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
