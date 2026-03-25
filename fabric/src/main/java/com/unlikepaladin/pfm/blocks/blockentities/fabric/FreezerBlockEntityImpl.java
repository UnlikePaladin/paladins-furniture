package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FreezerBlockEntityImpl {
    public static BlockEntityType.BlockEntitySupplier<? extends FreezerBlockEntity> getFactory() {
        return FreezerBlockEntity::new;
    }
}
