package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.ScreenHandlerRegistry;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class ScreenHandlerRegistryForge {

    @SubscribeEvent
    public static void registerScreenHandlers(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.MENU_TYPES, screenHandlerTypeRegisterHelper -> {
            ScreenHandlerRegistry.registerScreenHandlers();
            ScreenHandlerRegistryImpl.screenHandlerMap.forEach(screenHandlerTypeRegisterHelper::register);
        });
    }
}
