package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity3x3;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.GenericStorageBlockEntityBalm3x3;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class GenericStorageBlockEntity3x3Impl {
    public static Supplier<? extends GenericStorageBlockEntity3x3> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? GenericStorageBlockEntityBalm3x3::new : GenericStorageBlockEntity3x3::new;
    }
}
