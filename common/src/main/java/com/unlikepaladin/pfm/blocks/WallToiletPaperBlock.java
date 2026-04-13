package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.SimpleStoolBlock.rotateShape;

public class WallToiletPaperBlock extends HorizontalDirectionalBlock {
    protected static final BooleanProperty WALL = BooleanProperty.create("wall");
    private static final List<FurnitureBlock> TOILET_PAPER = new ArrayList<>();
    public static final MapCodec<WallToiletPaperBlock> CODEC = simpleCodec(WallToiletPaperBlock::new);
    public WallToiletPaperBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WALL, false));
        TOILET_PAPER.add(new FurnitureBlock(this, "toilet_paper"));

    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamToiletPaperBlocks() {
        return TOILET_PAPER.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WALL, FACING);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.getValue(FACING) && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            world.setBlockAndUpdate(pos, state.cycle(WALL));
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    public static final VoxelShape WALL_PAPER_SOUTH = Shapes.or(box(11.5, 12, 12,12.5, 13, 16), box(3.5, 12, 11.5,4.5, 13, 16), box(4.5, 10.5, 10.5,11.5, 14.5, 14.5));
    public static final VoxelShape WALL_PAPER_NORTH = rotateShape(Direction.SOUTH, Direction.NORTH, WALL_PAPER_SOUTH);
    public static final VoxelShape WALL_PAPER_EAST = rotateShape(Direction.SOUTH, Direction.EAST, WALL_PAPER_SOUTH);
    public static final VoxelShape WALL_PAPER_WEST = rotateShape(Direction.SOUTH, Direction.WEST, WALL_PAPER_SOUTH);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        switch (direction) {
            case EAST: {
                return WALL_PAPER_EAST;
            }
            case WEST: {
                return WALL_PAPER_WEST;
            }
            case SOUTH: {
                return WALL_PAPER_SOUTH;
            }
            default:
            case NORTH: {
                return WALL_PAPER_NORTH;
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
