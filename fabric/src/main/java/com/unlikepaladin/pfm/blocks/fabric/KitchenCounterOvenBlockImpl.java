package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.CounterOvenBlockEntityBalm;
import net.minecraft.block.entity.BlockEntityType;

public class KitchenCounterOvenBlockImpl {
    public static BlockEntityType.BlockEntityFactory<? extends CounterOvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? CounterOvenBlockEntityBalm::new : CounterOvenBlockEntity::new;
    }
}
