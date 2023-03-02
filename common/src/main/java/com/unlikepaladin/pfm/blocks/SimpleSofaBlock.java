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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class SimpleSofaBlock extends ArmChairColoredBlock {
    private static final List<FurnitureBlock> SIMPLE_SOFAS = new ArrayList<>();
    public SimpleSofaBlock(DyeColor color, Settings settings) {
        super(color, settings);
        SIMPLE_SOFAS.add(new FurnitureBlock(this, "arm_chair"));
    }
    public static Stream<FurnitureBlock> streamSimpleSofas() {
        return SIMPLE_SOFAS.stream();
    }
    @Override
    public boolean canConnect(BlockView world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        boolean canConnect = (state.getBlock() instanceof SimpleSofaBlock);
        return canConnect;
    }
    @Override
    public boolean isArmChair(BlockState state) {
        return(state.getBlock().getClass().isAssignableFrom(SimpleSofaBlock.class) && state.getBlock() instanceof SimpleSofaBlock);
    }

    public static final VoxelShape STANDARD = VoxelShapes.union(createCuboidShape(11.7, 0, 12,14.2, 3, 14.5),createCuboidShape(11.7, 0, 1.5,14.2, 3, 4),createCuboidShape(0.7, 0, 1.5,3.2, 3, 4),createCuboidShape(0.7, 0, 12,3.2, 3, 14.5),createCuboidShape(5, 9.5, 13,16, 13.71, 16),createCuboidShape(5, 9.5, 0,16, 13.71, 3),createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 16));
    public static final VoxelShape OUTER = VoxelShapes.union(createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 5),createCuboidShape(11.7, 0, 12,14.2, 3, 14.5),createCuboidShape(0.7, 0, 1.5,3.2, 3, 4));
    public static final VoxelShape MIDDLE = VoxelShapes.union(createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 16));
    public static final VoxelShape INNER = VoxelShapes.union(createCuboidShape(11.7, 0, 1.5,14.2, 3, 4),createCuboidShape(0.7, 0, 12,3.2, 3, 14.5),createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 11),createCuboidShape(0, 9.5, 11,16, 19.51, 16));
    public static final VoxelShape RIGHT_EDGE = VoxelShapes.union(createCuboidShape(11.7, 0, 1.5,14.2, 3, 4),createCuboidShape(0.7, 0, 1.5,3.2, 3, 4),createCuboidShape(5, 9.5, 0,16, 13.71, 3),createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 16));
    public static final VoxelShape LEFT_EDGE = VoxelShapes.union(createCuboidShape(0.7, 0, 12,3.2, 3, 14.5),createCuboidShape(11.7, 0, 12,14.2, 3, 14.5),createCuboidShape(5, 9.5, 13,16, 13.71, 16),createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 16));

    public static final VoxelShape STANDARD_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, STANDARD);
    public static final VoxelShape STANDARD_EAST = rotateShape(Direction.WEST, Direction.EAST, STANDARD);
    public static final VoxelShape STANDARD_NORTH = rotateShape(Direction.WEST, Direction.NORTH, STANDARD);
    public static final VoxelShape MIDDLE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, MIDDLE);
    public static final VoxelShape MIDDLE_EAST = rotateShape(Direction.WEST, Direction.EAST, MIDDLE);
    public static final VoxelShape MIDDLE_NORTH = rotateShape(Direction.WEST, Direction.NORTH, MIDDLE);
    public static final VoxelShape OUTER_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, OUTER);
    public static final VoxelShape OUTER_EAST = rotateShape(Direction.WEST, Direction.EAST, OUTER);
    public static final VoxelShape OUTER_NORTH = rotateShape(Direction.WEST, Direction.NORTH, OUTER);
    public static final VoxelShape INNER_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, INNER);
    public static final VoxelShape INNER_EAST = rotateShape(Direction.WEST, Direction.EAST, INNER);
    public static final VoxelShape INNER_NORTH = rotateShape(Direction.WEST, Direction.NORTH, INNER);
    public static final VoxelShape RIGHT_EDGE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, RIGHT_EDGE);
    public static final VoxelShape RIGHT_EDGE_EAST = rotateShape(Direction.WEST, Direction.EAST, RIGHT_EDGE);
    public static final VoxelShape RIGHT_EDGE_NORTH = rotateShape(Direction.WEST, Direction.NORTH, RIGHT_EDGE);
    public static final VoxelShape LEFT_EDGE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, LEFT_EDGE);
    public static final VoxelShape LEFT_EDGE_EAST = rotateShape(Direction.WEST, Direction.EAST, LEFT_EDGE);
    public static final VoxelShape LEFT_EDGE_NORTH = rotateShape(Direction.WEST, Direction.NORTH, LEFT_EDGE);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        ArmChairShape shape = state.get(SHAPE);
        switch (shape) {
            case STRAIGHT:
                return switch(dir){
                    case NORTH -> STANDARD_NORTH;
                    case SOUTH -> STANDARD_SOUTH;
                    case EAST -> STANDARD_EAST;
                    default -> STANDARD;
                };
            case MIDDLE:
                return switch(dir){
                    case NORTH -> MIDDLE_NORTH;
                    case SOUTH -> MIDDLE_SOUTH;
                    case EAST -> MIDDLE_EAST;
                    default -> MIDDLE;
                };
            case OUTER_LEFT:
                return switch(dir){
                    case NORTH -> OUTER;
                    case SOUTH -> OUTER_EAST;
                    case EAST -> OUTER_NORTH;
                    default -> OUTER_SOUTH;
                };
            case OUTER_RIGHT:
                return switch(dir){
                    case NORTH -> OUTER_NORTH;
                    case SOUTH -> OUTER_SOUTH;
                    case EAST -> OUTER_EAST;
                    default -> OUTER;
                };
            case LEFT_EDGE:
                return switch(dir){
                    case NORTH -> LEFT_EDGE_NORTH;
                    case SOUTH -> LEFT_EDGE_SOUTH;
                    case EAST -> LEFT_EDGE_EAST;
                    default -> LEFT_EDGE;
                };
            case RIGHT_EDGE:
                return switch(dir){
                    case NORTH -> RIGHT_EDGE_NORTH;
                    case SOUTH -> RIGHT_EDGE_SOUTH;
                    case EAST -> RIGHT_EDGE_EAST;
                    default -> RIGHT_EDGE;
                };
            case INNER_RIGHT:
                return switch(dir){
                    case NORTH -> INNER_EAST;
                    case SOUTH -> INNER;
                    case EAST -> INNER_SOUTH;
                    default -> INNER_NORTH;
                };
            case INNER_LEFT:
                return switch(dir){
                    case NORTH -> INNER_NORTH;
                    case SOUTH -> INNER_SOUTH;
                    case EAST -> INNER_EAST;
                    default -> INNER;
                };
            default:
                return STANDARD;
        }
    }

}
