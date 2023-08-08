package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.ShowerHeadBlockEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class BasicShowerHeadBlock extends HorizontalFacingBlockWithEntity {
    public BasicShowerHeadBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, direction);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.get(FACING) && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public static final VoxelShape SHOWER_HEAD_SOUTH = VoxelShapes.union(createCuboidShape(6.5, 6.5, 15, 9.5, 9.5, 16), createCuboidShape(7.5, 7.5, 7,8.5, 8.5, 15), createCuboidShape(7.5, 5.5, 7,8.5, 7.5, 8),createCuboidShape(4.8, 4, 4.2,11.3, 5.5, 10.7));
    public static final VoxelShape SHOWER_HEAD_NORTH = rotateShape(Direction.SOUTH, Direction.NORTH, SHOWER_HEAD_SOUTH);
    public static final VoxelShape SHOWER_HEAD_EAST = rotateShape(Direction.SOUTH, Direction.EAST, SHOWER_HEAD_SOUTH);
    public static final VoxelShape SHOWER_HEAD_WEST = rotateShape(Direction.SOUTH, Direction.WEST, SHOWER_HEAD_SOUTH);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)){
            case NORTH: return SHOWER_HEAD_NORTH;
            case WEST: return SHOWER_HEAD_WEST;
            case EAST: return SHOWER_HEAD_EAST;
            default: return SHOWER_HEAD_SOUTH;
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return getBlockEntity();
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity() {
        return null;
    }
}
