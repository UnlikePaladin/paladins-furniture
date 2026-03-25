package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FridgeBlockEntityImpl {
    public static BlockEntityType.BlockEntitySupplier<? extends FridgeBlockEntity> getFactory() {
        return FridgeBlockEntity::new;
    }
}
