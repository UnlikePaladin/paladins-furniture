package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class IronFridgeBlock extends FridgeBlock {
    private Supplier<Block> freezer;
    public IronFridgeBlock(Settings settings, Supplier<Block> freezer) {
        super(settings, freezer);
        this.freezer = freezer;
    }

    protected static final VoxelShape FRIDGE = VoxelShapes.union(createCuboidShape(0.7, 1, 3, 15.7, 16, 16), createCuboidShape(13.2, 1.2, 0.1,14.2, 15.2, 1.1),createCuboidShape(13.2, 1.2, 1,14.2, 2.2, 3),createCuboidShape(13.2, 14.2, 1.1,14.2, 15.2, 3.1),createCuboidShape(14, 1, 2.3,15.5, 16, 3.3),createCuboidShape(0.7, 1, 2,14.6, 16, 3));
    protected static final VoxelShape FRIDGE_OPEN = VoxelShapes.union(createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(-1.2, 1.2, -10.5,-0.2, 15.2, -9.5),createCuboidShape(-0.3, 1.2, -10.5,1.7, 2.2, -9.5),createCuboidShape(-0.3, 14.2, -10.5,1.7, 15.2, -9.5),createCuboidShape(1, 1, -11.7,2, 16, -10.3),createCuboidShape(0.7, 1, -10.9,1.7, 16, 3));
    protected static final VoxelShape FRIDGE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FRIDGE);
    protected static final VoxelShape FRIDGE_SOUTH_OPEN = rotateShape(Direction.NORTH, Direction.SOUTH, FRIDGE_OPEN);
    protected static final VoxelShape FRIDGE_EAST = rotateShape(Direction.NORTH, Direction.EAST, FRIDGE);
    protected static final VoxelShape FRIDGE_EAST_OPEN = rotateShape(Direction.NORTH, Direction.EAST, FRIDGE_OPEN);
    protected static final VoxelShape FRIDGE_WEST = rotateShape(Direction.NORTH, Direction.WEST, FRIDGE);
    protected static final VoxelShape FRIDGE_WEST_OPEN = rotateShape(Direction.NORTH, Direction.WEST, FRIDGE_OPEN);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        Boolean open = state.get(OPEN);
        switch (dir) {
            case NORTH:
                if (open)
                    return FRIDGE_SOUTH_OPEN;
                else
                    return FRIDGE_SOUTH;
            case SOUTH:
                if (open)
                    return FRIDGE_OPEN;
                else
                    return FRIDGE;
            case EAST:
                if (open)
                    return FRIDGE_WEST_OPEN;
                else
                    return FRIDGE_WEST;
            case WEST:
            default:
                if (open)
                    return FRIDGE_EAST_OPEN;
                else
                    return FRIDGE_EAST;
        }
    }

    public void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockPos;
        BlockState blockState = world.getBlockState(blockPos = pos.down());
        if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof FreezerBlock) {
            BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, NOTIFY_ALL | SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, getRawIdFromState(blockState));
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return (world.getBlockState(pos.up()).isAir()) || (world.getBlockState(pos.down()).getBlock() instanceof FreezerBlock);
    }
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos, this.freezer.get().getDefaultState().with(FACING, placer.getHorizontalFacing()), NOTIFY_ALL);
        world.setBlockState(pos.up(), this.getDefaultState().with(FACING, placer.getHorizontalFacing()), NOTIFY_ALL);
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos.up())) instanceof FridgeBlockEntity) {
            ((FridgeBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
    }
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        BlockPos blockPos;
        BlockState blockState = world.getBlockState(blockPos = pos.down());
        if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof FreezerBlock) {
            BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, NOTIFY_ALL | SKIP_DROPS);
        }
    }
}
