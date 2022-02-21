package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.DrawerBlockEntity;
import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.KitchenCounter.SHAPE;
import static com.unlikepaladin.pfm.blocks.KitchenCounter.rotateShape;

public class KitchenCabinet extends HorizontalFacingBlock implements BlockEntityProvider {
    private final BlockState baseBlockState;
    private final Block baseBlock;

    public KitchenCabinet(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(OPEN, false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
    }
    public static final BooleanProperty OPEN = Properties.OPEN;

    protected static final VoxelShape STRAIGHT = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 16, 8), createCuboidShape(0, 1, 8,16, 16, 9), createCuboidShape(6, 3, 9,7, 7, 10), createCuboidShape(9, 3, 9,10, 7, 10));
    protected static final VoxelShape INNER_CORNER = VoxelShapes.union(createCuboidShape(0, 0, 0,8, 16, 8),createCuboidShape(1, 3, 9,2, 7, 10), createCuboidShape(0, 1, 8,8, 16, 9),createCuboidShape(7, 1, 9,8, 16, 16),createCuboidShape(8, 0, 0,16, 16, 16),createCuboidShape(6, 3, 13,7, 7, 14));
    protected static final VoxelShape OUTER_CORNER = VoxelShapes.union(createCuboidShape(0, 0, 0,8, 16, 8),createCuboidShape(6, 3, 9,7, 7, 10),createCuboidShape(0, 1, 8,8, 16, 9),createCuboidShape(8, 1, 0,9, 16, 8),createCuboidShape(9, 3, 6,10, 7, 7));
    protected static final VoxelShape LEFT_EDGE = VoxelShapes.union(createCuboidShape(8, 0, 0,16, 16, 8), createCuboidShape(8, 1, 8,16, 16, 9), createCuboidShape(14, 3, 9,15, 7, 10));
    protected static final VoxelShape RIGHT_EDGE = VoxelShapes.union(createCuboidShape(0, 0, 0,8, 16, 8), createCuboidShape(0, 1, 8,8, 16, 9), createCuboidShape(1, 3, 9,2, 7, 10));

    protected static final VoxelShape STRAIGHT_OPEN = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 16, 8), createCuboidShape(16, 3, 14,17, 7, 15), createCuboidShape(15, 1, 8,16, 16, 16), createCuboidShape(-1, 3, 14,0, 7, 15),createCuboidShape(0, 1, 8,1, 16, 16));
    protected static final VoxelShape INNER_CORNER_OPEN = VoxelShapes.union(createCuboidShape(7, 1, 9,8, 16, 16),createCuboidShape(8, 0, 8,16, 16, 16), createCuboidShape(6, 3, 13,7, 7, 14),createCuboidShape(0, 0, 0,16, 16, 8));
    protected static final VoxelShape OUTER_CORNER_OPEN = VoxelShapes.union(createCuboidShape(0, 0, 0,8, 16, 8),createCuboidShape(0, 1, 8,1, 16, 16),createCuboidShape(-1, 3, 14,0, 7, 15),createCuboidShape(8, 1, 0,9, 16, 8),createCuboidShape(9, 3, 6,10, 7, 7));
    protected static final VoxelShape LEFT_EDGE_OPEN = VoxelShapes.union(createCuboidShape(8, 0, 0,16, 16, 8), createCuboidShape(7, 3, 14,8, 7, 15), createCuboidShape(8, 1, 8,9, 16, 16));
    protected static final VoxelShape RIGHT_EDGE_OPEN = VoxelShapes.union(createCuboidShape(0, 0, 0,8, 16, 8), createCuboidShape(7, 1, 8,8, 16, 16), createCuboidShape(8, 3, 14,9, 7, 15));



    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(SHAPE);
        stateManager.add(OPEN);

    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        CounterShape shape = state.get(SHAPE);
        Boolean open = state.get(OPEN);
        switch(shape) {
            case STRAIGHT:
                if(dir.equals(Direction.NORTH))
                    if (open)
                    return STRAIGHT_OPEN;
                    else
                        return STRAIGHT;
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, STRAIGHT_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, STRAIGHT);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, STRAIGHT_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, STRAIGHT);
                else
                    if(open)
                    return rotateShape(Direction.NORTH, Direction.WEST, STRAIGHT_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.WEST, STRAIGHT);

            case INNER_LEFT:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER);
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return INNER_CORNER_OPEN;
                    else
                        return INNER_CORNER;
                else
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER);
            case INNER_RIGHT:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return INNER_CORNER_OPEN;
                    else
                        return INNER_CORNER;
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER);
                else
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER);
            case OUTER_LEFT:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return OUTER_CORNER_OPEN;
                    else
                        return OUTER_CORNER;
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER);
                else
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER);
            case OUTER_RIGHT:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER);
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER);
                else
                    if (open)
                        return OUTER_CORNER_OPEN;
                    else
                        return OUTER_CORNER;
            case LEFT_EDGE:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return LEFT_EDGE_OPEN;
                    else
                        return LEFT_EDGE;
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, LEFT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, LEFT_EDGE);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, LEFT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, LEFT_EDGE);
                else
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.WEST, LEFT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.WEST, LEFT_EDGE);
            case RIGHT_EDGE:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return RIGHT_EDGE_OPEN;
                    else
                        return RIGHT_EDGE;
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, RIGHT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, RIGHT_EDGE);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, RIGHT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, RIGHT_EDGE);
                else
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.WEST, RIGHT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.WEST, RIGHT_EDGE);
            default:
                return STRAIGHT;
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Inventory) {
            ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
    public boolean isCounter(BlockState state) {
        return state.getBlock() instanceof KitchenCabinet;
    }

    public boolean canConnect(BlockView world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        return (state.getBlock() instanceof KitchenCabinet);
    }
    private boolean isDifferentOrientation(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.offset(dir));
        return !this.isCounter(blockState); //|| blockState.get(FACING) != state.get(FACING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        return direction.getAxis().isHorizontal() ? state.with(SHAPE, getShape(state, world, pos)) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.baseBlockState.neighborUpdate(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        return blockState.with(SHAPE, this.getShape(blockState, world, blockPos));
    }

    private CounterShape getShape(BlockState state, BlockView world, BlockPos pos) {
        Direction direction3 = null;
        Object direction2;
        Direction direction = state.get(FACING);
        BlockState blockState = world.getBlockState(pos.offset(direction));
        boolean right = this.canConnect(world, pos, state.get(FACING).rotateYCounterclockwise(), state.get(FACING));
        boolean left = this.canConnect(world, pos, state.get(FACING).rotateYClockwise(), state.get(FACING));

        if (isCounter(blockState) && ((Direction)(direction2 = blockState.get(FACING))).getAxis() != state.get(FACING).getAxis() && isDifferentOrientation(state, world, pos, ((Direction)direction2).getOpposite())) {
            if (direction2 == direction.rotateYCounterclockwise()) {
                return CounterShape.OUTER_LEFT;
            }
            return CounterShape.OUTER_RIGHT;
        }
        direction2 = world.getBlockState(pos.offset(direction.getOpposite()));
        boolean innerCorner = isCounter((BlockState)direction2) && (direction3 = (Direction) ((State)direction2).get(FACING)).getAxis() != state.get(FACING).getAxis() && isDifferentOrientation(state, world, pos, direction3);
        if (innerCorner) {
            if (direction3 == direction.rotateYCounterclockwise()) {
                return CounterShape.INNER_LEFT;
            }
            return CounterShape.INNER_RIGHT;
        }
        if (left && right) {
            return CounterShape.STRAIGHT;
        }
        else if (left) {
            return CounterShape.LEFT_EDGE;
        }
        else if (right) {
            return CounterShape.RIGHT_EDGE;
        }
        return CounterShape.STRAIGHT;
    }



    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DrawerBlockEntity) {
            player.openHandledScreen((DrawerBlockEntity)blockEntity);
            player.incrementStat(StatisticsRegistry.CABINET_SEARCHED);
            PiglinBrain.onGuardedBlockInteracted(player, true);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof DrawerBlockEntity) {
            ((DrawerBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DrawerBlockEntity(pos,state);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }


}
