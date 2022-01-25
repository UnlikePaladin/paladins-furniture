package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.entity.EntityChair;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.StairShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;


public class KitchenCounter extends HorizontalFacingBlock {
    private float height = 0.36f;
    private final Block baseBlock;
    public static final EnumProperty<CounterShape> SHAPE = EnumProperty.of("shape", CounterShape.class);

    private final BlockState baseBlockState;
    public KitchenCounter(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(SHAPE);
    }
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        return blockState.with(SHAPE, getShape(blockState, world, blockPos));
    }
    private static CounterShape getShape(BlockState state, BlockView world, BlockPos pos) {
        Direction direction3 = null;
        Object direction2;
        Direction direction = state.get(FACING);
        BlockState blockState = world.getBlockState(pos.offset(direction));
        boolean right = isCounter(world, pos, state.get(FACING).rotateYCounterclockwise(), state.get(FACING));
        boolean left = isCounter(world, pos, state.get(FACING).rotateYClockwise(), state.get(FACING));

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


    private static boolean isCounter(BlockView world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if(state.getBlock() instanceof KitchenCounter)
        {
            Direction sourceDirection = state.get(FACING);
            return state.getBlock() instanceof KitchenCounter;
        }
        return false;
    }

    private static boolean isDifferentOrientation(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.offset(dir));
        return !KitchenCounter.isCounter(blockState); //|| blockState.get(FACING) != state.get(FACING);
    }
    public static boolean isCounter(BlockState state) {
        return state.getBlock() instanceof KitchenCounter;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        return direction.getAxis().isHorizontal() ? state.with(SHAPE, getShape(state, world, pos)) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }




  protected static final VoxelShape FACE_SOUTH = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 16, 16));
    @SuppressWarnings("deprecated")


    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.baseBlockState.neighborUpdate(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }

    @Override

        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case WEST:
                return FACE_SOUTH;
            case NORTH:
                return FACE_SOUTH;
            case SOUTH:
                return FACE_SOUTH;
            case EAST:
            default:
                return FACE_SOUTH;
        }
    }


}

enum CounterShape implements StringIdentifiable
{
    STRAIGHT("straight"),
    INNER_LEFT("inner_left"),
    INNER_RIGHT("inner_right"),
    OUTER_LEFT("outer_left"),
    OUTER_RIGHT("outer_right"),
    LEFT_EDGE("left_edge"),
    RIGHT_EDGE("right_edge");

    private final String name;

    CounterShape(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
