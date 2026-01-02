package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.FreezerBlockEntityBalm;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class FreezerBlockEntityImpl extends FreezerBlockEntity {
    public FreezerBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static BlockEntityType.BlockEntityFactory<? extends FreezerBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? FreezerBlockEntityBalm::new : FreezerBlockEntityImpl::new;
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        world.invalidateCapabilities(pos);
    }
}
