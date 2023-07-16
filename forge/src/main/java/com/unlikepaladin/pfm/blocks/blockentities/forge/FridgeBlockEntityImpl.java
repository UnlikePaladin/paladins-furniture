package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import com.unlikepaladin.pfm.compat.forge.cookingforblockheads.FridgeBlockEntityBalm;
import net.minecraft.block.entity.BlockEntityType;

public class FridgeBlockEntityImpl {
    public static BlockEntityType.BlockEntityFactory<? extends FridgeBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? FridgeBlockEntityBalm::new : FridgeBlockEntity::new;
    }
}
