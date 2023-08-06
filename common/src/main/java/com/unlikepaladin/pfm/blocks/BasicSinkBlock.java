package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.DinnerTableBlock.rotateShape;

public class BasicSinkBlock extends AbstractSinkBlock {
    private static final List<BasicSinkBlock> SINKS = new ArrayList<>();

    public BasicSinkBlock(Settings settings, Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, precipitationPredicate, behaviorMap);
        SINKS.add(this);
    }

    public static Stream<BasicSinkBlock> streamSinks() {
        return SINKS.stream();
    }

    public static final VoxelShape NORTH = VoxelShapes.union(createCuboidShape(4, 1, 0.3,12, 11.3, 8.3), createCuboidShape(3, 0, 0.3,13, 1, 9.3),createCuboidShape(1.0625, 11.3, 0.3,14.9675, 16.3, 12.3));
    public static final VoxelShape SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, NORTH);
    public static final VoxelShape EAST = rotateShape(Direction.NORTH, Direction.EAST, NORTH);
    public static final VoxelShape WEST = rotateShape(Direction.NORTH, Direction.WEST, NORTH);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(Properties.HORIZONTAL_FACING);
        switch (facing){
            case NORTH: return NORTH;
            case EAST: return EAST;
            case WEST: return WEST;
            default: return SOUTH;
        }
    }
}
