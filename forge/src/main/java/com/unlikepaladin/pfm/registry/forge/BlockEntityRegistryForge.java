package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.blocks.blockentities.*;
import com.unlikepaladin.pfm.blocks.blockentities.forge.*;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntityRegistryForge {

    @SubscribeEvent
    public static void registerEntities(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, blockEntityTypeRegisterHelper -> {
            BlockEntityRegistry.registerBlockEntities();
            BlockEntityRegistryImpl.blockEntityTypes.forEach(blockEntityTypeRegisterHelper::register);
        });
    }
}
