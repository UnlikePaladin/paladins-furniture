package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.SinkBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SinkBlockEntityImpl {
    public static BlockEntityType.BlockEntitySupplier<? extends SinkBlockEntity> getFactory() {
        return SinkBlockEntity::new;
    }
}
