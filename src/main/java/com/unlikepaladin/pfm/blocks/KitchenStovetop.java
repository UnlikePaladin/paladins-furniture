package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
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

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    protected static final VoxelShape STOVETOP = VoxelShapes.union(createCuboidShape(0, 0, 1, 16, 0.5, 15));
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case WEST:
                return rotateShape(Direction.NORTH, Direction.EAST, STOVETOP);
            case NORTH:
                return rotateShape(Direction.NORTH, Direction.SOUTH, STOVETOP);
            case SOUTH:
                return STOVETOP;
            case EAST:
            default:
                return rotateShape(Direction.NORTH, Direction.WEST, STOVETOP);
        }
    }
}
