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

    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(0.8, 9.5, 2.6,2.6, 19.5, 13.6), createCuboidShape(0.8, 17.5, 10.6,2.6, 21.5, 14.6), createCuboidShape(0.8, 17.5, 1.6, 2.6, 21.5, 5.6), createCuboidShape(0.8, 8, 2.6,13.3, 9.5, 13.6), createCuboidShape(12, 0, 1.5,14, 8, 3.5), createCuboidShape(11.5, 6.5, 2.5, 13.5, 8, 4.5), createCuboidShape(11.5, 6.5, 11.5, 13.5, 8, 13.5), createCuboidShape(12, 0, 12.5, 14, 8, 14.5), createCuboidShape(1.5, 6.5, 11, 3.5, 8, 13), createCuboidShape(0.5, 0, 12.5,2.5, 8, 14.5), createCuboidShape(0.5, 0, 1.5,2.5, 8, 3.5), createCuboidShape(1.5, 6.5, 2.5,3.5, 8, 4.5));
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
            switch (dir) {
                case WEST:
                    return FACE_WEST_TUCKED;
                case NORTH:
                    return FACE_NORTH_TUCKED;
                case SOUTH:
                    return FACE_SOUTH_TUCKED;
                default:
                    return FACE_EAST_TUCKED;
            }
        }
        switch (dir) {
            case WEST:
                return FACE_WEST;
            case NORTH:
                return FACE_NORTH;
            case SOUTH:
                return FACE_SOUTH;
            default:
                return FACE_EAST;
        }
    }
}

