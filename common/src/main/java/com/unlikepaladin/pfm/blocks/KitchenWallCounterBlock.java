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

public class KitchenWallCounterBlock extends KitchenCounterBlock{

    private static final List<FurnitureBlock> WOOD_COUNTERS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_COUNTERS = new ArrayList<>();
    public KitchenWallCounterBlock(Settings settings) {
        super(settings);
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(KitchenWallCounterBlock.class)){
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
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        CounterShape shape = state.get(SHAPE);
            return switch (shape) {
                case INNER_LEFT -> switch (dir) {
                    case NORTH -> INNER_MIDDLE_WEST;
                    case SOUTH -> INNER_MIDDLE_EAST;
                    case WEST -> INNER_MIDDLE_SOUTH;
                    default -> INNER_MIDDLE;
                };
                case INNER_RIGHT -> switch (dir) {
                    case NORTH -> INNER_MIDDLE;
                    case SOUTH -> INNER_MIDDLE_SOUTH;
                    case WEST -> INNER_MIDDLE_WEST;
                    default -> INNER_MIDDLE_EAST;
                };
                case OUTER_RIGHT -> switch (dir) {
                    case NORTH -> OUTER_MIDDLE_EAST;
                    case SOUTH -> OUTER_MIDDLE_WEST;
                    case WEST -> OUTER_MIDDLE;
                    default -> OUTER_MIDDLE_SOUTH;
                };
                case OUTER_LEFT -> switch (dir) {
                    case NORTH -> OUTER_MIDDLE;
                    case SOUTH -> OUTER_MIDDLE_SOUTH;
                    case WEST -> OUTER_MIDDLE_WEST;
                    default -> OUTER_MIDDLE_EAST;
                };
                default -> switch (dir) {
                    case NORTH -> MIDDLE;
                    case SOUTH -> MIDDLE_SOUTH;
                    case WEST -> MIDDLE_WEST;
                    default -> MIDDLE_EAST;
                };
            };
    }
}
