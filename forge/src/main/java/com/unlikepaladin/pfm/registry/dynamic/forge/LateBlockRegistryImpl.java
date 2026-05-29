package com.unlikepaladin.pfm.registry.dynamic.forge;

import com.google.common.collect.ImmutableSet;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.AbstractSittableBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.mixin.PFMPointOfInterestTypesAccessor;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.registry.forge.BlockItemRegistryImpl;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
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
            registerLateItem(itemName, () -> new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(LateBlockRegistry.getItemRegistryKey(itemName))) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 300;
                }
            }, group);
        }
        registerLateItem(itemName, () -> new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(LateBlockRegistry.getItemRegistryKey(itemName))), group);
    }


    public static void registerBlocks(IForgeRegistry<Block> blockRegisterEvent) {
        try {
            LateBlockRegistry.registerBlocks();
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        blocks.forEach(blockRegisterEvent::register);
    }

    public static void registerItems(IForgeRegistry<Item> itemIForgeRegistry) {
        items.forEach((itemName, itemSup) -> {
            Item item = itemSup.get();
            itemIForgeRegistry.register(itemName, item);
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
        event.register(ForgeRegistries.Keys.POI_TYPES, pointOfInterestTypeRegisterHelper -> {
            Set<BlockState> originalBedStates = ForgeRegistries.POI_TYPES.getValue(PoiTypes.HOME.location()).matchingStates();
            Set<BlockState> addedBedStates = Arrays.stream(PaladinFurnitureModBlocksItems.getBeds()).flatMap(block -> block.getStateDefinition().getPossibleStates().stream().filter(state -> state.getValue(SimpleBedBlock.PART) == BedPart.HEAD)).collect(ImmutableSet.toImmutableSet());
            Set<BlockState> newBedStates = new HashSet<>();
            newBedStates.addAll(originalBedStates);
            newBedStates.addAll(addedBedStates);
            PoiType pointOfInterestType = new PoiType(newBedStates, 1, 1);
            ForgeRegistries.POI_TYPES.register("minecraft:home", pointOfInterestType);
            PFMPointOfInterestTypesAccessor.setHome(ForgeRegistries.POI_TYPES.getHolder(pointOfInterestType).get().unwrapKey().get());
            // PaladinFurnitureModForge.replaceHomePOIStates();
        });
    }
}
