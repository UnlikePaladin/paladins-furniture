package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.items.forge.FurnitureGuideBookImpl;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockItemRegistryForge {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BlockItemRegistry.registerCommonBlocks();
        event.getRegistry().registerAll(
                BlockItemRegistryImpl.blocks.toArray(new Block[0])
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        PaladinFurnitureModBlocksItems.FURNITURE_BOOK = new FurnitureGuideBookImpl(new Item.Properties().tab(PaladinFurnitureMod.FURNITURE_GROUP).rarity(Rarity.RARE).stacksTo(1));
        BlockItemRegistry.registerCommonItems();
        BlockItemRegistryImpl.items.forEach((itemId, itemSupplier) -> {
            Item item = itemSupplier.get();
            item.setRegistryName(itemId);
            event.getRegistry().register(item);
        });
    }
}
