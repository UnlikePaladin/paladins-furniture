package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.CounterOvenBlockEntityBalm;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class KitchenCounterOvenBlockImpl {
    public static Supplier<? extends CounterOvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? CounterOvenBlockEntityBalm::new : CounterOvenBlockEntity::new;
    }
}
