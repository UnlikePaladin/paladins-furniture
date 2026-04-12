package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.items.PFMComponents;
import net.minecraft.core.component.ComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class PFMComponentsImpl {
    public static Map<ResourceLocation, ComponentType<?>> components = new HashMap<>();
    public static <T> ComponentType<T> register(ResourceLocation id, ComponentType<T> type) {
        components.put(id, type);
        return type;
    }

    @SubscribeEvent
    public static void registerComponents(RegisterEvent event) {
        event.register(BuiltInRegistries.DATA_COMPONENT_TYPE.key(), registerHelper -> {
            PFMComponents.registerComponents();
            PFMComponentsImpl.components.forEach(registerHelper::register);
        });
    }
}
