package com.unlikepaladin.pfm.registry.forge;

import com.google.common.collect.ImmutableSet;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.items.forge.FurnitureGuideBookImpl;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.enums.BedPart;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockItemRegistryForge {
    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        BlockItemRegistry.registerCommonBlocks();
        event.register(ForgeRegistries.Keys.BLOCKS, blockRegisterHelper -> {
            blockRegisterHelper.registerAll(BlockItemRegistryImpl.blocks.toArray(new Block[0]));
        });
    }

    @SubscribeEvent
    public static void registerItems(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, itemRegisterHelper -> {
            PaladinFurnitureModBlocksItems.FURNITURE_BOOK = new FurnitureGuideBookImpl(new Item.Settings().group(PaladinFurnitureMod.FURNITURE_GROUP).rarity(Rarity.RARE).maxCount(1));
            BlockItemRegistry.registerCommonItems();
            BlockItemRegistryImpl.items.forEach((itemId, itemSupplier) -> {
                Item item = itemSupplier.get();
                item.setRegistryName(itemId);
                itemRegisterHelper.register(item);
            });
        }
    }
}
