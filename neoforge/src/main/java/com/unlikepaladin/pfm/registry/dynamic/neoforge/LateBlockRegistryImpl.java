package com.unlikepaladin.pfm.registry.dynamic.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.AbstractSittableBlock;
import com.unlikepaladin.pfm.neoforge.PaladinFurnitureModNeoForge;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.registry.neoforge.BlockItemRegistryImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

@EventBusSubscriber(modid = "pfm", bus = EventBusSubscriber.Bus.MOD)
public class LateBlockRegistryImpl {

    public static Map<String, Block> blocks = new LinkedHashMap<>();
    public static Map<String, Supplier<Item>> items = new LinkedHashMap<>();
    public static void registerLateItem(String itemName, Supplier<Item> itemSup, Tuple<String, CreativeModeTab> group) {
        items.put(itemName, itemSup);
        BlockItemRegistryImpl.itemNameToGroup.put(itemName, group);
    }

    public static <T extends Block> T registerLateBlock(String blockId, Supplier<T> blockSup, boolean registerItem, Tuple<String, CreativeModeTab> group) {
        T block = blockSup.get();
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockId, block, group);
        }
        blocks.put(blockId, block);
        return block;
    }

    public static void registerBlockItemPlatformSpecific(String itemName, Block block, Tuple<String, CreativeModeTab> group) {
        if (AbstractSittableBlock.isWoodBased(block.defaultBlockState())) {
            registerLateItem(itemName, () -> new BlockItem(block, new Item.Properties()) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 300;
                }
            }, group);
        }
        registerLateItem(itemName, () -> new BlockItem(block, new Item.Properties()), group);
    }


    public static void registerBlocks(Registry<Block> blockRegisterEvent) {
        try {
            LateBlockRegistry.registerBlocks();
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        blocks.forEach((blockName, block) -> Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(PaladinFurnitureMod.MOD_ID, blockName), block));
    }

    public static void registerItems(Registry<Item> itemIForgeRegistry) {
        items.forEach((itemName, itemSup) -> {
            Item item = itemSup.get();
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(PaladinFurnitureMod.MOD_ID, itemName), item);
            if (!PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.containsKey(BlockItemRegistryImpl.itemNameToGroup.get(itemName))) {
                PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.put(BlockItemRegistryImpl.itemNameToGroup.get(itemName), new LinkedHashSet<>());
            }
            PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.get(BlockItemRegistryImpl.itemNameToGroup.get(itemName)).add(item);
        });
    }

    public static <T extends Block> T registerLateBlockClassic(String blockId, T block, boolean registerItem, Tuple<String, CreativeModeTab> group) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            registerBlockItemPlatformSpecific(blockId, block, group);
        }
        blocks.put(blockId, block);
        return block;
    }

    @SubscribeEvent
    public static void registerPOI(RegisterEvent event) {
        event.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), pointOfInterestTypeRegisterHelper -> {
            PaladinFurnitureModNeoForge.replaceHomePOIStates();
        });
    }
}
