package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity3x3;
import net.minecraft.block.entity.BlockEntityType;

public class GenericStorageBlockEntity3x3Impl {
    public static BlockEntityType.BlockEntityFactory<? extends GenericStorageBlockEntity3x3> getFactory() {
        return GenericStorageBlockEntity3x3::new;
    }
}
