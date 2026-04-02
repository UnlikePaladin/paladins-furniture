package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.neoforge.FreezerBlockEntityImpl;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.items.neoforge.FurnitureGuideBookImpl;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.ForwardingItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.*;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockItemRegistryNeoForge {
    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        event.register(BuiltInRegistries.BLOCK.key(), blockRegisterHelper -> {
            BlockItemRegistry.registerCommonBlocks();
            BlockItemRegistryImpl.blocks.forEach((blockName, block) -> blockRegisterHelper.register(new ResourceLocation(PaladinFurnitureMod.MOD_ID, blockName), block));
        });
    }

    @SubscribeEvent
    public static void registerItems(RegisterEvent event) {
        event.register(BuiltInRegistries.ITEM.key(), itemRegisterHelper -> {
            if (!PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.containsKey(PaladinFurnitureMod.FURNITURE_GROUP)) {
                PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.put(PaladinFurnitureMod.FURNITURE_GROUP, new ArrayList<>());
            }
            PaladinFurnitureModBlocksItems.FURNITURE_BOOK = new FurnitureGuideBookImpl(new Item.Properties().rarity(Rarity.RARE).stacksTo(1));
            PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.get(PaladinFurnitureMod.FURNITURE_GROUP).add(PaladinFurnitureModBlocksItems.FURNITURE_BOOK);
            BlockItemRegistry.registerCommonItems();
            BlockItemRegistryImpl.items.forEach((itemName, itemSupplier) -> {
                Item item = itemSupplier.get();
                if (!PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.containsKey(BlockItemRegistryImpl.itemNameToGroup.get(itemName))) {
                    PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.put(BlockItemRegistryImpl.itemNameToGroup.get(itemName), new ArrayList<>());
                }
                PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.get(BlockItemRegistryImpl.itemNameToGroup.get(itemName)).add(item);
                itemRegisterHelper.register(new ResourceLocation(PaladinFurnitureMod.MOD_ID, itemName), item);
            });
        });
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntities.FREEZER_BLOCK_ENTITY, (freezerBlockEntity, side) -> {
            if (side == null) {
                return new ForwardingItemHandler(() -> new InvWrapper(freezerBlockEntity));
            } else {
                return new ForwardingItemHandler(() -> new SidedInvWrapper(freezerBlockEntity, side));
            }
        });

        if (ModList.get().isLoaded("cookingforblockheads")) {
            PFMCookingForBlockHeadsCompat.registerCapabilities(event);
        }
    }
}
