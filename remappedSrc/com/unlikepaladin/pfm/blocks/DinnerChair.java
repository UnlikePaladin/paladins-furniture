package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class DinnerChair extends BasicChair {
    public float height = 0.36f;

    public DinnerChair(Settings settings) {
        super(settings);
    setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }



    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());

    }
    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(2, 0, 1.625 ,4 ,21.98 ,3.625), createCuboidShape(2, 0, 12.375, 4, 21.98, 14.375), createCuboidShape(12, 0, 1.62, 14, 9, 3.62), createCuboidShape(12, 0, 12.5, 14, 9, 14.5), createCuboidShape(2.6, 12.4,3.6, 3.9, 14.4, 12.6 ) ,  createCuboidShape(2.62, 15.42, 3.6, 3.9, 21.42, 12.6), createCuboidShape(3.87, 8.8, 1.5, 14.95, 10.29, 14.5), createCuboidShape(3.8, 8, 1.5, 15, 9, 14.56));
    protected static final VoxelShape FACE_SOUTH = VoxelShapes.union(createCuboidShape(1.625, 0, 12,3.625, 21.98438, 14), createCuboidShape(12.375, 0, 12, 14.375, 21.98438, 14), createCuboidShape(12.48438, 0, 2, 14.48438, 9, 4), createCuboidShape(1.625, 0, 2, 3.625, 9, 4), createCuboidShape(3.5939, 12.42188, 12.07493, 12.5939, 14.42188, 13.37493 ), createCuboidShape(3.5939, 15.42188, 12.07493, 12.5939, 21.42188, 13.37493 ), createCuboidShape(1.5312, 8.79688, 1.025, 14.5312, 10.29688, 12.125), createCuboidShape(1.5, 8, 0.9875,14.56, 9, 12.1875) );
    protected static final VoxelShape FACE_NORTH = VoxelShapes.union(createCuboidShape(12.375, 0, 2 ,14.375, 21.98438, 4), createCuboidShape(1.625, 0, 2, 3.625, 21.98438, 4), createCuboidShape(1.51562, 0, 12, 3.51562, 9, 14), createCuboidShape(12.375, 0, 12, 14.375, 9, 14), createCuboidShape(3.4061, 12.42188, 2.62507, 12.4061, 14.42188, 3.92507 ), createCuboidShape(3.4061, 15.42188, 2.62507, 12.4061, 21.42188, 3.92507), createCuboidShape(1.4688, 8.79688, 3.875, 14.4688, 10.29688, 14.975), createCuboidShape(1.44, 8, 3.8125, 14.5, 9, 15.0125));
    protected static final VoxelShape FACE_EAST = VoxelShapes.union(createCuboidShape(12, 0, 12.375 ,14, 21.98438, 14.375),createCuboidShape(2, 0, 1.51562,4, 9, 3.51562), createCuboidShape(2, 0, 12.375, 4, 9, 14.375), createCuboidShape(12, 0, 1.625, 14, 21.98438, 3.625), createCuboidShape(12.07493, 12.42188, 3.4061, 13.37493, 14.42188, 12.4061), createCuboidShape(12.07493, 15.42188, 3.4061, 13.37493, 21.42188, 12.4061), createCuboidShape(1.025, 8.79688, 1.4688, 12.125, 10.29688, 14.4688 ), createCuboidShape(0.9875, 8, 1.44, 12.1875, 9, 14.5) );
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

