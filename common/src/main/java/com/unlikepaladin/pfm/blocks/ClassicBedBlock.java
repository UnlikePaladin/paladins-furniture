package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.LogTableBlock.rotateShape;

public class ClassicBedBlock extends SimpleBedBlock {
    private static final List<FurnitureBlock> CLASSIC_BEDS = new ArrayList<>();
    public ClassicBedBlock(DyeColor color, Settings settings) {
        super(color, settings);
        if(this.getClass().isAssignableFrom(ClassicBedBlock.class)){
            String bedColor = color.getName();
            CLASSIC_BEDS.add(new FurnitureBlock(this, bedColor+"_classic_bed"));
        }
    }

    public static Stream<FurnitureBlock> streamClassicBeds() {
        return CLASSIC_BEDS.stream();
    }

    @Override
    public boolean isBed(WorldAccess world, BlockPos pos, Direction direction, Direction bedDirection, BlockState originalState)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if(state.getBlock().getClass().isAssignableFrom(ClassicBedBlock.class) && state.getBlock() instanceof ClassicBedBlock)
        {
            if (state.get(PART) == originalState.get(PART)) {
                Direction sourceDirection = state.get(FACING);
                return sourceDirection.equals(bedDirection);
            }
        }
        return false;
    }

    static final VoxelShape HEAD = VoxelShapes.union(createCuboidShape(0, 9, 0,16, 16, 3),createCuboidShape(0, 0, 0,16, 9, 16));
    static final VoxelShape HEAD_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD);
    static final VoxelShape HEAD_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD);
    static final VoxelShape HEAD_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD);

    static final VoxelShape FOOT_EAST = VoxelShapes.union(createCuboidShape(0, 9, 0,3, 11, 16),createCuboidShape(0, 0, 0,18, 9, 16));
    static final VoxelShape FOOT_SOUTH = rotateShape(Direction.EAST, Direction.SOUTH, FOOT_EAST);
    static final VoxelShape FOOT_WEST = rotateShape(Direction.EAST, Direction.WEST, FOOT_EAST);
    static final VoxelShape FOOT_NORTH = rotateShape(Direction.EAST, Direction.NORTH, FOOT_EAST);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getBedShape(state.get(FACING), state.get(PART), HEAD, FOOT_NORTH, HEAD_EAST, FOOT_EAST, HEAD_WEST, FOOT_WEST, HEAD_SOUTH, FOOT_SOUTH);
    }

}
