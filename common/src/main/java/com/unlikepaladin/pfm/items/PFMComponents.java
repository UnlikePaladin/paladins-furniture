package com.unlikepaladin.pfm.items;

import com.mojang.serialization.Codec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.component.ComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;

public class PFMComponents {
    public static void registerComponents() {
        PaladinFurnitureMod.GENERAL_LOGGER.info("Registering {} components", PaladinFurnitureMod.MOD_ID);
        VARIANT_COMPONENT = register(
                ResourceLocation.parse(PaladinFurnitureMod.MOD_ID, "variant"),
                ComponentType.<ResourceLocation>builder().persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC).build()
        );

        COLOR_COMPONENT = register(
                ResourceLocation.parse(PaladinFurnitureMod.MOD_ID, "color"),
                ComponentType.<DyeColor>builder().persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC).build()
        );
    }

    @ExpectPlatform
    public static <T> ComponentType<T> register(ResourceLocation id, ComponentType<T> type) {
        throw new AssertionError();
    }

    public static ComponentType<ResourceLocation> VARIANT_COMPONENT;

    public static ComponentType<DyeColor> COLOR_COMPONENT;
}
