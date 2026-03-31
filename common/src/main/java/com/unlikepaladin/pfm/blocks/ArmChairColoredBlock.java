package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class ArmChairColoredBlock extends ArmChairBlock implements DyeableFurnitureBlock {
    public static final EnumProperty<ArmChairShape> SHAPE = EnumProperty.create("shape", ArmChairShape.class);
    private static final List<FurnitureBlock> COLORED_ARMCHAIRS = new ArrayList<>();
    private final DyeColor color;

    public ArmChairColoredBlock(DyeColor color, Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(SHAPE, ArmChairShape.STRAIGHT));
            if (this.getClass().isAssignableFrom(ArmChairColoredBlock.class)) {
                COLORED_ARMCHAIRS.add(new FurnitureBlock(this, "arm_chair"));
            }
        this.color = color;
    }

    public static Stream<FurnitureBlock> streamArmChairColored() {
        return COLORED_ARMCHAIRS.stream();
    }

    public DyeColor getPFMColor() {
        return this.color;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(SHAPE);
    }

    protected static final VoxelShape STANDARD = Shapes.or(box(12, 0, 12 ,14.5, 3, 14.5),box(12, 0, 1.5,14.5, 3, 4), box(1, 0, 1.5, 3.5, 3, 4), box(1, 0, 12, 3.5, 3, 14.5), box(6.6, 2, 13, 16.3, 13.71, 16), box(6.6, 2, 0, 16.3, 13.71, 3), box(0.3, 2, 3, 16.3, 10.51, 13), box(0.3, 10.5, 3, 5.3, 25.51, 13), box(0.3, 2, 13, 6.6, 25.51, 16), box(0.3, 2, 0, 6.6, 25.51, 3));
    protected static final VoxelShape MIDDLE = Shapes.or(box(0, 2, 0.3,16, 9.51, 16),box(0, 9.5, 0.3,16, 25.51, 5.3), box(0, 9.5, 5.3, 16, 10.5, 16));
    protected static final VoxelShape OUTER = Shapes.or(box(0, 2, 0,16, 10.51, 15.7),box(0, 10.5, 10.7,5.3, 25.51, 15.7), box(0.3, 2, 15.7, 5.3, 25.51, 16),box(5.3, 2, 15.7,16, 10.51, 16),box(12.5, 0, 1.7,15, 3, 4.2),box(1, 0, 11.7,3.5, 3, 14.2));
    protected static final VoxelShape LEFT_EDGE = Shapes.or(box(1.5, 0, 12,4, 3, 14.5),box(1.5, 0, 1,4, 3, 3.5), box(0, 2, 6.6, 3, 13.71, 16),box(3, 2, 0.3,16, 10.51, 16),box(3, 10.5, 0.3,16, 25.51, 5.3),box(0, 2, 0.3,3, 25.51, 6.6));
    protected static final VoxelShape RIGHT_EDGE = Shapes.or(box(12.5, 0, 12,15, 3, 14.5),box(12.5, 0, 1,15, 3, 3.5), box(13, 2, 6.6, 16, 13.71, 16),box(0, 2, 0.3,13, 10.51, 16),box(0, 10.5, 0.3,13, 25.51, 5.3),box(13, 2, 0.3,16, 25.51, 6.6));
    protected static final VoxelShape INNER = Shapes.or(box(12.5, 0, 12,15, 3, 14.5),box(1, 0, 1.5,3.5, 3, 4), box(0.3, 2, 0.3, 16, 10.51, 16),box(0.3, 10.5, 5.3,5.3, 25.51, 16),box(0.3, 10.5, 0.3,16, 25.51, 5.3));

    protected static final VoxelShape STANDARD_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, STANDARD);
    protected static final VoxelShape STANDARD_EAST = rotateShape(Direction.WEST, Direction.EAST, STANDARD);
    protected static final VoxelShape STANDARD_NORTH = rotateShape(Direction.WEST, Direction.NORTH, STANDARD);

    protected static final VoxelShape MIDDLE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, MIDDLE);
    protected static final VoxelShape MIDDLE_EAST = rotateShape(Direction.WEST, Direction.EAST, MIDDLE);
    protected static final VoxelShape MIDDLE_WEST = rotateShape(Direction.WEST, Direction.NORTH, MIDDLE);

    protected static final VoxelShape OUTER_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, OUTER);
    protected static final VoxelShape OUTER_EAST = rotateShape(Direction.WEST, Direction.EAST, OUTER);
    protected static final VoxelShape OUTER_WEST = rotateShape(Direction.WEST, Direction.NORTH, OUTER);

    protected static final VoxelShape LEFT_EDGE_NORTH = rotateShape(Direction.WEST, Direction.NORTH, LEFT_EDGE);
    protected static final VoxelShape LEFT_EDGE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, LEFT_EDGE);
    protected static final VoxelShape LEFT_EDGE_EAST = rotateShape(Direction.WEST, Direction.EAST, LEFT_EDGE);

    protected static final VoxelShape RIGHT_EDGE_NORTH = rotateShape(Direction.WEST, Direction.NORTH, RIGHT_EDGE);
    protected static final VoxelShape RIGHT_EDGE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, RIGHT_EDGE);
    protected static final VoxelShape RIGHT_EDGE_EAST = rotateShape(Direction.WEST, Direction.EAST, RIGHT_EDGE);

    protected static final VoxelShape INNER_NORTH = rotateShape(Direction.WEST, Direction.NORTH, INNER);
    protected static final VoxelShape INNER_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, INNER);
    protected static final VoxelShape INNER_EAST = rotateShape(Direction.WEST, Direction.EAST, INNER);
    @SuppressWarnings("deprecated")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        ArmChairShape shape = state.getValue(SHAPE);
        switch(shape) {
            case STRAIGHT:
                return switch (dir) {
                    case NORTH -> STANDARD_NORTH;
                    case SOUTH -> STANDARD_SOUTH;
                    case EAST -> STANDARD_EAST;
                    default -> STANDARD;
                };
            case MIDDLE:
                return switch (dir) {
                    case NORTH -> MIDDLE;
                    case SOUTH -> MIDDLE_EAST;
                    case EAST -> MIDDLE_WEST;
                    default -> MIDDLE_SOUTH;
                };
            case OUTER_LEFT:
                return switch (dir) {
                    case NORTH -> OUTER_WEST;
                    case SOUTH -> OUTER_SOUTH;
                    case EAST -> OUTER_EAST;
                    default -> OUTER;
                };
            case OUTER_RIGHT:
                return switch (dir) {
                    case NORTH -> OUTER_EAST;
                    case SOUTH -> OUTER;
                    case EAST -> OUTER_SOUTH;
                    default -> OUTER_WEST;
                };
            case LEFT_EDGE:
                return switch (dir) {
                    case NORTH -> LEFT_EDGE;
                    case SOUTH -> LEFT_EDGE_EAST;
                    case EAST -> LEFT_EDGE_NORTH;
                    default -> LEFT_EDGE_SOUTH;
                };
            case RIGHT_EDGE:
                return switch (dir) {
                    case NORTH -> RIGHT_EDGE;
                    case SOUTH -> RIGHT_EDGE_EAST;
                    case EAST -> RIGHT_EDGE_NORTH;
                    default -> RIGHT_EDGE_SOUTH;
                };
            case INNER_RIGHT:
                return switch (dir) {
                    case NORTH -> INNER_NORTH;
                    case SOUTH -> INNER_SOUTH;
                    case EAST -> INNER_EAST;
                    default -> INNER;
                };
            case INNER_LEFT:
                return switch (dir) {
                    case NORTH -> INNER;
                    case SOUTH -> INNER_EAST;
                    case EAST -> INNER_NORTH;
                    default -> INNER_SOUTH;
                };
            default:
                return STANDARD;
        }
    }


    public boolean isArmChair(BlockState state) {
        return(state.getBlock().getClass().isAssignableFrom(ArmChairColoredBlock.class) && state.getBlock() instanceof ArmChairColoredBlock);
    }


    private ArmChairShape getShape(BlockState state, BlockGetter world, BlockPos pos) {
        Direction direction3;
        Object direction2;
        Direction direction = state.getValue(FACING);
        BlockState blockState = world.getBlockState(pos.relative(direction));
        boolean right = this.canConnect(world, pos, state.getValue(FACING).getCounterClockWise(), state.getValue(FACING));
        boolean left = this.canConnect(world, pos, state.getValue(FACING).getClockWise(), state.getValue(FACING));
        if (this.isArmChair(blockState) && ((Direction)(direction2 = blockState.getValue(FACING))).getAxis() != state.getValue(FACING).getAxis() && this.isDifferentOrientation(state, world, pos, ((Direction)direction2).getOpposite())) {
            if (direction2 == direction.getCounterClockWise()) {
                return ArmChairShape.OUTER_LEFT;
            }
            return ArmChairShape.OUTER_RIGHT;
        }
        direction2 = world.getBlockState(pos.relative(direction.getOpposite()));
        if (this.isArmChair((BlockState)direction2) && (direction3 = (Direction) ((BlockState)direction2).getValue(FACING)).getAxis() != state.getValue(FACING).getAxis() && this.isDifferentOrientation(state, world, pos, direction3)) {
            if (direction3 == direction.getCounterClockWise()) {
                return ArmChairShape.INNER_LEFT;
            }
            return ArmChairShape.INNER_RIGHT;
        }
        if (left && right) {
            return ArmChairShape.MIDDLE;
        }
        else if (left) {
            return ArmChairShape.LEFT_EDGE;
        }
        else if (right) {
            return ArmChairShape.RIGHT_EDGE;
        }
        return ArmChairShape.STRAIGHT;
    }

    public boolean canConnect(BlockGetter world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.relative(direction));
        return (state.getBlock().getClass().isAssignableFrom(ArmChairColoredBlock.class) && state.getBlock() instanceof ArmChairColoredBlock);
    }

    private boolean isDifferentOrientation(BlockState state, BlockGetter world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.relative(dir));
        return !this.isArmChair(blockState) || blockState.getValue(FACING) != state.getValue(FACING);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction.getAxis().isHorizontal() ? state.setValue(FACING, state.getValue(FACING)).setValue(SHAPE, getShape(state, world, pos)) : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
        Direction facing = PaladinFurnitureMod.getPFMConfig().doChairsFacePlayer() ? ctx.getHorizontalDirection() : ctx.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(SHAPE, getShape(blockState, ctx.getLevel(), ctx.getClickedPos())).setValue(FACING, facing);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(state.getBlock())) {
            this.defaultBlockState().neighborChanged(world, pos, Blocks.AIR, pos, false);
            this.onPlace(this.defaultBlockState(), world, pos, oldState, false);
        }
    }


}

enum ArmChairShape implements StringRepresentable
{
    STRAIGHT("straight"),
    INNER_LEFT("inner_left"),
    INNER_RIGHT("inner_right"),
    OUTER_LEFT("outer_left"),
    OUTER_RIGHT("outer_right"),
    MIDDLE("middle"),
    LEFT_EDGE("left_edge"),
    RIGHT_EDGE("right_edge");


    private final String name;

    ArmChairShape(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}



