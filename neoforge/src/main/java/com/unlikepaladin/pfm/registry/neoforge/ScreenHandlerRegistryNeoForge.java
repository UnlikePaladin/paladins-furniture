package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.registry.ScreenHandlerRegistry;
import net.minecraft.registry.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ScreenHandlerRegistryNeoForge {

    @SubscribeEvent
    public static void registerScreenHandlers(RegisterEvent event) {
        event.register(Registries.SCREEN_HANDLER.getKey(), screenHandlerTypeRegisterHelper -> {
            ScreenHandlerRegistry.registerScreenHandlers();
            ScreenHandlerRegistryImpl.screenHandlerMap.forEach(screenHandlerTypeRegisterHelper::register);
        });
    }
}
