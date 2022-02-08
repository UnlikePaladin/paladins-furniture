package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class XboxFridge extends Fridge
{
    public XboxFridge(Settings settings, Supplier<Block> freezer) {
        super(settings, freezer);
    }
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
        stateManager.add(HALF);
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FridgeBlockEntity) {
            player.openHandledScreen((FridgeBlockEntity)blockEntity);
            player.incrementStat(StatisticsRegistry.FRIDGE_OPENED);
            PiglinBrain.onGuardedBlockInteracted(player, true);
        }
        return ActionResult.CONSUME;
    }
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), this.getDefaultState().with(FACING, placer.getHorizontalFacing()).with(HALF, DoubleBlockHalf.UPPER).with(OPEN,false), Block.NOTIFY_ALL);
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof FridgeBlockEntity) {
            ((FridgeBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            if (neighborState.isOf(this) && neighborState.get(HALF) != doubleBlockHalf) {
                return state.with(FACING, neighborState.get(FACING)).with(OPEN, neighborState.get(OPEN));
            }
            return Blocks.AIR.getDefaultState();
        }
        if (doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            return blockState.isSideSolidFullSquare(world, blockPos, Direction.UP);
        }
        return blockState.isOf(this);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            return this.getDefaultState().with(FACING, ctx.getPlayerFacing()).with(OPEN, false).with(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    protected static final VoxelShape XBOX_FRIDGE = VoxelShapes.union(createCuboidShape(0.5, 1, 3,15.5, 32, 16),createCuboidShape(1, 0, 2.84,15, 1, 15.84),createCuboidShape(0.51, 1, 1.91,15.31, 16, 2.91));
    protected static final VoxelShape XBOX_FRIDGE_UPPER = VoxelShapes.union(createCuboidShape(0.5, -15, 3,15.5, 16, 16),createCuboidShape(1, -16, 2.84,15, -15, 15.84),createCuboidShape(0.51, 0, 1.91,15.31, 16, 2.91));
    protected static final VoxelShape XBOX_FRIDGE_OPEN = VoxelShapes.union(createCuboidShape(0.5, 1, 3,15.5, 32, 16),createCuboidShape(1, 0, 2.84,15, 1, 15.84),createCuboidShape(0.5, 16, -11.69,1.5, 32, 3.11));
    protected static final VoxelShape XBOX_FRIDGE_UPPER_OPEN = VoxelShapes.union(createCuboidShape(0.5, -15, 3,15.5, 16, 16),createCuboidShape(1, -16, 2.84,15, -15, 15.84),createCuboidShape(0.5, 0, -11.69,1.5, 16, 3.11));

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        Boolean open = state.get(OPEN);
        Enum<DoubleBlockHalf> half = state.get(HALF);

        switch (dir) {
            case NORTH:
                if (half == DoubleBlockHalf.UPPER){
                    if (open) {
                    return rotateShape(Direction.NORTH, Direction.SOUTH, XBOX_FRIDGE_UPPER_OPEN);}
                    else {
                        return rotateShape(Direction.NORTH, Direction.SOUTH, XBOX_FRIDGE_UPPER);}
                    }
                else {
                    if (open)
                    return rotateShape(Direction.NORTH, Direction.SOUTH, XBOX_FRIDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, XBOX_FRIDGE);
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
                        return rotateShape(Direction.NORTH, Direction.WEST, XBOX_FRIDGE_UPPER_OPEN);}
                    else {
                        return rotateShape(Direction.NORTH, Direction.WEST, XBOX_FRIDGE_UPPER);}
                }
                else {
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.WEST, XBOX_FRIDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.WEST, XBOX_FRIDGE);
                }
            case WEST:
            default:
                if (half == DoubleBlockHalf.UPPER){
                    if (open) {
                        return rotateShape(Direction.NORTH, Direction.EAST, XBOX_FRIDGE_UPPER_OPEN);}
                    else {
                        return rotateShape(Direction.NORTH, Direction.EAST, XBOX_FRIDGE_UPPER);}
                }
                else {
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, XBOX_FRIDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, XBOX_FRIDGE);
                }
        }
    }
}
