package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.LogTable.rotateShape;

public class SimpleBed extends BedBlock implements Waterloggable {
    public static EnumProperty<MiddleShape> SHAPE = EnumProperty.of("shape", MiddleShape.class);
    private static final List<FurnitureBlock> SIMPLE_BEDS = new ArrayList<>();
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public SimpleBed(DyeColor color, Settings settings) {
        super(color, settings);
        setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false).with(PART, BedPart.FOOT).with(OCCUPIED, false));
        if(this.getClass().isAssignableFrom(SimpleBed.class)){
            String bedColor = color.getName();
            SIMPLE_BEDS.add(new FurnitureBlock(this, bedColor+"_simple_bed"));
        }
    }

    public static Stream<FurnitureBlock> streamSimpleBeds() {
        return SIMPLE_BEDS.stream();
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing()).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
        Direction direction = ctx.getPlayerFacing();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(direction);
        if (ctx.getWorld().getBlockState(blockPos2).canReplace(ctx)) {
            return getShape(blockState, ctx.getWorld(), ctx.getBlockPos(), blockState.get(FACING));
        }
        return null;
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (direction == getDirectionTowardsOtherPart(state.get(PART), state.get(FACING))) {
            if (neighborState.isOf(this) && neighborState.get(PART) != state.get(PART)) {
                return state.with(OCCUPIED, neighborState.get(OCCUPIED));
            }
            return Blocks.AIR.getDefaultState();
        }
        return direction.getAxis().isHorizontal() ? getShape(state, world, pos, state.get(FACING)) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockPos;
        BlockState blockState;
        BedPart bedPart;
        if (!world.isClient && player.isCreative() && (bedPart = state.get(PART)) == BedPart.FOOT && (blockState = world.getBlockState(blockPos = pos.offset(getDirectionTowardsOtherPart(bedPart, state.get(FACING))))).isOf(this) && blockState.get(PART) == BedPart.HEAD) {
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
        }
        this.spawnBreakParticles(world, player, pos, state);
        if (state.isIn(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinBrain.onGuardedBlockInteracted(player, false);
        }
        world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(SHAPE);
        stateManager.add(WATERLOGGED);
        super.appendProperties(stateManager);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public boolean isBed(WorldAccess world, BlockPos pos, Direction direction, Direction bedDirection, BlockState originalState)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if(state.getBlock().getClass().isAssignableFrom(SimpleBed.class))
        {
            if (state.get(PART) == originalState.get(PART)) {
                Direction sourceDirection = state.get(FACING);
                return sourceDirection.equals(bedDirection);
            }
        }
        return false;
    }

    public BlockState getShape(BlockState state, WorldAccess world, BlockPos pos, Direction dir)
    {
        boolean left = isBed(world, pos, dir.rotateYCounterclockwise(), dir, state);
        boolean right = isBed(world, pos, dir.rotateYClockwise(), dir, state);
        if(left && right)
        {
            return state.with(SHAPE, MiddleShape.MIDDLE);
        }
        else if(left)
        {
            return state.with(SHAPE, MiddleShape.RIGHT);
        }
        else if(right)
        {
            return state.with(SHAPE, MiddleShape.LEFT);
        }
        return state.with(SHAPE, MiddleShape.SINGLE);
    }

    static final VoxelShape HEAD = VoxelShapes.union(createCuboidShape(0, 2, 0, 16, 14, 3),createCuboidShape(0, 2, 3, 16, 9, 16),createCuboidShape(1, 9, 3, 15, 10, 11));
    static final VoxelShape HEAD_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD);
    static final VoxelShape HEAD_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD);
    static final VoxelShape HEAD_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD);

    static final VoxelShape FOOT = VoxelShapes.union(createCuboidShape(0, 2, 0, 16, 9, 13),createCuboidShape(0, 2, 13, 16, 10, 16));
    static final VoxelShape FOOT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT);
    static final VoxelShape FOOT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT);
    static final VoxelShape FOOT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT);

    static final VoxelShape FOOT_FOOT_R = createCuboidShape(13, 0, 13, 16, 2, 16);
    static final VoxelShape FOOT_FOOT_L = createCuboidShape(0, 0, 13, 3, 2, 16);
    static final VoxelShape FOOT_HEAD_R = createCuboidShape(13, 0, 0, 16, 2, 3);
    static final VoxelShape FOOT_HEAD_L = createCuboidShape(0, 0, 0, 3, 2, 3);

    static final VoxelShape HEAD_SINGLE = VoxelShapes.union(HEAD, FOOT_HEAD_L, FOOT_HEAD_R);
    static final VoxelShape HEAD_SINGLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_SINGLE);
    static final VoxelShape HEAD_SINGLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_SINGLE);
    static final VoxelShape HEAD_SINGLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_SINGLE);

    static final VoxelShape FOOT_SINGLE = VoxelShapes.union(FOOT, FOOT_FOOT_L, FOOT_FOOT_R);
    static final VoxelShape FOOT_SINGLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_SINGLE);
    static final VoxelShape FOOT_SINGLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_SINGLE);
    static final VoxelShape FOOT_SINGLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_SINGLE);

    static final VoxelShape HEAD_LEFT = VoxelShapes.union(HEAD, FOOT_HEAD_L);
    static final VoxelShape HEAD_LEFT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_LEFT);
    static final VoxelShape HEAD_LEFT_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_LEFT);
    static final VoxelShape HEAD_LEFT_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_LEFT);

    static final VoxelShape FOOT_LEFT = VoxelShapes.union(FOOT, FOOT_FOOT_L);
    static final VoxelShape FOOT_LEFT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_LEFT);
    static final VoxelShape FOOT_LEFT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_LEFT);
    static final VoxelShape FOOT_LEFT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_LEFT);

    static final VoxelShape HEAD_RIGHT = VoxelShapes.union(HEAD, FOOT_HEAD_R);
    static final VoxelShape HEAD_RIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD_RIGHT);
    static final VoxelShape HEAD_RIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD_RIGHT);
    static final VoxelShape HEAD_RIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD_RIGHT);

    static final VoxelShape FOOT_RIGHT = VoxelShapes.union(FOOT, FOOT_FOOT_R);
    static final VoxelShape FOOT_RIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FOOT_RIGHT);
    static final VoxelShape FOOT_RIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, FOOT_RIGHT);
    static final VoxelShape FOOT_RIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, FOOT_RIGHT);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        BedPart bedPart = state.get(PART);
        MiddleShape middleShape = state.get(SHAPE);
        switch (middleShape){
            case MIDDLE -> {
                switch (dir){
                    case NORTH -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD;
                        }
                        return FOOT;
                    }
                    case EAST -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_EAST;
                        }
                        return FOOT_EAST;
                    }
                    case WEST -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_WEST;
                        }
                        return FOOT_WEST;
                    }
                    default -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SOUTH;
                        }
                        return FOOT_SOUTH;
                    }
                }
            }
            case SINGLE -> {
                switch (dir){
                    case NORTH -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SINGLE;
                        }
                        return FOOT_SINGLE;
                    }
                    case EAST -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SINGLE_EAST;
                        }
                        return FOOT_SINGLE_EAST;
                    }
                    case WEST -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SINGLE_WEST;
                        }
                        return FOOT_SINGLE_WEST;
                    }
                    default -> {
                        if(bedPart == BedPart.HEAD){
                            return HEAD_SINGLE_SOUTH;
                        }
                        return FOOT_SINGLE_SOUTH;
                    }
                }
            }
        case RIGHT -> {
            switch (dir) {
                case NORTH -> {
                    if (bedPart == BedPart.HEAD) {
                        return HEAD_RIGHT;
                    }
                    return FOOT_RIGHT;
                }
                case EAST -> {
                    if (bedPart == BedPart.HEAD) {
                        return HEAD_RIGHT_EAST;
                    }
                    return FOOT_RIGHT_EAST;
                }
                case WEST -> {
                    if (bedPart == BedPart.HEAD) {
                        return HEAD_RIGHT_WEST;
                    }
                    return FOOT_RIGHT_WEST;
                }
                default -> {
                    if (bedPart == BedPart.HEAD) {
                        return HEAD_RIGHT_SOUTH;
                    }
                    return FOOT_RIGHT_SOUTH;
                    }
                }
            }
            default -> {
                switch (dir) {
                    case NORTH -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_LEFT;
                        }
                        return FOOT_LEFT;
                    }
                    case EAST -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_LEFT_EAST;
                        }
                        return FOOT_LEFT_EAST;
                    }
                    case WEST -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_LEFT_WEST;
                        }
                        return FOOT_LEFT_WEST;
                    }
                    default -> {
                        if (bedPart == BedPart.HEAD) {
                            return HEAD_LEFT_SOUTH;
                        }
                        return FOOT_LEFT_SOUTH;
                    }
                }
            }
        }
    }
}
