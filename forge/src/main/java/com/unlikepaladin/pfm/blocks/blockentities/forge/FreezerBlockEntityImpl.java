package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.FreezerBlockEntityBalm;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FreezerBlockEntityImpl {
    public static Supplier<? extends FreezerBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? FreezerBlockEntityBalm::new : FreezerBlockEntity::new;
    }
}
