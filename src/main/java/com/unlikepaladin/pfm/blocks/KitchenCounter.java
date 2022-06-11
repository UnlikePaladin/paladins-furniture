package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
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
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty DOWN = Properties.DOWN;

    private final BlockState baseBlockState;
    public KitchenCounter(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(UP, false).with(DOWN,false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(SHAPE);
        stateManager.add(UP);
        stateManager.add(DOWN);
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
        boolean up = connectsVertical(world.getBlockState(blockPos.up()).getBlock());
        boolean down = connectsVertical(world.getBlockState(blockPos.down()).getBlock());
        return blockState.with(SHAPE, getShape(blockState, world, blockPos)).with(UP, up).with(DOWN, down);
    }
    public static boolean connectsVertical(Block block) {
        return block instanceof KitchenCounter || block instanceof KitchenCounterOven || block instanceof Fridge || block instanceof Freezer;
    }
    private static CounterShape getShape(BlockState state, BlockView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockState blockState = world.getBlockState(pos.offset(direction));
        if (isCounter(blockState)) {
            Direction direction2 = blockState.get(FACING);

            if (direction2.getAxis() != state.get(FACING).getAxis() && isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                if (direction2 == direction.rotateYCounterclockwise()) {
                    return CounterShape.OUTER_LEFT;
                }

                return CounterShape.OUTER_RIGHT;
            }
        }

        BlockState direction2 = world.getBlockState(pos.offset(direction.getOpposite()));
        if (isCounter(direction2)) {
            Direction direction3 = direction2.get(FACING);
            if (direction3.getAxis() != state.get(FACING).getAxis() && isDifferentOrientation(state, world, pos, direction3)) {
                if (direction3 == direction.rotateYCounterclockwise()) {
                    return CounterShape.INNER_LEFT;
                }

                return CounterShape.INNER_RIGHT;
            }
        }
        boolean right = canConnect(world, pos, state.get(FACING).rotateYCounterclockwise(), state.get(FACING));
        boolean left = canConnect(world, pos, state.get(FACING).rotateYClockwise(), state.get(FACING));
        if (left && right) {
            return CounterShape.STRAIGHT;
        } else if (left) {
            return CounterShape.LEFT_EDGE;
        } else if (right) {
            return CounterShape.RIGHT_EDGE;
        }
        return CounterShape.STRAIGHT;

    }

    public static boolean canConnect(BlockView world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        boolean canConnect = (state.getBlock() instanceof KitchenCounter || state.getBlock() instanceof AbstractFurnaceBlock || state.getBlock() instanceof AbstractCauldronBlock);
        return canConnect;
    }

    private static boolean isDifferentOrientation(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.offset(dir));
        return !canConnectToCounter(blockState); //|| blockState.get(FACING) != state.get(FACING);
    }

    public static boolean canConnectToCounter(BlockState state) {
        return state.getBlock() instanceof KitchenCounter || state.getBlock() instanceof AbstractFurnaceBlock || state.getBlock() instanceof AbstractCauldronBlock;
    }

    public static boolean isCounter(BlockState state) {
        return state.getBlock() instanceof KitchenCounter;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
       if ( direction.getAxis().isHorizontal()) {
           return state.with(SHAPE, getShape(state, world, pos));
       }
       else if (direction.getAxis().isVertical()) {
           boolean up = connectsVertical(world.getBlockState(pos.up()).getBlock());
           boolean down = connectsVertical(world.getBlockState(pos.down()).getBlock());
           return state.with(UP, up).with(DOWN, down);
       }
       else{
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @SuppressWarnings("deprecated")
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.baseBlockState.neighborUpdate(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }
    /**
     * Method to rotate VoxelShapes from this random Forge Forums thread: https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/
     */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }
        return buffer[0];
    }

    protected static final VoxelShape STRAIGHT = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 1, 12), createCuboidShape(0, 1, 0,16, 14, 13), createCuboidShape(0, 14, 0,16, 16, 16));
    protected static final VoxelShape INNER_CORNER = VoxelShapes.union(createCuboidShape(0, 14, 0,16, 16, 16),createCuboidShape(0, 1, 0,16, 14, 13),createCuboidShape(3, 1, 13,16, 14, 16));
    protected static final VoxelShape OUTER_CORNER = VoxelShapes.union(createCuboidShape(0, 14, 0,16, 16, 16),createCuboidShape(0, 1, 0,13, 14, 13),createCuboidShape(0, 0, 0,12, 1, 12));
    protected static final VoxelShape LEFT_EDGE = VoxelShapes.union(createCuboidShape(2, 0, 0,16, 1, 12), createCuboidShape(2, 1, 0,16, 14, 13), createCuboidShape(0, 0, 0,2, 14, 16),createCuboidShape(0, 14, 0,16, 16, 16));
    protected static final VoxelShape RIGHT_EDGE = VoxelShapes.union(createCuboidShape(0, 0, 0,14, 1, 12), createCuboidShape(0, 1, 0,14, 14, 13), createCuboidShape(14, 0, 0,16, 14, 16),createCuboidShape(0, 14, 0,16, 16, 16));
    protected static final VoxelShape MIDDLE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 16, 13));
    protected static final VoxelShape INNER_MIDDLE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 16, 13), createCuboidShape(3, 0, 13,16, 16, 16));
    protected static final VoxelShape OUTER_MIDDLE = VoxelShapes.union(createCuboidShape(0, 0, 0,13, 16, 13));
    protected static final VoxelShape BOTTOM = VoxelShapes.union(createCuboidShape(0, 1, 0, 16, 16, 13),createCuboidShape(0, 0, 0,16, 1, 12));
    protected static final VoxelShape OUTER_BOTTOM = VoxelShapes.union(createCuboidShape(0, 0, 0, 12, 1, 12),createCuboidShape(0, 1, 0,13, 16, 13));
    protected static final VoxelShape INNER_BOTTOM = VoxelShapes.union(createCuboidShape(0, 1, 0, 16, 16, 13),createCuboidShape(3, 1, 13,16, 16, 16),createCuboidShape(0, 0, 0,16, 1, 12),createCuboidShape(4, 0, 12,16, 1, 16));

    protected static final VoxelShape MIDDLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MIDDLE);
    protected static final VoxelShape MIDDLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, MIDDLE);
    protected static final VoxelShape MIDDLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, MIDDLE);

    protected static final VoxelShape INNER_MIDDLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, INNER_MIDDLE);
    protected static final VoxelShape INNER_MIDDLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, INNER_MIDDLE);
    protected static final VoxelShape INNER_MIDDLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, INNER_MIDDLE);

    protected static final VoxelShape OUTER_MIDDLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_MIDDLE);
    protected static final VoxelShape OUTER_MIDDLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, OUTER_MIDDLE);
    protected static final VoxelShape OUTER_MIDDLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, OUTER_MIDDLE);

    protected static final VoxelShape OUTER_BOTTOM_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_BOTTOM);
    protected static final VoxelShape OUTER_BOTTOM_EAST = rotateShape(Direction.NORTH, Direction.EAST, OUTER_BOTTOM);
    protected static final VoxelShape OUTER_BOTTOM_WEST = rotateShape(Direction.NORTH, Direction.WEST, OUTER_BOTTOM);

    protected static final VoxelShape INNER_BOTTOM_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, INNER_BOTTOM);
    protected static final VoxelShape INNER_BOTTOM_EAST = rotateShape(Direction.NORTH, Direction.EAST, INNER_BOTTOM);
    protected static final VoxelShape INNER_BOTTOM_WEST = rotateShape(Direction.NORTH, Direction.WEST, INNER_BOTTOM);

    protected static final VoxelShape BOTTOM_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, BOTTOM);
    protected static final VoxelShape BOTTOM_EAST = rotateShape(Direction.NORTH, Direction.EAST, BOTTOM);
    protected static final VoxelShape BOTTOM_WEST = rotateShape(Direction.NORTH, Direction.WEST, BOTTOM);

    protected static final VoxelShape STRAIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, STRAIGHT);
    protected static final VoxelShape STRAIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, STRAIGHT);
    protected static final VoxelShape STRAIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, STRAIGHT);

    protected static final VoxelShape INNER_CORNER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER);
    protected static final VoxelShape INNER_CORNER_EAST = rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER);
    protected static final VoxelShape INNER_CORNER_WEST = rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER);

    protected static final VoxelShape OUTER_CORNER_SOUTH =  rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER);
    protected static final VoxelShape OUTER_CORNER_EAST = rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER);
    protected static final VoxelShape OUTER_CORNER_WEST = rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER);

    protected static final VoxelShape LEFT_EDGE_SOUTH =  rotateShape(Direction.NORTH, Direction.SOUTH, LEFT_EDGE);
    protected static final VoxelShape LEFT_EDGE_EAST = rotateShape(Direction.NORTH, Direction.EAST, LEFT_EDGE);
    protected static final VoxelShape LEFT_EDGE_WEST = rotateShape(Direction.NORTH, Direction.WEST, LEFT_EDGE);

    protected static final VoxelShape RIGHT_EDGE_SOUTH =  rotateShape(Direction.NORTH, Direction.SOUTH, RIGHT_EDGE);
    protected static final VoxelShape RIGHT_EDGE_EAST = rotateShape(Direction.NORTH, Direction.EAST, RIGHT_EDGE);
    protected static final VoxelShape RIGHT_EDGE_WEST = rotateShape(Direction.NORTH, Direction.WEST, RIGHT_EDGE);
    @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        CounterShape shape = state.get(SHAPE);
        boolean up = state.get(UP);
        boolean down = state.get(DOWN);
        if (down) {
            return switch (shape) {
                case INNER_LEFT -> switch (dir) {
                    case NORTH -> INNER_MIDDLE_WEST;
                    case SOUTH -> INNER_MIDDLE_EAST;
                    case WEST -> INNER_MIDDLE_SOUTH;
                    default -> INNER_MIDDLE;
                };
                case INNER_RIGHT -> switch (dir) {
                    case NORTH -> INNER_MIDDLE;
                    case SOUTH -> INNER_MIDDLE_SOUTH;
                    case WEST -> INNER_MIDDLE_WEST;
                    default -> INNER_MIDDLE_EAST;
                };
                case OUTER_RIGHT -> switch (dir) {
                    case NORTH -> OUTER_MIDDLE_EAST;
                    case SOUTH -> OUTER_MIDDLE_WEST;
                    case WEST -> OUTER_MIDDLE;
                    default -> OUTER_MIDDLE_SOUTH;
                };
                case OUTER_LEFT -> switch (dir) {
                    case NORTH -> OUTER_MIDDLE;
                    case SOUTH -> OUTER_MIDDLE_SOUTH;
                    case WEST -> OUTER_MIDDLE_WEST;
                    default -> OUTER_MIDDLE_EAST;
                };
                default -> switch (dir) {
                    case NORTH -> MIDDLE;
                    case SOUTH -> MIDDLE_SOUTH;
                    case WEST -> MIDDLE_WEST;
                    default -> MIDDLE_EAST;
                };
            };
        } else if (up) {
            return switch (shape) {
                case OUTER_RIGHT -> switch (dir) {
                    case NORTH -> OUTER_BOTTOM_EAST;
                    case SOUTH -> OUTER_BOTTOM_WEST;
                    case WEST -> OUTER_BOTTOM;
                    default -> OUTER_BOTTOM_SOUTH;
                };
                case OUTER_LEFT -> switch (dir) {
                    case NORTH -> OUTER_BOTTOM;
                    case SOUTH -> OUTER_BOTTOM_SOUTH;
                    case WEST -> OUTER_BOTTOM_WEST;
                    default -> OUTER_BOTTOM_EAST;
                };
                case INNER_LEFT -> switch (dir) {
                    case NORTH -> INNER_BOTTOM_WEST;
                    case SOUTH -> INNER_BOTTOM_EAST;
                    case WEST -> INNER_BOTTOM_SOUTH;
                    default -> INNER_BOTTOM;
                };
                case INNER_RIGHT -> switch (dir) {
                    case NORTH -> INNER_BOTTOM;
                    case SOUTH -> INNER_BOTTOM_SOUTH;
                    case WEST -> INNER_BOTTOM_WEST;
                    default -> INNER_BOTTOM_EAST;
                };
                default -> switch (dir) {
                    case NORTH -> BOTTOM;
                    case SOUTH -> BOTTOM_SOUTH;
                    case WEST -> BOTTOM_WEST;
                    default -> BOTTOM_EAST;
                };
            };
        }

            return switch (shape) {
                case STRAIGHT -> switch (dir) {
                    case NORTH -> STRAIGHT;
                    case SOUTH -> STRAIGHT_SOUTH;
                    case EAST -> STRAIGHT_EAST;
                    default -> STRAIGHT_WEST;
                };
                case INNER_LEFT -> switch (dir) {
                    case NORTH -> INNER_CORNER_WEST;
                    case SOUTH -> INNER_CORNER_EAST;
                    case EAST -> INNER_CORNER;
                    default -> INNER_CORNER_SOUTH;
                };
                case INNER_RIGHT -> switch (dir) {
                    case NORTH -> INNER_CORNER;
                    case SOUTH -> INNER_CORNER_SOUTH;
                    case EAST -> INNER_CORNER_EAST;
                    default -> INNER_CORNER_WEST;
                };
                case OUTER_LEFT -> switch (dir) {
                    case NORTH -> OUTER_CORNER;
                    case SOUTH -> OUTER_CORNER_SOUTH;
                    case EAST -> OUTER_CORNER_EAST;
                    default -> OUTER_CORNER_WEST;
                };
                case OUTER_RIGHT -> switch (dir) {
                    case NORTH -> OUTER_CORNER_EAST;
                    case SOUTH -> OUTER_CORNER_WEST;
                    case EAST -> OUTER_CORNER_SOUTH;
                    default -> OUTER_CORNER;
                };
                case LEFT_EDGE -> switch (dir) {
                    case NORTH -> LEFT_EDGE;
                    case SOUTH -> LEFT_EDGE_SOUTH;
                    case EAST -> LEFT_EDGE_EAST;
                    default -> LEFT_EDGE_WEST;
                };
                case RIGHT_EDGE -> switch (dir) {
                    case NORTH -> RIGHT_EDGE;
                    case SOUTH -> RIGHT_EDGE_SOUTH;
                    case EAST -> RIGHT_EDGE_EAST;
                    default -> RIGHT_EDGE_WEST;
                };
            };
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

