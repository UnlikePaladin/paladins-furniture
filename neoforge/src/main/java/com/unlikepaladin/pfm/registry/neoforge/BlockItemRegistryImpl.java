package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.AbstractSittableBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import com.unlikepaladin.pfm.utilities.Tuple;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class BlockItemRegistryImpl {
    public static Map<String, Supplier<Item>> items = new LinkedHashMap<>();
    public static Map<String, Block> blocks = new HashMap<>();
    public static Map<String, Tuple<String, CreativeModeTab>> itemNameToGroup = new HashMap<>();

    public static void registerItemPlatformSpecific(String itemId, Supplier<Item> item, Tuple<String, CreativeModeTab> group) {
        items.put(itemId,item);
        itemNameToGroup.put(itemId, group);
    }

    public static void registerBlockPlatformSpecific(String blockId, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockId, block, new Tuple<>("building_blocks", BuiltInRegistries.CREATIVE_MODE_TAB.getValue(CreativeModeTabs.BUILDING_BLOCKS)));
        }
        blocks.put(blockId, block);
    }

    public static void registerBlockItemPlatformSpecific(String itemName, Block block, Tuple<String, CreativeModeTab> group) {
        if (AbstractSittableBlock.isWoodBased(block.defaultBlockState())) {
            registerItemPlatformSpecific(itemName, () -> new BlockItem(block, new Item.Properties().component(DataComponents.COOKING_FUEL, new CookingFuel(new ResolvableInt.Constant(300), new ResolvableFloat.Constant(1.0F))).useBlockDescriptionPrefix().setId(LateBlockRegistry.getItemRegistryKey(itemName))), group);
        }
        registerItemPlatformSpecific(itemName, () -> new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(LateBlockRegistry.getItemRegistryKey(itemName))), group);
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
