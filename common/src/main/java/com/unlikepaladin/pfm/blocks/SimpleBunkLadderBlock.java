package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SimpleBunkLadderBlock extends LadderBlock {
    public static final BooleanProperty UP = BlockStateProperties.UP;
    private static final List<FurnitureBlock> SIMPLE_BUNK_LADDER = new ArrayList<>();
    public SimpleBunkLadderBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(((this.getStateDefinition().any()).setValue(FACING, Direction.NORTH)).setValue(WATERLOGGED, false).setValue(UP, true));
        SIMPLE_BUNK_LADDER.add(new FurnitureBlock(this, "simple_bunk_ladder"));
    }

    public static Stream<FurnitureBlock> streamSimpleBunkLadder() {
        return SIMPLE_BUNK_LADDER.stream();
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState;
        if (!ctx.replacingClickedOnBlock() && (blockState = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(ctx.getHorizontalDirection().getOpposite()))).is(this) && blockState.getValue(FACING) == ctx.getHorizontalDirection()) {
            return null;
        }
        blockState = this.defaultBlockState();
        Level worldView = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        boolean up = ctx.getLevel().getBlockState(blockPos.above()).getBlock() instanceof SimpleBunkLadderBlock;
        for (Direction direction : ctx.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal() || !(blockState = blockState.setValue(FACING, direction.getOpposite())).canSurvive(worldView, blockPos)) continue;
            return blockState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(UP, up);
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis().isVertical() && canSurvive(state, world, pos)) {
            boolean up = world.getBlockState(pos.above()).getBlock() instanceof SimpleBunkLadderBlock;
            return state.setValue(UP, up);
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    private boolean canPlaceOn(BlockGetter world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isFaceSturdy(world, pos, side) || blockState.getBlock() instanceof BedBlock;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        return this.canPlaceOn(world, pos.relative(direction.getOpposite()), direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, UP);
    }


}
