package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.LogTableBlock.rotateShape;

public class ClassicBedBlock extends SimpleBedBlock {
    private static final List<FurnitureBlock> CLASSIC_BEDS = new ArrayList<>();
    public ClassicBedBlock(DyeColor color, BlockBehaviour.Properties settings) {
        super(color, settings);
        if(this.getClass().isAssignableFrom(ClassicBedBlock.class)){
            String bedColor = color.getName();
            CLASSIC_BEDS.add(new FurnitureBlock(this, bedColor+"_classic_bed"));
        }
    }

    public static Stream<FurnitureBlock> streamClassicBeds() {
        return CLASSIC_BEDS.stream();
    }

    static final VoxelShape HEAD = Shapes.or(box(0, 9, 0,16, 16, 3),box(0, 0, 0,16, 9, 16));
    static final VoxelShape HEAD_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD);
    static final VoxelShape HEAD_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD);
    static final VoxelShape HEAD_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD);

    static final VoxelShape FOOT_EAST = Shapes.or(box(0, 9, 0,3, 11, 16),box(0, 0, 0,18, 9, 16));
    static final VoxelShape FOOT_SOUTH = rotateShape(Direction.EAST, Direction.SOUTH, FOOT_EAST);
    static final VoxelShape FOOT_WEST = rotateShape(Direction.EAST, Direction.WEST, FOOT_EAST);
    static final VoxelShape FOOT_NORTH = rotateShape(Direction.EAST, Direction.NORTH, FOOT_EAST);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getBedShape(state.getValue(FACING), state.getValue(PART), HEAD, FOOT_NORTH, HEAD_EAST, FOOT_EAST, HEAD_WEST, FOOT_WEST, HEAD_SOUTH, FOOT_SOUTH);
    }

}
