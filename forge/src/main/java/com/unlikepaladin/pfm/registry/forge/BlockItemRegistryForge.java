package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.items.forge.FurnitureGuideBookImpl;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
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
            PaladinFurnitureModBlocksItems.FURNITURE_BOOK = new FurnitureGuideBookImpl(new Item.Properties().tab(PaladinFurnitureMod.FURNITURE_GROUP).rarity(Rarity.RARE).stacksTo(1));
            BlockItemRegistry.registerCommonItems();
            BlockItemRegistryImpl.items.forEach((itemId, itemSupplier) -> {
                Item item = itemSupplier.get();
                itemRegisterHelper.register(itemId, item);
            });
        });
    }
}
