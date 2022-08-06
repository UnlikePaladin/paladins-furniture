package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.BedPart;
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

import static com.unlikepaladin.pfm.blocks.LogTable.rotateShape;

public class ClassicBed extends SimpleBed {
    private static final List<FurnitureBlock> CLASSIC_BEDS = new ArrayList<>();
    public ClassicBed(DyeColor color, Settings settings) {
        super(color, settings);
        if(this.getClass().isAssignableFrom(ClassicBed.class)){
            String bedColor = color.getName();
            CLASSIC_BEDS.add(new FurnitureBlock(this, bedColor+"_classic_bed"));
        }
    }

    public static Stream<FurnitureBlock> streamClassicBeds() {
        return CLASSIC_BEDS.stream();
    }


    static final VoxelShape HEAD = VoxelShapes.union(createCuboidShape(0, 2, 1, 16, 14, 3),createCuboidShape(0, 2, 3, 16, 9, 16),createCuboidShape(1, 9, 3, 15, 10, 11));
    static final VoxelShape HEAD_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD);
    static final VoxelShape HEAD_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD);
    static final VoxelShape HEAD_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD);

    static final VoxelShape FOOT = VoxelShapes.union(createCuboidShape(0, 2, 0, 16, 9, 13),createCuboidShape(0, 2, 13, 16, 10, 15));
    static final VoxelShape FOOT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT);
    static final VoxelShape FOOT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT);
    static final VoxelShape FOOT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT);

    static final VoxelShape FOOT_FOOT_R = createCuboidShape(13, 0, 13, 16, 11, 16);
    static final VoxelShape FOOT_FOOT_L = createCuboidShape(0, 0, 13, 3, 11, 16);
    static final VoxelShape FOOT_HEAD_R = createCuboidShape(13, 0, 0, 16, 16, 3);
    static final VoxelShape FOOT_HEAD_L = createCuboidShape(0, 0, 0, 3, 16, 3);

    static final VoxelShape HEAD_SINGLE = VoxelShapes.union(HEAD, FOOT_HEAD_L, FOOT_HEAD_R);
    static final VoxelShape HEAD_SINGLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_SINGLE);
    static final VoxelShape HEAD_SINGLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_SINGLE);
    static final VoxelShape HEAD_SINGLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_SINGLE);

    static final VoxelShape FOOT_SINGLE = VoxelShapes.union(FOOT, FOOT_FOOT_L, FOOT_FOOT_R);
    static final VoxelShape FOOT_SINGLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_SINGLE);
    static final VoxelShape FOOT_SINGLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_SINGLE);
    static final VoxelShape FOOT_SINGLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_SINGLE);

    static final VoxelShape HEAD_LEFT = VoxelShapes.union(HEAD, FOOT_HEAD_L);
    static final VoxelShape HEAD_LEFT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_LEFT);
    static final VoxelShape HEAD_LEFT_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_LEFT);
    static final VoxelShape HEAD_LEFT_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_LEFT);

    static final VoxelShape FOOT_LEFT = VoxelShapes.union(FOOT, FOOT_FOOT_L);
    static final VoxelShape FOOT_LEFT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_LEFT);
    static final VoxelShape FOOT_LEFT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_LEFT);
    static final VoxelShape FOOT_LEFT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_LEFT);

    static final VoxelShape HEAD_RIGHT = VoxelShapes.union(HEAD, FOOT_HEAD_R);
    static final VoxelShape HEAD_RIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_RIGHT);
    static final VoxelShape HEAD_RIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_RIGHT);
    static final VoxelShape HEAD_RIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_RIGHT);

    static final VoxelShape FOOT_RIGHT = VoxelShapes.union(FOOT, FOOT_FOOT_R);
    static final VoxelShape FOOT_RIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_RIGHT);
    static final VoxelShape FOOT_RIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_RIGHT);
    static final VoxelShape FOOT_RIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_RIGHT);

    static final VoxelShape FOOT_BUNK_LEFT = createCuboidShape(0, -5, 13,3, 0, 16);
    static final VoxelShape FOOT_BUNK_LEFT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_BUNK_LEFT);
    static final VoxelShape FOOT_BUNK_LEFT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_BUNK_LEFT);
    static final VoxelShape FOOT_BUNK_LEFT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_BUNK_LEFT);

    static final VoxelShape FOOT_BUNK_RIGHT = createCuboidShape(13, -5, 13,16, 0, 16);
    static final VoxelShape FOOT_BUNK_RIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_BUNK_RIGHT);
    static final VoxelShape FOOT_BUNK_RIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_BUNK_RIGHT);
    static final VoxelShape FOOT_BUNK_RIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_BUNK_RIGHT);

    static final VoxelShape FOOT_SINGLE_BUNK = VoxelShapes.union(FOOT_SINGLE, FOOT_BUNK_LEFT, FOOT_BUNK_RIGHT);
    static final VoxelShape FOOT_SINGLE_SOUTH_BUNK = VoxelShapes.union(FOOT_SINGLE_SOUTH, FOOT_BUNK_LEFT_SOUTH, FOOT_BUNK_RIGHT_SOUTH);
    static final VoxelShape FOOT_SINGLE_EAST_BUNK = VoxelShapes.union(FOOT_SINGLE_EAST, FOOT_BUNK_LEFT_EAST, FOOT_BUNK_RIGHT_EAST);
    static final VoxelShape FOOT_SINGLE_WEST_BUNK = VoxelShapes.union(FOOT_SINGLE_WEST, FOOT_BUNK_LEFT_WEST, FOOT_BUNK_RIGHT_WEST);

    static final VoxelShape FOOT_LEFT_BUNK = VoxelShapes.union(FOOT_LEFT, FOOT_BUNK_LEFT);
    static final VoxelShape FOOT_LEFT_SOUTH_BUNK = VoxelShapes.union(FOOT_LEFT_SOUTH, FOOT_BUNK_LEFT_SOUTH);
    static final VoxelShape FOOT_LEFT_EAST_BUNK = VoxelShapes.union(FOOT_LEFT_EAST, FOOT_BUNK_LEFT_EAST);
    static final VoxelShape FOOT_LEFT_WEST_BUNK = VoxelShapes.union(FOOT_LEFT_WEST, FOOT_BUNK_LEFT_WEST);

    static final VoxelShape FOOT_RIGHT_BUNK = VoxelShapes.union(FOOT_RIGHT, FOOT_BUNK_RIGHT);
    static final VoxelShape FOOT_RIGHT_SOUTH_BUNK = VoxelShapes.union(FOOT_RIGHT_SOUTH, FOOT_BUNK_RIGHT_SOUTH);
    static final VoxelShape FOOT_RIGHT_EAST_BUNK = VoxelShapes.union(FOOT_RIGHT_EAST, FOOT_BUNK_RIGHT_EAST);
    static final VoxelShape FOOT_RIGHT_WEST_BUNK = VoxelShapes.union(FOOT_RIGHT_WEST, FOOT_BUNK_RIGHT_WEST);

    @Override
    public boolean isBed(WorldAccess world, BlockPos pos, Direction direction, Direction bedDirection, BlockState originalState)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if(state.getBlock().getClass().isAssignableFrom(ClassicBed.class) && state.getBlock() instanceof ClassicBed)
        {
            if (state.get(PART) == originalState.get(PART)) {
                Direction sourceDirection = state.get(FACING);
                return sourceDirection.equals(bedDirection);
            }
        }
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        BedPart bedPart = state.get(PART);
        MiddleShape middleShape = state.get(SHAPE);
        boolean bunk = state.get(BUNK);
        if (bedPart == BedPart.FOOT && bunk) {
            switch (middleShape) {
                case SINGLE -> {
                    switch (dir) {
                        case NORTH -> {
                            return FOOT_SINGLE_BUNK;
                        }
                        case EAST -> {
                            return FOOT_SINGLE_EAST_BUNK;
                        }
                        case WEST -> {
                            return FOOT_SINGLE_WEST_BUNK;
                        }
                        default -> {
                            return FOOT_SINGLE_SOUTH_BUNK;
                        }
                    }
                }
                case MIDDLE -> {
                    switch (dir) {
                        case NORTH -> {
                            return FOOT;
                        }
                        case EAST -> {
                            return FOOT_EAST;
                        }
                        case WEST -> {
                            return FOOT_WEST;
                        }
                        default -> {
                            return FOOT_SOUTH;
                        }
                    }
                }
                case RIGHT -> {
                    switch (dir) {
                        case NORTH -> {
                            return FOOT_RIGHT_BUNK;
                        }
                        case EAST -> {
                            return FOOT_RIGHT_EAST_BUNK;
                        }
                        case WEST -> {
                            return FOOT_RIGHT_WEST_BUNK;
                        }
                        default -> {
                            return FOOT_RIGHT_SOUTH_BUNK;
                        }
                    }
                }
                default -> {
                    switch (dir) {
                        case NORTH -> {
                            return FOOT_LEFT_BUNK;
                        }
                        case EAST -> {
                            return FOOT_LEFT_EAST_BUNK;
                        }
                        case WEST -> {
                            return FOOT_LEFT_WEST_BUNK;
                        }
                        default -> {
                            return FOOT_LEFT_SOUTH_BUNK;
                        }
                    }
                }
            }
        }
        switch (middleShape){
            case MIDDLE -> {
                switch (dir){
                    case NORTH -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD;
                        }
                        return FOOT;
                    }
                    case EAST -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_EAST;
                        }
                        return FOOT_EAST;
                    }
                    case WEST -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_WEST;
                        }
                        return FOOT_WEST;
                    }
                    default -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SOUTH;
                        }
                        return FOOT_SOUTH;
                    }
                }
            }
            case SINGLE -> {
                switch (dir){
                    case NORTH -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SINGLE;
                        }
                        return FOOT_SINGLE;
                    }
                    case EAST -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SINGLE_EAST;
                        }
                        return FOOT_SINGLE_EAST;
                    }
                    case WEST -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SINGLE_WEST;
                        }
                        return FOOT_SINGLE_WEST;
                    }
                    default -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SINGLE_SOUTH;
                        }
                        return FOOT_SINGLE_SOUTH;
                    }
                }
            }
            case RIGHT -> {
                switch (dir) {
                    case NORTH -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_RIGHT;
                        }
                        return FOOT_RIGHT;
                    }
                    case EAST -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_RIGHT_EAST;
                        }
                        return FOOT_RIGHT_EAST;
                    }
                    case WEST -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_RIGHT_WEST;
                        }
                        return FOOT_RIGHT_WEST;
                    }
                    default -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_RIGHT_SOUTH;
                        }
                        return FOOT_RIGHT_SOUTH;
                    }
                }
            }
            default -> {
                switch (dir) {
                    case NORTH -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_LEFT;
                        }
                        return FOOT_LEFT;
                    }
                    case EAST -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_LEFT_EAST;
                        }
                        return FOOT_LEFT_EAST;
                    }
                    case WEST -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_LEFT_WEST;
                        }
                        return FOOT_LEFT_WEST;
                    }
                    default -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_LEFT_SOUTH;
                        }
                        return FOOT_LEFT_SOUTH;
                    }
                }
            }
        }
    }
}
