package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;

import java.util.function.Supplier;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class IronFreezer extends Freezer{
    private Supplier<Block> fridge;
    public IronFreezer(Settings settings, Supplier<Block> fridge) {
        super(settings, fridge);
        this.fridge = fridge;
    }
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.up()).getBlock() == this.fridge.get();
    }

    protected static final VoxelShape FREEZER = VoxelShapes.union(createCuboidShape(0.7, 1, 2,14.7, 16, 3),createCuboidShape(14.3, 1, 2.3,15.3, 16, 3.3),createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(1.7, 14, 1.06,2.7, 15, 2.06),createCuboidShape(1.7, 14, 0.06,14.2, 15, 1.06));
    protected static final VoxelShape FREEZER_OPEN = VoxelShapes.union(createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(13.7, 11, -1.4,14.7, 12.1, 3.6),createCuboidShape(0.7, 11, -1.4,1.7, 12.1, 3.6),createCuboidShape(14, 1, -1.6,15.5, 16, -0.6),createCuboidShape(0.7, 1, -2,14.6, 16, -1),createCuboidShape(1.7, 14, -2.9,2.7, 15, -0.9),createCuboidShape(1.7, 14, -3.9,14.2, 15, -2.9),createCuboidShape(13.2, 14, -3,14.2, 15, -1));
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        Boolean open = state.get(OPEN);
        switch (dir) {
            case NORTH:
                if (open)
                    return rotateShape(Direction.NORTH, Direction.SOUTH, FREEZER_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.SOUTH, FREEZER);
            case SOUTH:
                if (open)
                    return FREEZER_OPEN;
                else
                    return FREEZER;
            case EAST:
                if (open)
                    return rotateShape(Direction.NORTH, Direction.WEST, FREEZER_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.WEST, FREEZER);
            case WEST:
            default:
                if (open)
                    return rotateShape(Direction.NORTH, Direction.EAST, FREEZER_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.EAST, FREEZER);
        }
    }
    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        BlockPos blockPos;
        BlockState blockState = world.getBlockState(blockPos = pos.up());
        if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof Fridge) {
            ItemScatterer.spawn((World) world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.fridge.get().asItem()));
            BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
        }
    }
    protected void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockPos;
        BlockState blockState = world.getBlockState(blockPos = pos.up());
        if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof Fridge) {
            BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
        }
    }
}
