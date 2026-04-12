package com.unlikepaladin.pfm.items;

import com.mojang.serialization.Codec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;

public class PFMComponents {
    public static void registerComponents() {
        PaladinFurnitureMod.GENERAL_LOGGER.info("Registering {} components", PaladinFurnitureMod.MOD_ID);
        VARIANT_COMPONENT = register(
                ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "variant"),
                DataComponentType.<ResourceLocation>builder().persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC).build()
        );

        COLOR_COMPONENT = register(
                ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "color"),
                DataComponentType.<DyeColor>builder().persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC).build()
        );
    }

    @ExpectPlatform
    public static <T> DataComponentType<T> register(ResourceLocation id, DataComponentType<T> type) {
        throw new AssertionError();
    }

    public static DataComponentType<ResourceLocation> VARIANT_COMPONENT;

    public static DataComponentType<DyeColor> COLOR_COMPONENT;
}
