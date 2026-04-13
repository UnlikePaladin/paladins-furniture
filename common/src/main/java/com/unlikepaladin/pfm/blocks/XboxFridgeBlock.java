package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class XboxFridgeBlock extends FridgeBlock
{
    public XboxFridgeBlock(Properties settings, Supplier<FreezerBlock> freezer) {
        super(settings, freezer);
    }
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
        stateManager.add(HALF);
    }
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (world instanceof ServerWorld serverWorld && blockEntity instanceof FridgeBlockEntity) {
            player.openMenu((FridgeBlockEntity)blockEntity);
            player.awardStat(Statistics.FRIDGE_OPENED);
            PiglinAi.angerNearbyPiglins(serverWorld, player, true);
        }
        return InteractionResult.CONSUME;
    }
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlock(pos.above(), this.defaultBlockState().setValue(FACING, placer.getDirection()).setValue(HALF, DoubleBlockHalf.UPPER).setValue(OPEN,false), UPDATE_ALL);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && player.isCreative()) {
            this.onBreakInCreative(world, pos, state, player);
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            if (neighborState.is(this) && neighborState.getValue(HALF) != doubleBlockHalf) {
                return state.setValue(FACING, neighborState.getValue(FACING)).setValue(OPEN, neighborState.getValue(OPEN));
            }
        }
        return state;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.below();
        BlockState blockState = world.getBlockState(blockPos);
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return blockState.isFaceSturdy(world, blockPos, Direction.UP);
        }
        return blockState.is(this);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        Level world = ctx.getLevel();
        if (blockPos.getY() < world.getMaxBuildHeight() - 1 && world.getBlockState(blockPos.above()).canBeReplaced(ctx)) {
            return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(OPEN, false).setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockPos blockPos;
        BlockState blockState = world.getBlockState(blockPos = pos.below());
        if (blockState.is(state.getBlock())) {
            BlockState blockState2 = blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
            world.setBlock(blockPos, blockState2, UPDATE_ALL | UPDATE_SUPPRESS_DROPS);
        }
        blockState = world.getBlockState(blockPos = pos.above());
        if (blockState.is(state.getBlock())) {
            BlockState blockState2 = blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
            world.setBlock(blockPos, blockState2, UPDATE_ALL | UPDATE_SUPPRESS_DROPS);
        }
        super.destroy(world, pos, state);
    }

    protected static final VoxelShape XBOX_FRIDGE = Shapes.or(box(0.5, 1, 3,15.5, 32, 16),box(1, 0, 2.84,15, 1, 15.84),box(0.51, 1, 1.91,15.31, 16, 2.91));
    protected static final VoxelShape XBOX_FRIDGE_UPPER = Shapes.or(box(0.5, -15, 3,15.5, 16, 16),box(1, -16, 2.84,15, -15, 15.84),box(0.51, 0, 1.91,15.31, 16, 2.91));
    protected static final VoxelShape XBOX_FRIDGE_OPEN = Shapes.or(box(0.5, 1, 3,15.5, 32, 16),box(1, 0, 2.84,15, 1, 15.84),box(0.5, 16, -11.69,1.5, 32, 3.11));
    protected static final VoxelShape XBOX_FRIDGE_UPPER_OPEN = Shapes.or(box(0.5, -15, 3,15.5, 16, 16),box(1, -16, 2.84,15, -15, 15.84),box(0.5, 0, -11.69,1.5, 16, 3.11));

    protected static final VoxelShape XBOX_FRIDGE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, XBOX_FRIDGE);
    protected static final VoxelShape XBOX_FRIDGE_UPPER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, XBOX_FRIDGE_UPPER);
    protected static final VoxelShape XBOX_FRIDGE_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, XBOX_FRIDGE_OPEN);
    protected static final VoxelShape XBOX_FRIDGE_UPPER_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, XBOX_FRIDGE_UPPER_OPEN);
    protected static final VoxelShape XBOX_FRIDGE_EAST = rotateShape(Direction.NORTH, Direction.EAST, XBOX_FRIDGE);
    protected static final VoxelShape XBOX_FRIDGE_UPPER_EAST = rotateShape(Direction.NORTH, Direction.EAST, XBOX_FRIDGE_UPPER);
    protected static final VoxelShape XBOX_FRIDGE_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, XBOX_FRIDGE_OPEN);
    protected static final VoxelShape XBOX_FRIDGE_UPPER_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, XBOX_FRIDGE_UPPER_OPEN);
    protected static final VoxelShape XBOX_FRIDGE_WEST = rotateShape(Direction.NORTH, Direction.WEST, XBOX_FRIDGE);
    protected static final VoxelShape XBOX_FRIDGE_UPPER_WEST = rotateShape(Direction.NORTH, Direction.WEST, XBOX_FRIDGE_UPPER);
    protected static final VoxelShape XBOX_FRIDGE_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, XBOX_FRIDGE_OPEN);
    protected static final VoxelShape XBOX_FRIDGE_UPPER_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, XBOX_FRIDGE_UPPER_OPEN);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        Boolean open = state.getValue(OPEN);
        Enum<DoubleBlockHalf> half = state.getValue(HALF);

        switch (dir) {
            case NORTH:
                if (half == DoubleBlockHalf.UPPER){
                    if (open) {
                    return XBOX_FRIDGE_UPPER_OPEN_SOUTH;}
                    else {
                        return XBOX_FRIDGE_UPPER_SOUTH;}
                    }
                else {
                    if (open)
                    return XBOX_FRIDGE_OPEN_SOUTH;
                    else
                        return XBOX_FRIDGE_SOUTH;
                }

            case SOUTH:
                if (half == DoubleBlockHalf.UPPER){
                    if (open) {
                        return XBOX_FRIDGE_UPPER_OPEN;}
                    else {
                        return XBOX_FRIDGE_UPPER;}
                }
                else {
                    if (open)
                        return XBOX_FRIDGE_OPEN;
                    else
                        return XBOX_FRIDGE;
                }

            case EAST:
                if (half == DoubleBlockHalf.UPPER){
                    if (open) {
                        return XBOX_FRIDGE_UPPER_OPEN_WEST;}
                    else {
                        return XBOX_FRIDGE_UPPER_WEST;}
                }
                else {
                    if (open)
                        return XBOX_FRIDGE_OPEN_WEST;
                    else
                        return XBOX_FRIDGE_WEST;
                }
            default:
                if (half == DoubleBlockHalf.UPPER){
                    if (open) {
                        return XBOX_FRIDGE_UPPER_OPEN_EAST;}
                    else {
                        return XBOX_FRIDGE_UPPER_EAST;}
                }
                else {
                    if (open)
                        return XBOX_FRIDGE_OPEN_EAST;
                    else
                        return XBOX_FRIDGE_EAST;
            }
        }
    }
}
