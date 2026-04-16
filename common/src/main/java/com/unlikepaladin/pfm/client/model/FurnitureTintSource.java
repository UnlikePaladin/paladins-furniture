package com.unlikepaladin.pfm.client.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.items.PFMComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;

public record FurnitureTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<FurnitureTintSource> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(FurnitureTintSource::defaultColor)).apply(instance, FurnitureTintSource::new));

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity user) {
        if (stack.has(PFMComponents.COLOR_COMPONENT))
            return ARGB.opaque(stack.get(PFMComponents.COLOR_COMPONENT).getFireworkColor());
        return defaultColor;
    }

    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
