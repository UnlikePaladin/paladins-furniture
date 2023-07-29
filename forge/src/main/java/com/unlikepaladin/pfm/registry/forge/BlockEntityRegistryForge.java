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
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Arrays;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntityRegistryForge {

    @SubscribeEvent
    public static void registerEntities(RegisterEvent event) {
        BlockEntityRegistry.registerBlockEntities();
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, blockEntityTypeRegisterHelper -> {
            blockEntityTypeRegisterHelper.registerAll(BlockEntityRegistryImpl.blockEntityTypes.toArray(new BlockEntityType[0]));
        );
    }
}
