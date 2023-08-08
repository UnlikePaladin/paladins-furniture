package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.blocks.BasicSinkBlock;
import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.blocks.blockentities.*;
import com.unlikepaladin.pfm.blocks.blockentities.forge.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntityRegistryForge {

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        BlockEntityRegistry.registerBlockEntities();
        event.getRegistry().registerAll(
                BlockEntityRegistryImpl.blockEntityTypes.toArray(new BlockEntityType[0])
        );
    }
}
