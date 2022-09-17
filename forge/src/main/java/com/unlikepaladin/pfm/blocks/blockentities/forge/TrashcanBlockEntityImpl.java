package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class TrashcanBlockEntityImpl extends TrashcanBlockEntity {
    public TrashcanBlockEntityImpl(BlockEntityType<? extends TrashcanBlockEntity> trashcanBlockEntity) {
        super(trashcanBlockEntity);
    }

    public TrashcanBlockEntityImpl() {
        super();
    }
}
