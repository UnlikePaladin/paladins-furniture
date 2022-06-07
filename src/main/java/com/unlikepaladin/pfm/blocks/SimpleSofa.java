package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class SimpleSofa extends ArmChairDyeable{
    public SimpleSofa(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canConnect(BlockView world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        boolean canConnect = (state.getBlock() instanceof SimpleSofa);
        return canConnect;
    }

    public VoxelShape STANDARD = VoxelShapes.union(createCuboidShape(11.7, 0, 12,14.2, 3, 14.5),createCuboidShape(11.7, 0, 1.5,14.2, 3, 4),createCuboidShape(0.7, 0, 1.5,3.2, 3, 4),createCuboidShape(0.7, 0, 12,3.2, 3, 14.5),createCuboidShape(5, 9.5, 13,16, 13.71, 16),createCuboidShape(5, 9.5, 0,16, 13.71, 3),createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 16));
    public VoxelShape OUTER = VoxelShapes.union(createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 5),createCuboidShape(11.7, 0, 12,14.2, 3, 14.5),createCuboidShape(0.7, 0, 1.5,3.2, 3, 4));
    public VoxelShape MIDDLE = VoxelShapes.union(createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 16));
    public VoxelShape INNER = VoxelShapes.union(createCuboidShape(11.7, 0, 1.5,14.2, 3, 4),createCuboidShape(0.7, 0, 12,3.2, 3, 14.5),createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 11),createCuboidShape(0, 9.5, 11,16, 19.51, 16));
    public VoxelShape RIGHT_EDGE = VoxelShapes.union(createCuboidShape(11.7, 0, 1.5,14.2, 3, 4),createCuboidShape(0.7, 0, 1.5,3.2, 3, 4),createCuboidShape(5, 9.5, 0,16, 13.71, 3),createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 16));
    public VoxelShape LEFT_EDGE = VoxelShapes.union(createCuboidShape(0.7, 0, 12,3.2, 3, 14.5),createCuboidShape(11.7, 0, 12,14.2, 3, 14.5),createCuboidShape(5, 9.5, 13,16, 13.71, 16),createCuboidShape(0, 2, 0,16, 9.51, 16),createCuboidShape(0, 9.5, 0,5, 19.51, 16));

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        ArmChairShape shape = state.get(SHAPE);
        switch (shape) {
            case STRAIGHT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.EAST, STANDARD);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.NORTH, STANDARD);
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.SOUTH, STANDARD);
                else
                    return STANDARD;
            case MIDDLE:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.EAST, MIDDLE);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.NORTH, MIDDLE);
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.SOUTH, MIDDLE);
                else
                    return MIDDLE;
            case OUTER_LEFT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.NORTH, OUTER);
                if (dir.equals(Direction.NORTH))
                    return OUTER;
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.EAST, OUTER);
                else
                    return rotateShape(Direction.WEST, Direction.SOUTH, OUTER);
                case OUTER_RIGHT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.EAST, OUTER);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.NORTH, OUTER);
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.SOUTH, OUTER);
                else
                    return OUTER;
                case LEFT_EDGE:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.EAST, LEFT_EDGE);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.NORTH, LEFT_EDGE);
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.SOUTH, LEFT_EDGE);
                else
                    return LEFT_EDGE;
            case RIGHT_EDGE:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.EAST, RIGHT_EDGE);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.NORTH, RIGHT_EDGE);
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.SOUTH, RIGHT_EDGE);
                else
                    return RIGHT_EDGE;
            case INNER_RIGHT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.SOUTH, INNER);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.EAST, INNER);
                if (dir.equals(Direction.SOUTH))
                    return INNER;
                else
                    return rotateShape(Direction.WEST, Direction.NORTH, INNER);
            case INNER_LEFT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.EAST, INNER);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.NORTH, INNER);
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.SOUTH, INNER);
                else
                    return INNER;
            default:
                return STANDARD;
        }
    }

}
