package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(KitchenCounterBlock.FACING);
        BlockState neighborStateFacing = world.getBlockState(pos.offset(direction));
        BlockState neighborStateOpposite = world.getBlockState(pos.offset(direction.getOpposite()));
        boolean open = state.get(OPEN);
        if (canConnectToCounter(neighborStateFacing) && neighborStateFacing.getProperties().contains(Properties.HORIZONTAL_FACING)) {
            Direction direction2 = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
            if (direction2.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                if (direction2 == direction.rotateYCounterclockwise()) {
                    switch (direction) {
                        case NORTH: {
                            if (open) {
                                return MIDDLE_OUTER_CORNER_OPEN;
                            }
                            return MIDDLE_OUTER_CORNER;
                        }
                        case SOUTH: {
                            if (open) {
                                return MIDDLE_OUTER_CORNER_OPEN_SOUTH;
                            }
                            return MIDDLE_OUTER_CORNER_SOUTH;
                        }
                        case EAST: {
                            if (open) {
                                return MIDDLE_OUTER_CORNER_OPEN_EAST;
                            }
                            return MIDDLE_OUTER_CORNER_EAST;
                        }
                        default: {
                            if (open) {
                                return MIDDLE_OUTER_CORNER_OPEN_WEST;
                            }
                            return MIDDLE_OUTER_CORNER_WEST;
                        }
                    }
                }
                else {
                    switch (direction) {
                        case NORTH: {
                            if (open) {
                                return MIDDLE_OUTER_CORNER_OPEN_EAST;
                            }
                            return MIDDLE_OUTER_CORNER_EAST;
                        }
                        case SOUTH: {
                            if (open) {
                                return MIDDLE_OUTER_CORNER_OPEN_WEST;
                            }
                            return MIDDLE_OUTER_CORNER_WEST;
                        }
                        case EAST: {
                            if (open) {
                                return MIDDLE_OUTER_CORNER_OPEN_SOUTH;
                            }
                            return MIDDLE_OUTER_CORNER_SOUTH;
                        }
                        default: {
                            if (open) {
                                return MIDDLE_OUTER_CORNER_OPEN;
                            }
                            return MIDDLE_OUTER_CORNER;
                        }
                    }
                }
            } else {
                switch (direction) {
                    case NORTH: {
                        if (open) {
                            return MIDDLE_OPEN;
                        }
                        return MIDDLE;
                    }
                    case SOUTH: {
                        if (open) {
                            return MIDDLE_OPEN_SOUTH;
                        }
                        return MIDDLE_SOUTH;
                    }
                    case EAST: {
                        if (open) {
                            return MIDDLE_OPEN_EAST;
                        }
                        return MIDDLE_EAST;
                    }
                    default: {
                        if (open) {
                            return MIDDLE_OPEN_WEST;
                        }
                        return MIDDLE_WEST;
                    }
                }
            }
        }
        else if (canConnectToCounter(neighborStateOpposite) && neighborStateOpposite.getProperties().contains(Properties.HORIZONTAL_FACING)) {
            Direction direction3;
            if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING).getOpposite();
            }
            else {
                direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
            }
            if (direction3.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(state, world, pos, direction3)) {
                if (direction3 == direction.rotateYCounterclockwise()) {
                    switch (direction) {
                        case NORTH: return MIDDLE_INNER_CORNER_WEST;
                        case SOUTH: return MIDDLE_INNER_CORNER_EAST;
                        case EAST: return MIDDLE_INNER_CORNER;
                        default: return MIDDLE_INNER_CORNER_SOUTH;
                    }
                } else {
                    switch (direction) {
                        case NORTH: return MIDDLE_INNER_CORNER;
                        case SOUTH: return MIDDLE_INNER_CORNER_SOUTH;
                        case EAST: return MIDDLE_INNER_CORNER_EAST;
                        default: return MIDDLE_INNER_CORNER_WEST;
                    }
                }
            } else {
                switch (direction) {
                    case NORTH: {
                        if (open) {
                            return MIDDLE_OPEN;
                        }
                        return MIDDLE;
                    }
                    case SOUTH: {
                        if (open) {
                            return MIDDLE_OPEN_SOUTH;
                        }
                        return MIDDLE_SOUTH;
                    }
                    case EAST: {
                        if (open) {
                            return MIDDLE_OPEN_EAST;
                        }
                        return MIDDLE_EAST;
                    }
                    default: {
                        if (open) {
                            return MIDDLE_OPEN_WEST;
                        }
                        return MIDDLE_WEST;
                    }
                }
            }
        }
        else {
            switch (direction) {
                case NORTH: {
                    if (open) {
                        return MIDDLE_OPEN;
                    }
                    return MIDDLE;
                }
                case SOUTH: {
                    if (open) {
                        return MIDDLE_OPEN_SOUTH;
                    }
                    return MIDDLE_SOUTH;
                }
                case EAST: {
                    if (open) {
                        return MIDDLE_OPEN_EAST;
                    }
                    return MIDDLE_EAST;
                }
                default: {
                    if (open) {
                        return MIDDLE_OPEN_WEST;
                    }
                    return MIDDLE_WEST;
                }
            }
        }
    }
}
