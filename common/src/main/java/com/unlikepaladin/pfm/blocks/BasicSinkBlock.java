package com.unlikepaladin.pfm.blocks;

import com.mojang.datafixers.util.Function3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.DinnerTableBlock.rotateShape;

public class BasicSinkBlock extends AbstractSinkBlock {
    private static final List<BasicSinkBlock> SINKS = new ArrayList<>();

    public BasicSinkBlock(Properties settings, Biome.Precipitation precipitationPredicate, CauldronBehavior.CauldronBehaviorMap behaviorMap) {
        super(settings, precipitationPredicate, behaviorMap);
        SINKS.add(this);
    }

    @Override
    public Function3<Settings, Biome.Precipitation, CauldronBehavior.CauldronBehaviorMap, AbstractSinkBlock> getSinkConstructor() {
        return BasicSinkBlock::new;
    }

    public static Stream<BasicSinkBlock> streamSinks() {
        return SINKS.stream();
    }

    public static final VoxelShape NORTH = Shapes.or(box(4, 1, 0.3,12, 11.3, 8.3), box(3, 0, 0.3,13, 1, 9.3),box(1.0625, 11.3, 0.3,14.9675, 16.3, 12.3));
    public static final VoxelShape SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, NORTH);
    public static final VoxelShape EAST = rotateShape(Direction.NORTH, Direction.EAST, NORTH);
    public static final VoxelShape WEST = rotateShape(Direction.NORTH, Direction.WEST, NORTH);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        switch (facing){
            case NORTH: return NORTH;
            case EAST: return EAST;
            case WEST: return WEST;
            default: return SOUTH;
        }
    }
}
