package com.unlikepaladin.pfm.blocks.models.bed;

import com.unlikepaladin.pfm.blocks.ClassicBedBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

public interface BedInterface {
    default boolean isBed(BlockAndTintGetter world, BlockPos pos, Direction direction, Direction bedDirection, BlockState originalState, boolean isClassic)
    {
        BlockState state = world.getBlockState(pos.relative(direction));
        if (isClassic) {
            if(state.getBlock().getClass().isAssignableFrom(ClassicBedBlock.class) && state.getBlock() instanceof ClassicBedBlock)
            {
                if (state.getValue(BedBlock.PART) == originalState.getValue(BedBlock.PART)) {
                    Direction sourceDirection = state.getValue(BedBlock.FACING);
                    return sourceDirection.equals(bedDirection);
                }
            }
        }
        else {
            if(state.getBlock().getClass().isAssignableFrom(SimpleBedBlock.class) && state.getBlock() instanceof SimpleBedBlock)
            {
                if (state.getValue(BedBlock.PART) == originalState.getValue(BedBlock.PART)) {
                    Direction sourceDirection = state.getValue(BedBlock.FACING);
                    return sourceDirection.equals(bedDirection);
                }
            }
        }
        return false;
    }
}
