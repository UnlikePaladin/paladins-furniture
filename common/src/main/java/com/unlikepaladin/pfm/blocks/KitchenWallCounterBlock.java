package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class KitchenWallCounterBlock extends KitchenCounterBlock{

    private static final List<FurnitureBlock> WOOD_COUNTERS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_COUNTERS = new ArrayList<>();
    public KitchenWallCounterBlock(Properties settings) {
        super(settings);
        if(AbstractSittableBlock.isWoodBased(this.getDefaultState()) && this.getClass().isAssignableFrom(KitchenWallCounterBlock.class)){
            WOOD_COUNTERS.add(new FurnitureBlock(this, "kitchen_wall_counter"));
        }
        else if (this.getClass().isAssignableFrom(KitchenWallCounterBlock.class)){
            STONE_COUNTERS.add(new FurnitureBlock(this, "kitchen_wall_counter"));
        }
    }

    public static Stream<FurnitureBlock> streamWallWoodCounters() {
        return WOOD_COUNTERS.stream();
    }
    public static Stream<FurnitureBlock> streamWallStoneCounters() {
        return STONE_COUNTERS.stream();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(KitchenCounterBlock.FACING);
        BlockState neighborStateFacing = view.getBlockState(pos.relative(direction));
        BlockState neighborStateOpposite = view.getBlockState(pos.relative(direction.getOpposite()));
        if (canConnectToCounter(neighborStateFacing) && neighborStateFacing.getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(state, view, pos, direction2.getOpposite())) {
                if (direction2 == direction.getCounterClockWise()) {
                    switch (direction) {
                        case NORTH: return OUTER_MIDDLE;
                        case SOUTH: return OUTER_MIDDLE_SOUTH;
                        case EAST: return OUTER_MIDDLE_EAST;
                        default: return OUTER_MIDDLE_WEST;
                    }
                }
                else {
                    switch (direction) {
                        case NORTH: return OUTER_MIDDLE_EAST;
                        case SOUTH: return OUTER_MIDDLE_WEST;
                        case EAST: return OUTER_MIDDLE_SOUTH;
                        default: return OUTER_MIDDLE;
                    }
                }
            } else {
                switch (direction) {
                    case NORTH: return MIDDLE;
                    case SOUTH: return MIDDLE_SOUTH;
                    case EAST: return MIDDLE_EAST;
                    default: return MIDDLE_WEST;
                }
            }
        }
        else if (canConnectToCounter(neighborStateOpposite) && neighborStateOpposite.getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction3;
            if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
            }
            else {
                direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
            }
            if (direction3.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(state, view, pos, direction3)) {
                if (direction3 == direction.getCounterClockWise()) {
                    switch (direction) {
                        case NORTH: return INNER_MIDDLE_WEST;
                        case SOUTH: return INNER_MIDDLE_EAST;
                        case EAST: return INNER_MIDDLE;
                        default: return INNER_MIDDLE_SOUTH;
                    }
                } else {
                    switch (direction) {
                        case NORTH: return INNER_MIDDLE;
                        case SOUTH: return INNER_MIDDLE_SOUTH;
                        case EAST: return INNER_MIDDLE_EAST;
                        default: return INNER_MIDDLE_WEST;
                    }
                }
            } else {
                switch (direction) {
                    case NORTH: return MIDDLE;
                    case SOUTH: return MIDDLE_SOUTH;
                    case EAST: return MIDDLE_EAST;
                    default: return MIDDLE_WEST;
                }
            }
        } else {
            switch (direction) {
                case NORTH: return MIDDLE;
                case SOUTH: return MIDDLE_SOUTH;
                case EAST: return MIDDLE_EAST;
                default: return MIDDLE_WEST;
            }
        }
    }
}
