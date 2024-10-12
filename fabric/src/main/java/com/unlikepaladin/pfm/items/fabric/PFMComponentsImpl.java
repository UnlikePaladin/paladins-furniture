package com.unlikepaladin.pfm.items.fabric;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class PFMComponentsImpl {
    public static <T> ComponentType<T> register(Identifier id, ComponentType<T> type) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                id,
                type
        );
    }
}
