package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class KitchenCounterOvenBlockImpl {
    public static BlockEntityType.BlockEntitySupplier<? extends CounterOvenBlockEntity> getFactory() {
        return CounterOvenBlockEntity::new;
    }
}
