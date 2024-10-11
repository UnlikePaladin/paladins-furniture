package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.items.PFMComponents;
import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
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
