package com.unlikepaladin.pfm.items;

import com.mojang.serialization.Codec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.Identifier;

import java.util.List;

public class PFMComponents {
    public static void registerComponents() {
        PaladinFurnitureMod.GENERAL_LOGGER.info("Registering {} components", PaladinFurnitureMod.MOD_ID);
        VARIANT_COMPONENT = register(
                Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "variant"),
                DataComponentType.<Identifier>builder().persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC).build()
        );

        COLOR_COMPONENT = register(
                Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "color"),
                DataComponentType.<DyeColor>builder().persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC).build()
        );
        ACTIVATOR_COMPONENT = register(
                Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "activator"),
                DataComponentType.<List<BlockPos>>builder()
                        .persistent(BlockPos.CODEC.listOf())
                        .networkSynchronized(BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()))
                        .build()
        );
    }

    @ExpectPlatform
    public static <T> DataComponentType<T> register(Identifier id, DataComponentType<T> type) {
        throw new AssertionError();
    }

    public static DataComponentType<Identifier> VARIANT_COMPONENT;

    public static DataComponentType<DyeColor> COLOR_COMPONENT;

    public static DataComponentType<List<BlockPos>> ACTIVATOR_COMPONENT;
}
