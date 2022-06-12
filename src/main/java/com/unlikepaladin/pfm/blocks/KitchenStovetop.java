package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class KitchenStovetop extends HorizontalFacingBlock {
    public KitchenStovetop(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Block neighborBlock = world.getBlockState(pos.down()).getBlock();
        return neighborBlock instanceof KitchenCounterOven || neighborBlock instanceof KitchenCounter;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !this.canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    protected static final VoxelShape STOVETOP = VoxelShapes.union(createCuboidShape(0, 0, 1, 16, 0.5, 15));
    protected static final VoxelShape STOVETOP_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, STOVETOP);
    protected static final VoxelShape STOVETOP_EAST = rotateShape(Direction.NORTH, Direction.EAST, STOVETOP);
    protected static final VoxelShape STOVETOP_WEST = rotateShape(Direction.NORTH, Direction.WEST, STOVETOP);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case WEST -> STOVETOP_EAST;
            case NORTH -> STOVETOP_SOUTH;
            case SOUTH -> STOVETOP;
            default -> STOVETOP_WEST;
        };
    }
}
