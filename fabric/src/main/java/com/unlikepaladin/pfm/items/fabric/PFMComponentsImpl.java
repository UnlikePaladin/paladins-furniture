package com.unlikepaladin.pfm.items.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class PFMComponentsImpl {
    public static <T> DataComponentType<T> register(Identifier id, DataComponentType<T> type) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                id,
                type
        );
    }
}
