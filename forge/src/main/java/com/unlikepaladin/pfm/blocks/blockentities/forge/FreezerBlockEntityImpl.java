package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.compat.forge.cookingforblockheads.FreezerBlockEntityBalm;
import net.minecraft.block.entity.BlockEntityType;

public class FreezerBlockEntityImpl {
    public static BlockEntityType.BlockEntityFactory<? extends FreezerBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? FreezerBlockEntityBalm::new : FreezerBlockEntity::new;
    }
}
