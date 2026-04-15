package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.items.forge.FurnitureGuideBookImpl;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.*;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockItemRegistryForge {
    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, blockRegisterHelper -> {
            BlockItemRegistry.registerCommonBlocks();
            BlockItemRegistryImpl.blocks.forEach(blockRegisterHelper::register);
        });
    }

    @SubscribeEvent
    public static void registerItems(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, itemRegisterHelper -> {
            if (!PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.containsKey(PaladinFurnitureMod.FURNITURE_GROUP)) {
                PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.put(PaladinFurnitureMod.FURNITURE_GROUP, new LinkedHashSet<>());
            }
            PaladinFurnitureModBlocksItems.FURNITURE_BOOK = new FurnitureGuideBookImpl(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).setId(LateBlockRegistry.getItemRegistryKey("furniture_book")));
            PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.get(PaladinFurnitureMod.FURNITURE_GROUP).add(PaladinFurnitureModBlocksItems.FURNITURE_BOOK);
            BlockItemRegistry.registerCommonItems();
            BlockItemRegistryImpl.items.forEach((itemId, itemSupplier) -> {
                Item item = itemSupplier.get();
                if (!PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.containsKey(BlockItemRegistryImpl.itemNameToGroup.get(itemId))) {
                    PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.put(BlockItemRegistryImpl.itemNameToGroup.get(itemId), new LinkedHashSet<>());
                }
                PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.get(BlockItemRegistryImpl.itemNameToGroup.get(itemId)).add(item);
                itemRegisterHelper.register(itemId, item);
            });
        });
    }
}
