package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import static com.unlikepaladin.pfm.blocks.LogTable.rotateShape;

public class SimpleBed extends BedBlock {
    public static EnumProperty<MiddleShape> SHAPE = EnumProperty.of("shape", MiddleShape.class);
    public SimpleBed(DyeColor color, Settings settings) {
        super(color, settings);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        return getShape(blockState, ctx.getWorld(), ctx.getBlockPos(), blockState.get(FACING));
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == getDirectionTowardsOtherPart(state.get(PART), state.get(FACING))) {
            if (neighborState.isOf(this) && neighborState.get(PART) != state.get(PART)) {
                return state.with(OCCUPIED, neighborState.get(OCCUPIED));
            }
            return Blocks.AIR.getDefaultState();
        }
        return direction.getAxis().isHorizontal() ? getShape(state, world, pos, state.get(FACING)) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE);
        super.appendProperties(builder);
    }

    private boolean isBed(WorldAccess world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if(state.getBlock() == this)
        {
            Direction sourceDirection = state.get(FACING);
            return sourceDirection.equals(tableDirection);
        }
        return false;
    }

    public BlockState getShape(BlockState state, WorldAccess world, BlockPos pos, Direction dir)
    {
        boolean left = isBed(world, pos, dir.rotateYCounterclockwise(), dir);
        boolean right = isBed(world, pos, dir.rotateYClockwise(), dir);
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
