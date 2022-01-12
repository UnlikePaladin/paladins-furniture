package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ClassicChair extends BasicChair {
    public float height = 0.36f;


    public ClassicChair(Settings settings) {
        super(settings);
    setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));

    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(2, 0, 2.05 ,4, 24, 4.05), createCuboidShape(2, 0, 11.9, 4, 24, 13.9), createCuboidShape(12, 0, 11.9, 14, 9, 13.9), createCuboidShape(12, 0, 2.05, 14, 9, 4.05), createCuboidShape(2.2, 18.32, 3.9, 3.6, 19.32, 12.9) ,  createCuboidShape(2, 7.85, 4.05, 4, 10.15, 12), createCuboidShape(2.28, 10.14, 9.8, 3.58, 19.14, 10.8), createCuboidShape(2.28, 10.14, 7.5, 3.58, 19.14, 8.5), createCuboidShape(2.28, 10.14, 5, 3.58, 19.14, 6), createCuboidShape(2, 21, 4.02,4, 24, 11.92), createCuboidShape(3.85, 8.75, 1.98, 14.95, 10.25, 13.96), createCuboidShape(3.8, 7.86, 1.97, 15, 9.26, 13.97));
    protected static final VoxelShape FACE_SOUTH = VoxelShapes.union(createCuboidShape(2.05, 0, 12,4.05, 24, 14), createCuboidShape(11.9, 0, 12, 13.9, 24, 14), createCuboidShape(2.05, 0, 2, 4.05, 9, 4), createCuboidShape(11.9, 0, 2, 13.9, 9, 4), createCuboidShape(3.9, 18.32, 12.4, 12.9, 19.32, 13.8), createCuboidShape(4.05, 7.85, 12, 12, 10.15, 14 ), createCuboidShape(9.8, 10.14, 12.42, 10.8, 19.14, 13.72), createCuboidShape(7.5, 10.14, 12.42,8.5, 19.14, 13.72), createCuboidShape(5, 10.14, 12.42, 6, 19.14, 13.72), createCuboidShape(4.02, 21, 12, 11.92, 24, 14), createCuboidShape(1.98, 8.75, 1.05, 13.96, 10.25, 12.15), createCuboidShape(1.97, 7.86, 1, 13.97, 9.26, 12.2));
    protected static final VoxelShape FACE_NORTH = VoxelShapes.union(createCuboidShape(11.95, 0, 2 ,13.95, 24, 4), createCuboidShape(2.1, 0, 2, 4.1, 24, 4), createCuboidShape(2.1, 0, 12, 4.1, 9, 14), createCuboidShape(11.95, 0, 12, 13.95, 9, 14), createCuboidShape(3.1, 18.32, 2.2, 12.1, 19.32, 3.6 ), createCuboidShape(4, 7.85, 2, 11.95, 10.15, 4), createCuboidShape(5.2, 10.14, 2.28, 6.2, 19.14, 3.58), createCuboidShape(7.5, 10.14, 2.28, 8.5, 19.14, 3.58), createCuboidShape(10, 10.14, 2.2, 11, 19.14, 3.58), createCuboidShape(4.08, 21, 2, 11.98, 24, 4), createCuboidShape(2.04, 8.75, 3.85, 14.02, 10.25, 14.95), createCuboidShape(2.03, 7.86, 3.8, 14.03, 9.26, 15));
    protected static final VoxelShape FACE_EAST = VoxelShapes.union(createCuboidShape(12, 0, 11.95,14, 24, 13.95),createCuboidShape(12, 0, 2.1,14, 24, 4.1), createCuboidShape(2, 0, 11.95, 4, 9, 13.95), createCuboidShape(2, 0, 2.1, 4, 9, 4.1), createCuboidShape(12.4, 18.32, 3.1, 13.8, 19.32, 12.1), createCuboidShape(12, 7.85, 4, 14, 10.15, 11.95), createCuboidShape(12.42, 10.14, 5.2, 13.72, 19.14, 6.2), createCuboidShape(12.42, 10.14, 7.5, 13.72, 19.14, 8.5), createCuboidShape(12.42, 10.14, 10, 13.72, 19.14, 11), createCuboidShape(12, 21, 4.08, 14, 24, 11.98), createCuboidShape(1.05, 8.75, 2.04, 12.15, 10.25, 14.02), createCuboidShape(1, 7.86, 2.03, 12.2, 9.26, 14.03));
    @SuppressWarnings("deprecated")
    @Override

        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case WEST:
                return FACE_WEST;
            case NORTH:
                return FACE_NORTH;
            case SOUTH:
                return FACE_SOUTH;
            case EAST:
            default:
                return FACE_EAST;


        }
    }


}

