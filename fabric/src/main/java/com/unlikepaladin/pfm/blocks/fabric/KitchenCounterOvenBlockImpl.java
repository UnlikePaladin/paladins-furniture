package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class KitchenCounterOvenBlockImpl {
    public static Supplier<? extends CounterOvenBlockEntity> getFactory() {
        return CounterOvenBlockEntity::new;
    }
}
