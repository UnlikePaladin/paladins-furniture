package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class KitchenCounterOven extends Stove{
    public KitchenCounterOven(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StoveBlockEntity(PaladinFurnitureMod.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, pos, state);
    }
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, PaladinFurnitureMod.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY);
    }

    protected static final VoxelShape COUNTER_OVEN = VoxelShapes.union(createCuboidShape(0, 1, 0, 16, 14, 14),createCuboidShape(0, 0, 0, 16, 1, 12),createCuboidShape(0, 14, 0, 16, 16, 16),createCuboidShape(1.8, 11.2, 14.54, 14.3, 11.8, 15.14),createCuboidShape(2.5, 11.2, 13.07, 3.1, 11.8, 14.57),createCuboidShape(12.6, 11.2, 13.07, 13.2, 11.8, 14.57),createCuboidShape(1.8, 1.9, 14.44, 14.3, 2.5, 15.04),createCuboidShape(2.5, 1.9, 12.47, 3.1, 2.5, 14.47),createCuboidShape(12.6, 1.9, 12.47, 13.2, 2.5, 14.47));
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case WEST:
                return rotateShape(Direction.NORTH, Direction.EAST, COUNTER_OVEN);
            case NORTH:
                return rotateShape(Direction.NORTH, Direction.SOUTH, COUNTER_OVEN);
            case SOUTH:
                return COUNTER_OVEN;
            case EAST:
            default:
                return rotateShape(Direction.NORTH, Direction.WEST, COUNTER_OVEN);
        }
    }
}
