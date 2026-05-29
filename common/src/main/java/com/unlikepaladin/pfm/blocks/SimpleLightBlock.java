package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SimpleLightBlock extends PowerableBlock {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    private static final List<SimpleLightBlock> SIMPLE_LIGHTS = new ArrayList<>();
    public static final MapCodec<SimpleLightBlock> CODEC = simpleCodec(SimpleLightBlock::new);

    public SimpleLightBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(LIT,  false).setValue(POWERLOCKED, false));
        SIMPLE_LIGHTS.add(this);
    }

    @Override
    protected MapCodec<? extends PowerableBlock> codec() {
        return CODEC;
    }

    @Override
    public void setPowered(Level world, BlockPos lightPos, boolean powered) {
        BlockState state = world.getBlockState(lightPos);
        world.setBlockAndUpdate(lightPos, state.setValue(LIT, powered).setValue(POWERLOCKED, powered));
    }

    public static Stream<SimpleLightBlock> streamSimpleLights() {
        return SIMPLE_LIGHTS.stream();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean powered = ctx.getLevel().hasNeighborSignal(ctx.getClickedPos());
        return this.defaultBlockState().setValue(LIT, powered);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT) && !world.hasNeighborSignal(pos) && !state.getValue(POWERLOCKED)) {
            world.setBlock(pos, state.cycle(LIT), UPDATE_ALL);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        builder.add(POWERLOCKED);
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = Direction.UP;
        return canSupportCenter(world, pos.relative(direction), direction.getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (!state.canSurvive(levelReader, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        if (world.isClientSide()) {
            return;
        }
        boolean bl = (state.getValue(LIT));
        if (bl != world.hasNeighborSignal(pos)) {
            if (bl) {
                world.scheduleTick(pos, this, 4);
            } else {
                world.setBlock(pos, state.cycle(LIT), UPDATE_CLIENTS);
            }
        }
        super.neighborChanged(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    private static final VoxelShape SIMPLE_LIGHT = Shapes.or(box(4.5, 13.5, 4.5,11.5, 14.5, 11.5),box(3, 14.5, 3,13, 16, 13));
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SIMPLE_LIGHT;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
