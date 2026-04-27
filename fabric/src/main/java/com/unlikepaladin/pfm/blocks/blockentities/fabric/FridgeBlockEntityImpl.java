package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FridgeBlockEntityImpl {
    public static Supplier<? extends FridgeBlockEntity> getFactory() {
        return FridgeBlockEntity::new;
    }
}
