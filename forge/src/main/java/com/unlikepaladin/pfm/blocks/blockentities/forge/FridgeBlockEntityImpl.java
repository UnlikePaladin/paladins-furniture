package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.FridgeBlockEntityBalm;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FridgeBlockEntityImpl {
    public static Supplier<? extends FridgeBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? FridgeBlockEntityBalm::new : FridgeBlockEntity::new;
    }
}
