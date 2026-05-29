package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class KitchenCounterBlock extends HorizontalDirectionalBlock {
    private float height = 0.36f;
    private final Block baseBlock;
    protected FurnitureBlock counterFurnitureBlock;
    private final BlockState baseBlockState;
    private static final List<FurnitureBlock> WOOD_COUNTERS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_COUNTERS = new ArrayList<>();
    public KitchenCounterBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        counterFurnitureBlock = new FurnitureBlock(this, "kitchen_counter");
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(KitchenCounterBlock.class)){
            WOOD_COUNTERS.add(counterFurnitureBlock);
        }
        else if (this.getClass().isAssignableFrom(KitchenCounterBlock.class)){
            STONE_COUNTERS.add(counterFurnitureBlock);
        }
    }

    public static final MapCodec<KitchenCounterBlock> CODEC = simpleCodec(KitchenCounterBlock::new);
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamWoodCounters() {
        return WOOD_COUNTERS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneCounters() {
        return STONE_COUNTERS.stream();
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    public boolean canConnect(BlockGetter world, BlockPos pos, Direction direction)
    {
        BlockState state = world.getBlockState(pos.relative(direction));
        return (isCounter(state) || state.getBlock() instanceof AbstractFurnaceBlock || state.getBlock() instanceof AbstractCauldronBlock) || isCookingForBlockHeadsBlock(state.getBlock().getDescriptionId());
    }

    public static boolean isCookingForBlockHeadsBlock(String key) {
        return key.contains("cookingforblockheads") && (key.contains("cooking_table") || key.contains("oven") || key.contains("sink") || key.contains("corner") || key.contains("cabinet") || key.contains("counter"));
    }

    public boolean isDifferentOrientation(BlockState state, BlockGetter world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.relative(dir));
        return !canConnectToCounter(blockState); //|| blockState.getValue(FACING) != state.getValue(FACING);
    }

    public boolean canConnectToCounter(BlockState state) {
        return isCounter(state) || state.getBlock() instanceof AbstractFurnaceBlock || state.getBlock() instanceof AbstractCauldronBlock || isCookingForBlockHeadsBlock(state.getBlock().getDescriptionId());
    }

    public boolean isCounter(BlockState state) {
        boolean doDifferentCountersConnect;
        if (PaladinFurnitureMod.getPFMConfig().doCountersOfDifferentMaterialsConnect()) {
            doDifferentCountersConnect = state.getBlock() instanceof KitchenCounterBlock;
        }
        else if (state.getBlock() instanceof KitchenCounterBlock){
            doDifferentCountersConnect = (this.counterFurnitureBlock.getBaseMaterial() == ((KitchenCounterBlock)state.getBlock()).counterFurnitureBlock.getBaseMaterial());
        }
        else {
            doDifferentCountersConnect = false;
        }
        return (doDifferentCountersConnect || state.getBlock() instanceof KitchenWallCounterBlock || state.getBlock() instanceof KitchenWallDrawerBlock);
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        return LogTableBlock.rotateShape(from, to, shape);
    }

    protected static final VoxelShape STRAIGHT = Shapes.or(box(0, 0, 0,16, 1, 12), box(0, 1, 0,16, 14, 13), box(0, 14, 0,16, 16, 16));
    protected static final VoxelShape INNER_CORNER = Shapes.or(box(0, 14, 0,16, 16, 16),box(0, 1, 0,16, 14, 13),box(3, 1, 13,16, 14, 16));
    protected static final VoxelShape OUTER_CORNER = Shapes.or(box(0, 14, 0,16, 16, 16),box(0, 1, 0,13, 14, 13),box(0, 0, 0,12, 1, 12));
    protected static final VoxelShape LEFT_EDGE = Shapes.or(box(2, 0, 0,16, 1, 12), box(2, 1, 0,16, 14, 13), box(0, 0, 0,2, 14, 16),box(0, 14, 0,16, 16, 16));
    protected static final VoxelShape RIGHT_EDGE = Shapes.or(box(0, 0, 0,14, 1, 12), box(0, 1, 0,14, 14, 13), box(14, 0, 0,16, 14, 16),box(0, 14, 0,16, 16, 16));
    protected static final VoxelShape MIDDLE = Shapes.or(box(0, 0, 0, 16, 16, 13));
    protected static final VoxelShape INNER_MIDDLE = Shapes.or(box(0, 0, 0, 16, 16, 13), box(3, 0, 13,16, 16, 16));
    protected static final VoxelShape OUTER_MIDDLE = Shapes.or(box(0, 0, 0,13, 16, 13));

    protected static final VoxelShape MIDDLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MIDDLE);
    protected static final VoxelShape MIDDLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, MIDDLE);
    protected static final VoxelShape MIDDLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, MIDDLE);

    protected static final VoxelShape INNER_MIDDLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, INNER_MIDDLE);
    protected static final VoxelShape INNER_MIDDLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, INNER_MIDDLE);
    protected static final VoxelShape INNER_MIDDLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, INNER_MIDDLE);

    protected static final VoxelShape OUTER_MIDDLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_MIDDLE);
    protected static final VoxelShape OUTER_MIDDLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, OUTER_MIDDLE);
    protected static final VoxelShape OUTER_MIDDLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, OUTER_MIDDLE);


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
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(KitchenCounterBlock.FACING);
        boolean right = canConnect(world, pos, state.getValue(KitchenCounterBlock.FACING).getCounterClockWise());
        boolean left = canConnect(world, pos, state.getValue(KitchenCounterBlock.FACING).getClockWise());
        BlockState neighborStateFacing = world.getBlockState(pos.relative(direction));
        BlockState neighborStateOpposite = world.getBlockState(pos.relative(direction.getOpposite()));
        if (canConnectToCounter(neighborStateFacing) && neighborStateFacing.getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                if (direction2 == direction.getCounterClockWise()) {
                    switch (direction) {
                        case NORTH: return OUTER_CORNER;
                        case SOUTH: return OUTER_CORNER_SOUTH;
                        case EAST: return OUTER_CORNER_EAST;
                        default: return OUTER_CORNER_WEST;
                    }
                }
                else {
                    switch (direction) {
                        case NORTH: return OUTER_CORNER_EAST;
                        case SOUTH: return OUTER_CORNER_WEST;
                        case EAST: return OUTER_CORNER_SOUTH;
                        default: return OUTER_CORNER;
                    }
                }
            } else {
                return getMiddleShape(direction, left, right);
            }
        }
        else if (canConnectToCounter(neighborStateOpposite) && neighborStateOpposite.getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction3;
            if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
            }
            else {
                direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
            }
            if (direction3.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(state, world, pos, direction3)) {
                if (direction3 == direction.getCounterClockWise()) {
                    switch (direction) {
                        case NORTH: return INNER_CORNER_WEST;
                        case SOUTH: return INNER_CORNER_EAST;
                        case EAST: return INNER_CORNER;
                        default: return INNER_CORNER_SOUTH;
                    }
                } else {
                    switch (direction) {
                        case NORTH: return INNER_CORNER;
                        case SOUTH: return INNER_CORNER_SOUTH;
                        case EAST: return INNER_CORNER_EAST;
                        default: return INNER_CORNER_WEST;
                    }
                }
            } else {
                return getMiddleShape(direction, left, right);
            }
        }
        else {
            return getMiddleShape(direction, left, right);
        }
    }

    private VoxelShape getMiddleShape(Direction direction, boolean left, boolean right) {
        if (left && right) {
            switch (direction) {
                case NORTH: return STRAIGHT;
                case SOUTH: return STRAIGHT_SOUTH;
                case EAST: return STRAIGHT_EAST;
                default: return STRAIGHT_WEST;
            }
        } else if (left) {
            switch (direction) {
                case NORTH: return LEFT_EDGE;
                case SOUTH: return LEFT_EDGE_SOUTH;
                case EAST: return LEFT_EDGE_EAST;
                default: return LEFT_EDGE_WEST;
            }
        } else if (right) {
            switch (direction) {
                case NORTH: return RIGHT_EDGE;
                case SOUTH: return RIGHT_EDGE_SOUTH;
                case EAST: return RIGHT_EDGE_EAST;
                default: return RIGHT_EDGE_WEST;
            }
        } else {
            switch (direction) {
                case NORTH: return STRAIGHT;
                case SOUTH: return STRAIGHT_SOUTH;
                case EAST: return STRAIGHT_EAST;
                default: return STRAIGHT_WEST;
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (AbstractSittableBlock.isWoodBased(state)) {
            return 20;
        }
        return 0;
    }
}