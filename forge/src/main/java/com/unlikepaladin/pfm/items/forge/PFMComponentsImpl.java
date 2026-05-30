package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.items.PFMComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

public class PFMComponentsImpl {
    public static Map<Identifier, DataComponentType<?>> components = new HashMap<>();
    public static <T> DataComponentType<T> register(Identifier id, DataComponentType<T> type) {
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
