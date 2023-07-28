package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class FridgeBlockEntityImpl {
    public static BlockEntityType.BlockEntityFactory<? extends FridgeBlockEntity> getFactory() {
        return FridgeBlockEntity::new;
    }
}
