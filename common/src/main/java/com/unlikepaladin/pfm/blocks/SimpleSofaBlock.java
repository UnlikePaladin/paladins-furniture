package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SimpleSofaBlock extends ArmChairColoredBlock {
    private static final List<FurnitureBlock> SIMPLE_SOFAS = new ArrayList<>();
    public SimpleSofaBlock(DyeColor color, Properties settings) {
        super(color, settings);
        SIMPLE_SOFAS.add(new FurnitureBlock(this, "arm_chair"));
    }
    public static Stream<FurnitureBlock> streamSimpleSofas() {
        return SIMPLE_SOFAS.stream();
    }
    @Override
    public boolean canConnect(BlockGetter world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.relative(direction));
        boolean canConnect = (state.getBlock() instanceof SimpleSofaBlock);
        return canConnect;
    }
    @Override
    public boolean isArmChair(BlockState state) {
        return(state.getBlock().getClass().isAssignableFrom(SimpleSofaBlock.class) && state.getBlock() instanceof SimpleSofaBlock);
    }

    public static final VoxelShape STANDARD = Shapes.or(box(11.7, 0, 12,14.2, 3, 14.5),box(11.7, 0, 1.5,14.2, 3, 4),box(0.7, 0, 1.5,3.2, 3, 4),box(0.7, 0, 12,3.2, 3, 14.5),box(5, 9.5, 13,16, 13.71, 16),box(5, 9.5, 0,16, 13.71, 3),box(0, 2, 0,16, 9.51, 16),box(0, 9.5, 0,5, 19.51, 16));
    public static final VoxelShape OUTER = Shapes.or(box(0, 2, 0,16, 9.51, 16),box(0, 9.5, 0,5, 19.51, 5),box(11.7, 0, 12,14.2, 3, 14.5),box(0.7, 0, 1.5,3.2, 3, 4));
    public static final VoxelShape MIDDLE = Shapes.or(box(0, 2, 0,16, 9.51, 16),box(0, 9.5, 0,5, 19.51, 16));
    public static final VoxelShape INNER = Shapes.or(box(11.7, 0, 1.5,14.2, 3, 4),box(0.7, 0, 12,3.2, 3, 14.5),box(0, 2, 0,16, 9.51, 16),box(0, 9.5, 0,5, 19.51, 11),box(0, 9.5, 11,16, 19.51, 16));
    public static final VoxelShape RIGHT_EDGE = Shapes.or(box(11.7, 0, 1.5,14.2, 3, 4),box(0.7, 0, 1.5,3.2, 3, 4),box(5, 9.5, 0,16, 13.71, 3),box(0, 2, 0,16, 9.51, 16),box(0, 9.5, 0,5, 19.51, 16));
    public static final VoxelShape LEFT_EDGE = Shapes.or(box(0.7, 0, 12,3.2, 3, 14.5),box(11.7, 0, 12,14.2, 3, 14.5),box(5, 9.5, 13,16, 13.71, 16),box(0, 2, 0,16, 9.51, 16),box(0, 9.5, 0,5, 19.51, 16));

    public static final VoxelShape STANDARD_SOUTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.SOUTH, STANDARD);
    public static final VoxelShape STANDARD_EAST = PFMShapeUtil.rotateShape(Direction.WEST, Direction.EAST, STANDARD);
    public static final VoxelShape STANDARD_NORTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.NORTH, STANDARD);
    public static final VoxelShape MIDDLE_SOUTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.SOUTH, MIDDLE);
    public static final VoxelShape MIDDLE_EAST = PFMShapeUtil.rotateShape(Direction.WEST, Direction.EAST, MIDDLE);
    public static final VoxelShape MIDDLE_NORTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.NORTH, MIDDLE);
    public static final VoxelShape OUTER_SOUTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.SOUTH, OUTER);
    public static final VoxelShape OUTER_EAST = PFMShapeUtil.rotateShape(Direction.WEST, Direction.EAST, OUTER);
    public static final VoxelShape OUTER_NORTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.NORTH, OUTER);
    public static final VoxelShape INNER_SOUTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.SOUTH, INNER);
    public static final VoxelShape INNER_EAST = PFMShapeUtil.rotateShape(Direction.WEST, Direction.EAST, INNER);
    public static final VoxelShape INNER_NORTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.NORTH, INNER);
    public static final VoxelShape RIGHT_EDGE_SOUTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.SOUTH, RIGHT_EDGE);
    public static final VoxelShape RIGHT_EDGE_EAST = PFMShapeUtil.rotateShape(Direction.WEST, Direction.EAST, RIGHT_EDGE);
    public static final VoxelShape RIGHT_EDGE_NORTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.NORTH, RIGHT_EDGE);
    public static final VoxelShape LEFT_EDGE_SOUTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.SOUTH, LEFT_EDGE);
    public static final VoxelShape LEFT_EDGE_EAST = PFMShapeUtil.rotateShape(Direction.WEST, Direction.EAST, LEFT_EDGE);
    public static final VoxelShape LEFT_EDGE_NORTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.NORTH, LEFT_EDGE);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        ArmChairShape shape = state.getValue(SHAPE);
        switch (shape) {
            case STRAIGHT:
                switch (dir) {
                    case NORTH:
                        return STANDARD_NORTH;
                    case SOUTH:
                        return STANDARD_SOUTH;
                    case EAST:
                        return STANDARD_EAST;
                    default:
                        return STANDARD;
                }
            case MIDDLE:
                switch (dir) {
                    case NORTH:
                        return MIDDLE_NORTH;
                    case SOUTH:
                        return MIDDLE_SOUTH;
                    case EAST:
                        return MIDDLE_EAST;
                    default:
                        return MIDDLE;
                }
            case OUTER_LEFT:
                switch (dir) {
                    case NORTH:
                        return OUTER;
                    case SOUTH:
                        return OUTER_EAST;
                    case EAST:
                        return OUTER_NORTH;
                    default:
                        return OUTER_SOUTH;
                }
            case OUTER_RIGHT:
                switch (dir) {
                    case NORTH:
                        return OUTER_NORTH;
                    case SOUTH:
                        return OUTER_SOUTH;
                    case EAST:
                        return OUTER_EAST;
                    default:
                        return OUTER;
                }
            case LEFT_EDGE:
                switch (dir) {
                    case NORTH:
                        return LEFT_EDGE_NORTH;
                    case SOUTH:
                        return LEFT_EDGE_SOUTH;
                    case EAST:
                        return LEFT_EDGE_EAST;
                    default:
                        return LEFT_EDGE;
                }
            case RIGHT_EDGE:
                switch (dir) {
                    case NORTH:
                        return RIGHT_EDGE_NORTH;
                    case SOUTH:
                        return RIGHT_EDGE_SOUTH;
                    case EAST:
                        return RIGHT_EDGE_EAST;
                    default:
                        return RIGHT_EDGE;
                }
            case INNER_RIGHT:
                switch (dir) {
                    case NORTH:
                        return INNER_EAST;
                    case SOUTH:
                        return INNER;
                    case EAST:
                        return INNER_SOUTH;
                    default:
                        return INNER_NORTH;
                }
            case INNER_LEFT:
                switch (dir) {
                    case NORTH:
                        return INNER_NORTH;
                    case SOUTH:
                        return INNER_SOUTH;
                    case EAST:
                        return INNER_EAST;
                    default:
                        return INNER;
                }
            default:
                return STANDARD;
        }
    }

}
