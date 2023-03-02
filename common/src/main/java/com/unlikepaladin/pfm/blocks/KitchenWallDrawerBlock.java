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

public class KitchenWallDrawerBlock extends KitchenDrawerBlock {

    private static final List<FurnitureBlock> WOOD_DRAWERS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_DRAWERS = new ArrayList<>();
    public KitchenWallDrawerBlock(Settings settings) {
        super(settings);
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(KitchenWallDrawerBlock.class)){
            WOOD_DRAWERS.add(new FurnitureBlock(this, "kitchen_wall_drawer"));
        }
        else if (this.getClass().isAssignableFrom(KitchenWallDrawerBlock.class)){
            STONE_DRAWERS.add(new FurnitureBlock(this, "kitchen_wall_drawer"));
        }
    }

    public static Stream<FurnitureBlock> streamWallWoodDrawers() {
        return WOOD_DRAWERS.stream();
    }
    public static Stream<FurnitureBlock> streamWallStoneDrawers() {
        return STONE_DRAWERS.stream();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        CounterShape shape = state.get(SHAPE);
        boolean open = state.get(OPEN);
        switch (shape) {
            case INNER_LEFT: switch (dir) {
                case NORTH -> {
                    return MIDDLE_INNER_CORNER_WEST;
                }
                case SOUTH -> {
                    return MIDDLE_INNER_CORNER_EAST;
                }
                case WEST -> {
                    return MIDDLE_INNER_CORNER_SOUTH;
                }
                default -> {
                    return MIDDLE_INNER_CORNER;
                }
            }
            case INNER_RIGHT: switch (dir) {
                case NORTH -> {
                    return MIDDLE_INNER_CORNER;
                }
                case SOUTH -> {
                    return MIDDLE_INNER_CORNER_SOUTH;
                }
                case WEST -> {
                    return MIDDLE_INNER_CORNER_WEST;
                }
                default -> {
                    return MIDDLE_INNER_CORNER_EAST;
                }
            }
            case OUTER_RIGHT: switch (dir) {
                case NORTH -> {
                    if (open)
                    {
                        return MIDDLE_OUTER_CORNER_OPEN_EAST;
                    }
                    return MIDDLE_OUTER_CORNER_EAST;
                }
                case SOUTH -> {
                    if (open)
                    {
                        return MIDDLE_OUTER_CORNER_OPEN_WEST;
                    }
                    return MIDDLE_OUTER_CORNER_WEST;
                }
                case WEST -> {
                    if (open)
                    {
                        return MIDDLE_OUTER_CORNER_OPEN;
                    }
                    return MIDDLE_OUTER_CORNER;
                }
                default -> {
                    if (open)
                    {
                        return MIDDLE_OUTER_CORNER_OPEN_SOUTH;
                    }
                    return MIDDLE_OUTER_CORNER_SOUTH;
                }
            }
            case OUTER_LEFT: switch (dir) {
                case NORTH -> {
                    if (open)
                    {
                        return MIDDLE_OUTER_CORNER_OPEN;
                    }
                    return MIDDLE_OUTER_CORNER;
                }
                case SOUTH -> {
                    if (open)
                    {
                        return MIDDLE_OUTER_CORNER_OPEN_SOUTH;
                    }
                    return MIDDLE_OUTER_CORNER_SOUTH;
                }
                case WEST -> {
                    if (open)
                    {
                        return MIDDLE_OUTER_CORNER_OPEN_WEST;
                    }
                    return MIDDLE_OUTER_CORNER_WEST;
                }
                default -> {
                    if (open)
                    {
                        return MIDDLE_OUTER_CORNER_OPEN_EAST;
                    }
                    return MIDDLE_OUTER_CORNER_EAST;
                }
            }
            default: switch (dir) {
                case NORTH -> {
                    if(open){
                        return MIDDLE_OPEN;
                    }
                    return MIDDLE;
                }
                case SOUTH -> {
                    if (open) {
                        return MIDDLE_OPEN_SOUTH;
                    }
                    return MIDDLE_SOUTH;
                }
                case WEST -> {
                    if (open){
                        return MIDDLE_OPEN_WEST;
                    }
                    return MIDDLE_WEST;
                }
                default -> {
                    if (open) {
                        return MIDDLE_OPEN_EAST;
                    }
                    return MIDDLE_EAST;
                }
            }
        }
    }
}
