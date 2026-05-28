package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.registry.ScreenHandlerRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ScreenHandlerRegistryNeoForge {

    @SubscribeEvent
    public static void registerScreenHandlers(RegisterEvent event) {
        event.register(BuiltInRegistries.MENU.key(), screenHandlerTypeRegisterHelper -> {
            ScreenHandlerRegistry.registerScreenHandlers();
            ScreenHandlerRegistryImpl.screenHandlerMap.forEach(screenHandlerTypeRegisterHelper::register);
        });
    }
}
