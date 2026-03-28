package com.unlikepaladin.pfm.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PendantBlock extends PowerableBlock implements DynamicRenderLayerInterface {
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    private final BlockState baseBlockState;
    private final Block baseBlock;
    private static final List<PendantBlock> PENDANTS = new ArrayList<>();
    public PendantBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(UP, false).setValue(DOWN, false).setValue(LIT,  false).setValue(POWERLOCKED, false));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        PENDANTS.add(this);
    }

    public static Stream<PendantBlock> streamPendantLights() {
        return PENDANTS.stream();
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public void setPowered(Level world, BlockPos lightPos, boolean powered) {
        BlockState state = world.getBlockState(lightPos);
        world.setBlockAndUpdate(lightPos, state.setValue(LIT, powered).setValue(POWERLOCKED,powered));
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    public static BlockState canConnect(BlockState state, LevelAccessor world, BlockPos pos) {
        boolean up = world.getBlockState(pos.above()).getBlock() instanceof PendantBlock;
        boolean down = world.getBlockState(pos.below()).getBlock() instanceof PendantBlock;
        if (up) {
            return state.setValue(UP, true).setValue(DOWN, down).setValue(LIT, (world.getBlockState(pos.above()).getValue(LIT)));
        }
        else {
            return state.setValue(UP, false).setValue(DOWN, down);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return direction.getAxis().isVertical() ? canConnect(state, world, pos) : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(state.getBlock())) {
            this.baseBlockState.neighborChanged(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onPlace(this.baseBlockState, world, pos, oldState, false);
        }
    }

    private static final VoxelShape single = Shapes.or(box(4, 0, 4,12, 7, 12),box(5, 5, 5,11, 9, 11),box(7.5, 9, 7.5,8.5, 15.5, 8.5),box(6.5, 15.5, 6.5,9.5, 16, 9.5));
    private static final VoxelShape up = Shapes.or(box(4, 0, 4,12, 7, 12),box(5, 5, 5,11, 9, 11),box(7.5, 9, 7.5,8.5, 16, 8.5));
    private static final VoxelShape middle = box(7.5, 0, 7.5,8.5, 16, 8.5);
    private static final VoxelShape down = Shapes.or(box(7.5, 0, 7.5,8.5, 15.5, 8.5),box(6.5, 15.5, 6.5,9.5, 16, 9.5));
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(UP) && state.getValue(DOWN)) {
            return middle;
        }
        else if (state.getValue(UP)){
            return up;
        }
        else if (state.getValue(DOWN)){
            return down;
        }
        return single;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean powered = ctx.getLevel().hasNeighborSignal(ctx.getClickedPos());
        BlockState state = this.defaultBlockState().setValue(LIT, powered);
        return canConnect(state, ctx.getLevel(), ctx.getClickedPos());
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isClientSide) {
            return;
        }
        if(world.getBlockState(pos.above()).getBlock() instanceof PendantBlock) {
            return;
        }

        boolean bl = (state.getValue(LIT));
        if (bl != world.hasNeighborSignal(pos)) {
            if (bl) {
                world.scheduleTick(pos, this, 4);
            } else {
                world.setBlock(pos, state.cycle(LIT), Block.UPDATE_CLIENTS);
            }
        }
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = Direction.UP;
        return Block.canSupportCenter(world, pos.relative(direction), direction.getOpposite()) || world.getBlockState(pos.relative(direction)).getBlock() instanceof PendantBlock;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (state.getValue(LIT) && !world.hasNeighborSignal(pos) && !state.getValue(POWERLOCKED)) {
            world.setBlock(pos, state.cycle(LIT), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP);
        builder.add(DOWN);
        builder.add(LIT);
        builder.add(POWERLOCKED);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public RenderType getCustomRenderLayer() {
        return RenderType.translucent();
    }
}
