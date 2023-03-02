package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class IronFreezerBlock extends FreezerBlock {
    private Supplier<Block> fridge;
    public IronFreezerBlock(Settings settings, Supplier<Block> fridge) {
        super(settings, fridge);
        this.fridge = fridge;
    }
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.up()).getBlock() == this.fridge.get();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FreezerBlockEntity(BlockEntities.IRON_FREEZER_BLOCK_ENTITY, pos,state);
    }

    protected static final VoxelShape FREEZER = VoxelShapes.union(createCuboidShape(0.7, 1, 2,14.7, 16, 3),createCuboidShape(14.3, 1, 2.3,15.3, 16, 3.3),createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(1.7, 14, 1.06,2.7, 15, 2.06),createCuboidShape(1.7, 14, 0.06,14.2, 15, 1.06));
    protected static final VoxelShape FREEZER_OPEN = VoxelShapes.union(createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(13.7, 11, -1.4,14.7, 12.1, 3.6),createCuboidShape(0.7, 11, -1.4,1.7, 12.1, 3.6),createCuboidShape(14, 1, -1.6,15.5, 16, -0.6),createCuboidShape(0.7, 1, -2,14.6, 16, -1),createCuboidShape(1.7, 14, -2.9,2.7, 15, -0.9),createCuboidShape(1.7, 14, -3.9,14.2, 15, -2.9),createCuboidShape(13.2, 14, -3,14.2, 15, -1));

    protected static final VoxelShape FREEZER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FREEZER);
    protected static final VoxelShape FREEZER_SOUTH_OPEN = rotateShape(Direction.NORTH, Direction.SOUTH, FREEZER_OPEN);
    protected static final VoxelShape FREEZER_EAST = rotateShape(Direction.NORTH, Direction.EAST, FREEZER);
    protected static final VoxelShape FREEZER_EAST_OPEN = rotateShape(Direction.NORTH, Direction.EAST, FREEZER_OPEN);
    protected static final VoxelShape FREEZER_WEST = rotateShape(Direction.NORTH, Direction.WEST, FREEZER);
    protected static final VoxelShape FREEZER_WEST_OPEN = rotateShape(Direction.NORTH, Direction.WEST, FREEZER_OPEN);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        Boolean open = state.get(OPEN);
        switch (dir) {
            case NORTH:
                if (open)
                    return FREEZER_SOUTH_OPEN;
                else
                    return FREEZER_SOUTH;
            case SOUTH:
                if (open)
                    return FREEZER_OPEN;
                else
                    return FREEZER;
            case EAST:
                if (open)
                    return FREEZER_WEST_OPEN;
                else
                    return FREEZER_WEST;
            case WEST:
            default:
                if (open)
                    return FREEZER_EAST_OPEN;
                else
                    return FREEZER_EAST;
        }
    }
    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        BlockPos blockPos;
        BlockState blockState = world.getBlockState(blockPos = pos.up());
        if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof FridgeBlock) {
            ItemScatterer.spawn((World) world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.fridge.get().asItem()));
            BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, NOTIFY_ALL | SKIP_DROPS);
        }
    }
    protected void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockPos;
        BlockState blockState = world.getBlockState(blockPos = pos.up());
        if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof FridgeBlock) {
            BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, NOTIFY_ALL | SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, getRawIdFromState(blockState));
        }
    }
}
