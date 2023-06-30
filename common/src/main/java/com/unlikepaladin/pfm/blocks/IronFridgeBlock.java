package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class IronFridgeBlock extends FridgeBlock {
    private Supplier<FreezerBlock> freezer;
    public IronFridgeBlock(Settings settings, Supplier<FreezerBlock> freezer) {
        super(settings, freezer);
        this.freezer = freezer;
    }

    protected static final Map<Direction, VoxelShape> FRIDGE_SINGLE = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.7, 1, 3,15.7, 16, 16),createCuboidShape(13.71, 1, 2.609,14.69, 14.7, 3.609),createCuboidShape(13.2, 2.188, 0.094,14.2, 14.188, 1.094),createCuboidShape(13.2, 2.188, 0.984,14.2, 3.188, 2.984),createCuboidShape(13.2, 13.188, 1.063,14.2, 14.188, 3.063),createCuboidShape(14.247, 1.01, 2.137,15.247, 16, 3.557),createCuboidShape(0.7, 1, 2,14.6, 16, 3),createCuboidShape(0.71, 0.064, 2.609,14.69, 1.139, 15.909),createCuboidShape(14.71, 0.064, 3.509,15.69, 1.139, 15.909)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_TOP = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(13.2, -0.012, 0.094,14.2, 15.188, 1.094),createCuboidShape(13.2, 14.188, 1.063,14.2, 15.188, 3.063),createCuboidShape(14.247, 0, 2.137,15.247, 16, 3.557),createCuboidShape(0.7, 0, 2,14.6, 16, 3)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(13.2, 0.188, 0.094,14.2, 16.188, 1.094),createCuboidShape(14.247, 0, 2.137,15.247, 16, 3.557),createCuboidShape(0.7, 0, 2,14.6, 16, 3)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_BOTTOM = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(13.2, 2.188, 0.094,14.2, 16.188, 1.094),createCuboidShape(13.2, 2.188, 1.063,14.2, 3.188, 3.063),createCuboidShape(14.247, 1, 2.137,15.247, 16, 3.557),createCuboidShape(0.7, 1, 2,14.6, 16, 3),createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(0.71, 0.025, 2.609,14.69, 1.1, 3.609)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.7, 1, 3, 15.7, 16, 16), createCuboidShape(13.2, 1.2, 0.1,14.2, 15.2, 1.1),createCuboidShape(13.2, 1.2, 1,14.2, 2.2, 3),createCuboidShape(13.2, 14.2, 1.1,14.2, 15.2, 3.1),createCuboidShape(14, 1, 2.3,15.5, 16, 3.3),createCuboidShape(0.7, 1, 2,14.6, 16, 3)));}};

    protected static final Map<Direction, VoxelShape> FRIDGE_SINGLE_OPEN = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.7, 1, 3,15.7, 16, 16),createCuboidShape(1.4, 2.5, -10.7,4.4, 5.5, 2.3),createCuboidShape(1.4, 9.5, -10.7,4.4, 12.5, 2.3),createCuboidShape(0.71, 0.1, 2.609,15.39, 15.8, 4.009),createCuboidShape(-1.228, 2.188, -10.481,-0.228, 14.188, -9.481),createCuboidShape(-0.338, 2.188, -10.481,1.662, 3.188, -9.481),createCuboidShape(-0.259, 13.188, -10.481,1.641, 14.188, -9.481),createCuboidShape(0.815, 1.01, -11.528,2.235, 16, -10.528),createCuboidShape(0.678, 1, -10.881,1.678, 16, 3.019),createCuboidShape(14.71, 0.064, 3.509,15.69, 1.139, 15.909),createCuboidShape(0.71, 0.064, 2.584,14.69, 1.139, 15.909)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_TOP_OPEN = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(0.71, 0.1, 2.609,15.39, 15.8, 4.009),createCuboidShape(-1.228, 0.188, -10.481,-0.228, 15.188, -9.481),createCuboidShape(-0.259, 14.188, -10.481,1.641, 15.188, -9.481),createCuboidShape(0.815, 0, -11.528,2.235, 16, -10.528),createCuboidShape(0.678, 0, -10.881,1.678, 16, 3.019),createCuboidShape(1.4, 9.5, -10.7,4.4, 12.5, 2.3),createCuboidShape(1.4, 2.5, -10.7,4.4, 5.5, 2.3)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE_OPEN = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(1.4, 9.5, -10.7,4.4, 12.5, 2.3),createCuboidShape(1.4, 2.5, -10.7,4.4, 5.5, 2.3),createCuboidShape(-1.228, 0.188, -10.481,-0.228, 16.188, -9.481),createCuboidShape(0.815, 0, -11.528,2.235, 16, -10.528),createCuboidShape(0.678, 0, -10.881,1.678, 16, 3.019),createCuboidShape(0.71, 0, 2.609,15.39, 16, 4.009),createCuboidShape(0.7, 0, 3,15.7, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_BOTTOM_OPEN = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(-1.228, 2.188, -10.481,-0.228, 16.188, -9.481),createCuboidShape(-0.259, 2.188, -10.481,1.541, 3.188, -9.481),createCuboidShape(0.815, 1, -11.528,2.235, 16, -10.528),createCuboidShape(0.678, 1, -10.881,1.678, 16, 3.019),createCuboidShape(0.71, 0.1, 2.609,15.39, 15.988, 4.009),createCuboidShape(1.4, 9.5, -10.7,4.4, 12.5, 2.3),createCuboidShape(1.4, 2.5, -10.7,4.4, 5.5, 2.3)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_OPEN = new HashMap<>(){{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.7, 0, 3,15.7, 16, 16),createCuboidShape(-1.2, 1.2, -10.5,-0.2, 15.2, -9.5),createCuboidShape(-0.3, 1.2, -10.5,1.7, 2.2, -9.5),createCuboidShape(-0.3, 14.2, -10.5,1.7, 15.2, -9.5),createCuboidShape(1, 1, -11.7,2, 16, -10.3),createCuboidShape(0.7, 1, -10.9,1.7, 16, 3)));}};


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING).getOpposite();
        Boolean open = state.get(OPEN);
        boolean bottom = state.isOf(world.getBlockState(pos.up()).getBlock());
        boolean top = state.isOf(world.getBlockState(pos.down()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.down()).getBlock() instanceof IronFreezerBlock;

        if (top && bottom) {
            if (open) {
                if (!FRIDGE_MIDDLE_OPEN.containsKey(dir))
                    FRIDGE_MIDDLE_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_MIDDLE_OPEN.get(Direction.NORTH)));
                return FRIDGE_MIDDLE_OPEN.get(dir);
            }
            if (!FRIDGE_MIDDLE.containsKey(dir))
                FRIDGE_MIDDLE.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_MIDDLE.get(Direction.NORTH)));
            return FRIDGE_MIDDLE.get(dir);
        } else if (bottom) {
            if (open) {
                if (!FRIDGE_BOTTOM_OPEN.containsKey(dir))
                    FRIDGE_BOTTOM_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_BOTTOM_OPEN.get(Direction.NORTH)));
                return FRIDGE_BOTTOM_OPEN.get(dir);
            }
            if (!FRIDGE_BOTTOM.containsKey(dir))
                FRIDGE_BOTTOM.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_BOTTOM.get(Direction.NORTH)));
            return FRIDGE_BOTTOM.get(dir);
        } else if (top) {
            if (open) {
                if (!FRIDGE_TOP_OPEN.containsKey(dir))
                    FRIDGE_TOP_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_TOP_OPEN.get(Direction.NORTH)));
                return FRIDGE_TOP_OPEN.get(dir);
            }
            if (!FRIDGE_TOP.containsKey(dir))
                FRIDGE_TOP.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_TOP.get(Direction.NORTH)));
            return FRIDGE_TOP.get(dir);
        } else if (hasFreezer) {
            if (open) {
                if (!FRIDGE_OPEN.containsKey(dir))
                    FRIDGE_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_OPEN.get(Direction.NORTH)));
                return FRIDGE_OPEN.get(dir);
            }
            if (!FRIDGE.containsKey(dir))
                FRIDGE.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE.get(Direction.NORTH)));
            return FRIDGE.get(dir);
        } else {
            if (open) {
                if (!FRIDGE_SINGLE_OPEN.containsKey(dir))
                    FRIDGE_SINGLE_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_SINGLE_OPEN.get(Direction.NORTH)));
                return FRIDGE_SINGLE_OPEN.get(dir);
            }
            if (!FRIDGE_SINGLE.containsKey(dir))
                FRIDGE_SINGLE.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_SINGLE.get(Direction.NORTH)));
            return FRIDGE_SINGLE.get(dir);
        }
    }

    public void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos);
    }
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
    }
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
    }
}
