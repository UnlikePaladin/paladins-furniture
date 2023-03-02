package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FroggyChairBlock extends BasicChairBlock {
    public float height = 0.36f;

    private static final List<FurnitureBlock> FROGGY_CHAIRS = new ArrayList<>();
    public FroggyChairBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(TUCKED, false));
        if (this.getClass().isAssignableFrom(FroggyChairBlock.class)){
            FROGGY_CHAIRS.add(new FurnitureBlock(this, "froggy_chair_"));
        }
    }

    public static Stream<FurnitureBlock> streamFroggyChair() {
        return FROGGY_CHAIRS.stream();
    }

    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(0.3, 10.5, 1.6,2.6, 22.5, 14.6), createCuboidShape(0.3, 22.5, 9.6, 2.6, 24.5, 13.6), createCuboidShape(0.3, 22.5, 2.6, 2.6, 24.5, 6.6), createCuboidShape(2.8, 10.5, 3.1, 12.8, 11, 13.1), createCuboidShape(0.3, 8, 1.6,14.3, 10.5, 14.6), createCuboidShape(11, 0, 11.5, 13.5, 8, 14), createCuboidShape(1, 0, 2.5, 3.5, 8, 5), createCuboidShape(1, 0, 11.5, 3.5, 8, 14), createCuboidShape(11, 0, 2.5, 13.5, 8, 5));
    protected static final VoxelShape FACE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, FACE_WEST);
    protected static final VoxelShape FACE_NORTH = rotateShape(Direction.WEST, Direction.NORTH, FACE_WEST);
    protected static final VoxelShape FACE_EAST = rotateShape(Direction.WEST, Direction.EAST, FACE_WEST);
    protected static final VoxelShape FACE_NORTH_TUCKED = tuckShape(Direction.NORTH, FACE_NORTH);
    protected static final VoxelShape FACE_SOUTH_TUCKED = tuckShape(Direction.SOUTH, FACE_SOUTH);
    protected static final VoxelShape FACE_EAST_TUCKED = tuckShape(Direction.EAST, FACE_EAST);
    protected static final VoxelShape FACE_WEST_TUCKED = tuckShape(Direction.WEST, FACE_WEST);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        if (state.get(TUCKED)) {
            return switch (dir) {
                case WEST -> FACE_WEST_TUCKED;
                case NORTH -> FACE_NORTH_TUCKED;
                case SOUTH -> FACE_SOUTH_TUCKED;
                default -> FACE_EAST_TUCKED;
            };
        }
        return switch (dir) {
            case WEST -> FACE_WEST;
            case NORTH -> FACE_NORTH;
            case SOUTH -> FACE_SOUTH;
            default -> FACE_EAST;
        };
    }
}

