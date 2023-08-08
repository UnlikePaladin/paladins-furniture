package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FreezerBlockEntityImpl {
    public static Supplier<? extends FreezerBlockEntity> getFactory() {
        return FreezerBlockEntity::new;
    }
}
