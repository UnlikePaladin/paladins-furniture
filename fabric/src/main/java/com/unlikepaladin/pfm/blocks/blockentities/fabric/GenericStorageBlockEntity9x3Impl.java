package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity9x3;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class GenericStorageBlockEntity9x3Impl {
    public static BlockEntityType.BlockEntitySupplier<? extends GenericStorageBlockEntity9x3> getFactory() {
        return GenericStorageBlockEntity9x3::new;
    }
}
