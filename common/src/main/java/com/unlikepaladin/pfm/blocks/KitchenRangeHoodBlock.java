package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class KitchenRangeHoodBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty DRAWER = BooleanProperty.create("drawer");
    private static final List<FurnitureBlock> OVEN_RANGE_HOOD = new ArrayList<>();
    public static final MapCodec<KitchenRangeHoodBlock> CODEC = simpleCodec(KitchenRangeHoodBlock::new);

    public KitchenRangeHoodBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(DOWN, false).setValue(DRAWER, false));
        OVEN_RANGE_HOOD.add(new FurnitureBlock(this, "oven_range_hood"));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamOvenRangeHoods() {
        return OVEN_RANGE_HOOD.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(DOWN);
        builder.add(DRAWER);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean down = ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getBlock() instanceof KitchenRangeHoodBlock;
        boolean drawer = ctx.getLevel().getBlockState(ctx.getClickedPos().above()).getBlock() instanceof KitchenWallDrawerSmallBlock;
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(DOWN, down).setValue(DRAWER, drawer);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction.getAxis().isVertical()) {
            boolean down = levelReader.getBlockState(pos.below()).getBlock() instanceof KitchenRangeHoodBlock;
            boolean drawer = levelReader.getBlockState(pos.above()).getBlock() instanceof KitchenWallDrawerSmallBlock;
            return state.setValue(DOWN, down).setValue(DRAWER, drawer);
        }
        return super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    protected static final VoxelShape RANGE_HOOD = Shapes.or(box(2, 4, 0,14, 16, 7),box(0, 0, 0,16, 4, 15));
    protected static final VoxelShape RANGE_HOOD_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, RANGE_HOOD);
    protected static final VoxelShape RANGE_HOOD_EAST = rotateShape(Direction.NORTH, Direction.EAST, RANGE_HOOD);
    protected static final VoxelShape RANGE_HOOD_WEST = rotateShape(Direction.NORTH, Direction.WEST, RANGE_HOOD);

    protected static final VoxelShape RANGE_HOOD_BOTTOM_DRAWER = Shapes.or(box(2, 0, 0,14, 28, 7));
    protected static final VoxelShape RANGE_HOOD_BOTTOM_DRAWER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, RANGE_HOOD_BOTTOM_DRAWER);
    protected static final VoxelShape RANGE_HOOD_BOTTOM_DRAWER_EAST = rotateShape(Direction.NORTH, Direction.EAST, RANGE_HOOD_BOTTOM_DRAWER);
    protected static final VoxelShape RANGE_HOOD_BOTTOM_DRAWER_WEST = rotateShape(Direction.NORTH, Direction.WEST, RANGE_HOOD_BOTTOM_DRAWER);

    protected static final VoxelShape RANGE_HOOD_BOTTOM = Shapes.or(box(2, 0, 0,14, 16, 7));
    protected static final VoxelShape RANGE_HOOD_BOTTOM_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, RANGE_HOOD_BOTTOM);
    protected static final VoxelShape RANGE_HOOD_BOTTOM_EAST = rotateShape(Direction.NORTH, Direction.EAST, RANGE_HOOD_BOTTOM);
    protected static final VoxelShape RANGE_HOOD_BOTTOM_WEST = rotateShape(Direction.NORTH, Direction.WEST, RANGE_HOOD_BOTTOM);

    protected static final VoxelShape RANGE_HOOD_DRAWER = Shapes.or(box(2, 16, 0,14, 28, 7),box(0, 12, 0,16, 16, 15));
    protected static final VoxelShape RANGE_HOOD_DRAWER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, RANGE_HOOD_DRAWER);
    protected static final VoxelShape RANGE_HOOD_DRAWER_EAST = rotateShape(Direction.NORTH, Direction.EAST, RANGE_HOOD_DRAWER);
    protected static final VoxelShape RANGE_HOOD_DRAWER_WEST = rotateShape(Direction.NORTH, Direction.WEST, RANGE_HOOD_DRAWER);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        boolean down = state.getValue(DOWN);
        boolean drawer = state.getValue(DRAWER);
        Direction direction = state.getValue(FACING);
        if (down && drawer) {
            return switch (direction) {
                case NORTH -> RANGE_HOOD_BOTTOM_DRAWER;
                case SOUTH -> RANGE_HOOD_BOTTOM_DRAWER_SOUTH;
                case EAST -> RANGE_HOOD_BOTTOM_DRAWER_EAST;
                default -> RANGE_HOOD_BOTTOM_DRAWER_WEST;
            };
        } else if (down) {
            return switch (direction) {
                case NORTH -> RANGE_HOOD_BOTTOM;
                case SOUTH -> RANGE_HOOD_BOTTOM_SOUTH;
                case EAST -> RANGE_HOOD_BOTTOM_EAST;
                default -> RANGE_HOOD_BOTTOM_WEST;
            };
        }
        else if (drawer) {
            return switch (direction) {
                case NORTH -> RANGE_HOOD_DRAWER;
                case SOUTH -> RANGE_HOOD_DRAWER_SOUTH;
                case EAST -> RANGE_HOOD_DRAWER_EAST;
                default -> RANGE_HOOD_DRAWER_WEST;
            };
        }
        return switch (direction) {
            case NORTH -> RANGE_HOOD;
            case SOUTH -> RANGE_HOOD_SOUTH;
            case EAST -> RANGE_HOOD_EAST;
            default -> RANGE_HOOD_WEST;
        };
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
