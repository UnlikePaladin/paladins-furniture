package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity9x3;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.GenericStorageBlockEntityBalm9x3;
import net.minecraft.block.entity.BlockEntityType;

public class GenericStorageBlockEntity9x3Impl {
    public static BlockEntityType.BlockEntityFactory<? extends GenericStorageBlockEntity9x3> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? GenericStorageBlockEntityBalm9x3::new : GenericStorageBlockEntity9x3::new;
    }
}
