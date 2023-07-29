package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class FreezerBlockEntityImpl {
    public static BlockEntityType.BlockEntityFactory<? extends FreezerBlockEntity> getFactory() {
        return FreezerBlockEntity::new;
    }
}
