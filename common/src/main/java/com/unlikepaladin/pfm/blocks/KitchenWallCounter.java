package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class KitchenWallCounter extends KitchenCounter{

    private static final List<FurnitureBlock> WOOD_COUNTERS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_COUNTERS = new ArrayList<>();
    public KitchenWallCounter(Settings settings) {
        super(settings);
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(KitchenWallCounter.class)){
            WOOD_COUNTERS.add(new FurnitureBlock(this, "kitchen_wall_counter"));
        }
        else if (this.getClass().isAssignableFrom(KitchenWallCounter.class)){
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
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        CounterShape shape = state.get(SHAPE);
            switch (shape) {
                case INNER_LEFT: switch (dir) {
                    case NORTH: return INNER_MIDDLE_WEST;
                    case SOUTH: return INNER_MIDDLE_EAST;
                    case WEST: return INNER_MIDDLE_SOUTH;
                    default: return INNER_MIDDLE;
                }
                case INNER_RIGHT: switch (dir) {
                    case NORTH: return INNER_MIDDLE;
                    case SOUTH: return INNER_MIDDLE_SOUTH;
                    case WEST: return INNER_MIDDLE_WEST;
                    default: return INNER_MIDDLE_EAST;
                }
                case OUTER_RIGHT: switch (dir) {
                    case NORTH: return OUTER_MIDDLE_EAST;
                    case SOUTH: return OUTER_MIDDLE_WEST;
                    case WEST: return OUTER_MIDDLE;
                    default: return OUTER_MIDDLE_SOUTH;
                }
                case OUTER_LEFT: switch (dir) {
                    case NORTH: return OUTER_MIDDLE;
                    case SOUTH: return OUTER_MIDDLE_SOUTH;
                    case WEST: return OUTER_MIDDLE_WEST;
                    default: return OUTER_MIDDLE_EAST;
                }
                default: switch (dir) {
                    case NORTH: return MIDDLE;
                    case SOUTH: return MIDDLE_SOUTH;
                    case WEST: return MIDDLE_WEST;
                    default: return MIDDLE_EAST;
                }
            }
    }
}
