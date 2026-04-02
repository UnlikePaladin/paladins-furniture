package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntityRegistryNeoForge {

    @SubscribeEvent
    public static void registerEntities(RegisterEvent event) {
        event.register(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), blockEntityTypeRegisterHelper -> {
            BlockEntityRegistry.registerBlockEntities();
            BlockEntityRegistryImpl.blockEntityTypes.forEach(blockEntityTypeRegisterHelper::register);
        });
    }
}
