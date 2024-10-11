package com.unlikepaladin.pfm.items.neoforge;

import com.unlikepaladin.pfm.items.PFMComponents;
import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = "pfm", bus = EventBusSubscriber.Bus.MOD)
public class PFMComponentsImpl {
    public static Map<Identifier, DataComponentType<?>> components = new HashMap<>();
    public static <T> DataComponentType<T> register(Identifier id, DataComponentType<T> type) {
        components.put(id, type);
        return type;
    }

    @SubscribeEvent
    public static void registerComponents(RegisterEvent event) {
        event.register(Registries.DATA_COMPONENT_TYPE.getKey(), registerHelper -> {
            PFMComponents.registerComponents();
            PFMComponentsImpl.components.forEach(registerHelper::register);
        });
    }
}
