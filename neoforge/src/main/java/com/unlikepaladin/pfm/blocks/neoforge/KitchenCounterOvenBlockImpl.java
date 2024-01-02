package com.unlikepaladin.pfm.blocks.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.CounterOvenBlockEntityBalm;
import net.minecraft.block.entity.BlockEntityType;

public class KitchenCounterOvenBlockImpl {
    public static BlockEntityType.BlockEntityFactory<? extends CounterOvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? CounterOvenBlockEntityBalm::new : CounterOvenBlockEntity::new;
    }
}
