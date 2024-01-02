package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.FridgeBlockEntityBalm;
import net.minecraft.block.entity.BlockEntityType;

public class FridgeBlockEntityImpl {
    public static BlockEntityType.BlockEntityFactory<? extends FridgeBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? FridgeBlockEntityBalm::new : FridgeBlockEntity::new;
    }
}
