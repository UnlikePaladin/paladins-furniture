package com.unlikepaladin.pfm.items.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;


public class PFMComponentsImpl {
    public static <T> ComponentType<T> register(ResourceLocation id, ComponentType<T> type) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                id,
                type
        );
    }
}
