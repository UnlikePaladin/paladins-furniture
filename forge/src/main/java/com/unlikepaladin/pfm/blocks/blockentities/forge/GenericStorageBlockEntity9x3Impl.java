package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity9x3;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.GenericStorageBlockEntityBalm9x3;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class GenericStorageBlockEntity9x3Impl {
    public static Supplier<? extends GenericStorageBlockEntity9x3> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? GenericStorageBlockEntityBalm9x3::new : GenericStorageBlockEntity9x3::new;
    }
}
