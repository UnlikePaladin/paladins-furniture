package com.unlikepaladin.pfm.blocks.models.bed;

import com.unlikepaladin.pfm.blocks.ClassicBedBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

public interface BedInterface {
    default boolean isBed(BlockRenderView world, BlockPos pos, Direction direction, Direction bedDirection, BlockState originalState, boolean isClassic)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if (isClassic) {
            if(state.getBlock().getClass().isAssignableFrom(ClassicBedBlock.class) && state.getBlock() instanceof ClassicBedBlock)
            {
                if (state.get(BedBlock.PART) == originalState.get(BedBlock.PART)) {
                    Direction sourceDirection = state.get(BedBlock.FACING);
                    return sourceDirection.equals(bedDirection);
                }
            }
        }
        else {
            if(state.getBlock().getClass().isAssignableFrom(SimpleBedBlock.class) && state.getBlock() instanceof SimpleBedBlock)
            {
                if (state.get(BedBlock.PART) == originalState.get(BedBlock.PART)) {
                    Direction sourceDirection = state.get(BedBlock.FACING);
                    return sourceDirection.equals(bedDirection);
                }
            }
        }
        return false;
    }
}
