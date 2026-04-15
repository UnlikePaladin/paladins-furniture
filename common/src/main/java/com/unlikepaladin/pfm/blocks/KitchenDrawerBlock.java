package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity9x3;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Containers;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class KitchenDrawerBlock extends KitchenCounterBlock implements EntityBlock {
    private float height = 0.36f;
    private final Block baseBlock;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private final BlockState baseBlockState;
    private static final List<FurnitureBlock> WOOD_DRAWERS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_DRAWERS = new ArrayList<>();
    public KitchenDrawerBlock(Properties settings) {
        super(settings);
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        if (!(this.baseBlock instanceof KitchenWallDrawerSmallBlock)) {
            registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(OPEN, false));
        }
        counterFurnitureBlock = new FurnitureBlock(this, "kitchen_drawer");
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(KitchenDrawerBlock.class)){
            WOOD_DRAWERS.add(counterFurnitureBlock);
        }
        else if (this.getClass().isAssignableFrom(KitchenDrawerBlock.class)){
            STONE_DRAWERS.add(counterFurnitureBlock);
        }
    }

    public static Stream<FurnitureBlock> streamWoodDrawers() {
        return WOOD_DRAWERS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneDrawers() {
        return STONE_DRAWERS.stream();
    }
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.is(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Container) {
            Containers.dropContents(world, pos, (Container) blockEntity);
            world.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
    }
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (world instanceof ServerLevel serverWorld && blockEntity instanceof GenericStorageBlockEntity9x3) {
            player.openMenu((GenericStorageBlockEntity9x3)blockEntity);
            player.awardStat(Statistics.DRAWER_SEARCHED);
            PiglinAi.angerNearbyPiglins(serverWorld, player, true);
        }
        return InteractionResult.CONSUME;
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        return LogTableBlock.rotateShape(from, to, shape);
    }

    protected static final VoxelShape STRAIGHT = Shapes.or(box(0, 0, 0,16, 1, 12), box(0, 1, 0,16, 14, 13),box(0, 14, 0,16, 16, 16), box(1, 8, 12,15, 13, 14), box(1, 2, 12, 15, 7, 14), box(6, 4, 14, 10, 5, 15), box(6, 10, 14, 10, 11, 15));
    protected static final VoxelShape STRAIGHT_OPEN = Shapes.or(box(0, 0, 0,16, 1, 12), box(0, 1, 0,16, 14, 13), box(6, 10, 19, 10, 11, 20), box(1, 8, 13, 15, 13, 19), box(0, 14, 0, 16, 16, 16),box(1, 2, 12, 15, 7, 14), box(6, 4, 14, 10, 5, 15));
    protected static final VoxelShape OUTER_CORNER_OPEN = Shapes.or(box(0, 0, 0,12, 1, 12),box(0, 1, 0,13, 14, 13),box(0, 14, 0,16, 16, 16),box(5, 10, 19,8, 11, 20),box(1, 8, 13,12, 13, 19),box(1, 2, 12,12, 7, 14),box(5, 4, 14,8, 5, 15),box(12, 8, 1,14, 13, 12),box(12, 2, 1,14, 7, 12),box(14, 4, 5,15, 5, 8),box(14, 10, 5,15, 11, 8));
    protected static final VoxelShape OUTER_CORNER = Shapes.or(box(0, 0, 0,12, 1, 12),box(0, 1, 0,13, 14, 13),box(0, 14, 0,16, 16, 16),box(5, 10, 14,8, 11, 15),box(1, 8, 13,12, 13, 14),box(1, 2, 12,12, 7, 14),box(5, 4, 14,8, 5, 15),box(12, 8, 1,14, 13, 12),box(12, 2, 1,14, 7, 12),box(14, 4, 5,15, 5, 8),box(14, 10, 5,15, 11, 8));
    protected static final VoxelShape INNER_CORNER = Shapes.or(box(4, 0, 0,16, 1, 16),box(0, 0, 0,4, 1, 11.9),box(3, 1, 0,16, 14, 16),box(0, 1, 0,3, 14, 13),box(0, 14, 0,16, 16, 16),box(1, 2, 12,3, 7, 14),box(1, 8, 12,3, 13, 14),box(2, 8, 14,13, 13, 15));
    protected static final VoxelShape RIGHT_EDGE = Shapes.or(box(0,0,0,14, 1, 12),box(0,1,0,14, 14, 13),box(0,14,0,16, 16, 16),box(14,0,0,16, 14, 16),box(1,8,12,13, 13, 14),box(1, 2, 12,13, 7, 14),box(6, 4, 14,9,5,15),box(6,10,14,9, 11, 15));
    protected static final VoxelShape LEFT_EDGE = Shapes.or(box(2,0,0,16, 1, 12),box(2,1,0,16, 14, 13),box(0,0,0,2, 14, 16),box(0,14,0,16, 16, 16),box(3,8,12,15, 13, 14),box(3, 2, 12,15, 7, 14),box(8,4,14,11, 5, 15),box(8, 10, 14,11, 11, 15));
    protected static final VoxelShape RIGHT_EDGE_OPEN =  Shapes.or(box(0, 14, 0,16, 16, 16),box(14, 0, 0,16, 14, 16),box(0, 0, 0,14, 1, 12),box(0, 1, 0,14, 14, 13),box(6, 10, 19,10, 11, 20),box(1, 8, 13,13, 13, 19),box(1, 2, 12,13, 7, 14),box(6, 4, 14,10, 5, 15));
    protected static final VoxelShape LEFT_EDGE_OPEN = Shapes.or(box(0, 14, 0,16, 16, 16),box(0, 0, 0,2, 14, 16),box(2, 0, 0,16, 1, 12),box(2, 1, 0,16, 14, 13),box(8, 10, 19,12, 11, 20),box(3, 8, 13,15, 13, 19),box(3, 2, 12,15, 7, 14),box(8, 4, 14,12, 5, 15));

    protected static final VoxelShape STRAIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, STRAIGHT);
    protected static final VoxelShape STRAIGHT_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, STRAIGHT_OPEN);
    protected static final VoxelShape STRAIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, STRAIGHT);
    protected static final VoxelShape STRAIGHT_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, STRAIGHT_OPEN);
    protected static final VoxelShape STRAIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, STRAIGHT);
    protected static final VoxelShape STRAIGHT_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, STRAIGHT_OPEN);
    protected static final VoxelShape INNER_CORNER_WEST = rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER);
    protected static final VoxelShape INNER_CORNER_EAST = rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER);
    protected static final VoxelShape INNER_CORNER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER);
    protected static final VoxelShape OUTER_CORNER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER);
    protected static final VoxelShape OUTER_CORNER_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER_OPEN);
    protected static final VoxelShape OUTER_CORNER_EAST = rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER);
    protected static final VoxelShape OUTER_CORNER_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER_OPEN);
    protected static final VoxelShape OUTER_CORNER_WEST = rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER);
    protected static final VoxelShape OUTER_CORNER_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER_OPEN);
    protected static final VoxelShape LEFT_EDGE_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, LEFT_EDGE_OPEN);
    protected static final VoxelShape LEFT_EDGE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, LEFT_EDGE);
    protected static final VoxelShape LEFT_EDGE_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, LEFT_EDGE_OPEN);
    protected static final VoxelShape LEFT_EDGE_WEST =  rotateShape(Direction.NORTH, Direction.WEST, LEFT_EDGE);
    protected static final VoxelShape LEFT_EDGE_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, LEFT_EDGE_OPEN);
    protected static final VoxelShape LEFT_EDGE_EAST = rotateShape(Direction.NORTH, Direction.EAST, LEFT_EDGE);
    protected static final VoxelShape RIGHT_EDGE_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, RIGHT_EDGE_OPEN);
    protected static final VoxelShape RIGHT_EDGE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, RIGHT_EDGE);
    protected static final VoxelShape RIGHT_EDGE_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, RIGHT_EDGE_OPEN);
    protected static final VoxelShape RIGHT_EDGE_WEST =  rotateShape(Direction.NORTH, Direction.WEST, RIGHT_EDGE);
    protected static final VoxelShape RIGHT_EDGE_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, RIGHT_EDGE_OPEN);
    protected static final VoxelShape RIGHT_EDGE_EAST = rotateShape(Direction.NORTH, Direction.EAST, RIGHT_EDGE);

    protected static final VoxelShape MIDDLE = Shapes.or(box(0, 0, 0,16, 16, 13),box(13, 2, 14,14, 6, 15),box(1, 1, 13,15, 15, 14));
    protected static final VoxelShape MIDDLE_OPEN = Shapes.or(box(0, 0, 0,16, 16, 13),box(1, 1, 13,2, 15, 27),box(0, 2, 25,1, 6, 26));
    protected static final VoxelShape MIDDLE_OUTER_CORNER_OPEN = Shapes.or(box(0, 0, 0,13, 16, 13),box(1, 2, 12.75,2, 15, 23.75),box(0, 2.5, 21.75,1, 6.5, 22.75),box(13, 2, 1,14, 15, 12),box(14, 2.5, 10,15, 6.5, 11));
    protected static final VoxelShape MIDDLE_OUTER_CORNER = Shapes.or(box(0, 0, 0,13, 16, 13),box(13, 2, 1,14, 15, 12),box(14, 2.5, 10,15, 6.5, 11),box(1, 2, 12,12, 15, 14),box(10, 2.5, 14,11, 6.5, 15));
    protected static final VoxelShape MIDDLE_INNER_CORNER = Shapes.or(box(3, 0, 13,16, 16, 16),box(0, 0, 0,16, 16, 13),box(2, 1, 14,3, 15, 16),box(0, 1, 13,3, 15, 14));
    protected static final VoxelShape MIDDLE_INNER_CORNER_WEST =  rotateShape(Direction.NORTH, Direction.WEST, MIDDLE_INNER_CORNER);
    protected static final VoxelShape MIDDLE_INNER_CORNER_EAST =  rotateShape(Direction.NORTH, Direction.EAST, MIDDLE_INNER_CORNER);
    protected static final VoxelShape MIDDLE_INNER_CORNER_SOUTH =  rotateShape(Direction.NORTH, Direction.SOUTH, MIDDLE_INNER_CORNER);
    protected static final VoxelShape MIDDLE_OUTER_CORNER_WEST =  rotateShape(Direction.NORTH, Direction.WEST, MIDDLE_OUTER_CORNER);
    protected static final VoxelShape MIDDLE_OUTER_CORNER_EAST =  rotateShape(Direction.NORTH, Direction.EAST, MIDDLE_OUTER_CORNER);
    protected static final VoxelShape MIDDLE_OUTER_CORNER_SOUTH =  rotateShape(Direction.NORTH, Direction.SOUTH, MIDDLE_OUTER_CORNER);
    protected static final VoxelShape MIDDLE_OUTER_CORNER_OPEN_WEST =  rotateShape(Direction.NORTH, Direction.WEST, MIDDLE_OUTER_CORNER_OPEN);
    protected static final VoxelShape MIDDLE_OUTER_CORNER_OPEN_EAST =  rotateShape(Direction.NORTH, Direction.EAST, MIDDLE_OUTER_CORNER_OPEN);
    protected static final VoxelShape MIDDLE_OUTER_CORNER_OPEN_SOUTH =  rotateShape(Direction.NORTH, Direction.SOUTH, MIDDLE_OUTER_CORNER_OPEN);
    protected static final VoxelShape MIDDLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, MIDDLE);
    protected static final VoxelShape MIDDLE_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, MIDDLE_OPEN);
    protected static final VoxelShape MIDDLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, MIDDLE);
    protected static final VoxelShape MIDDLE_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, MIDDLE_OPEN);
    protected static final VoxelShape MIDDLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MIDDLE);
    protected static final VoxelShape MIDDLE_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MIDDLE_OPEN);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(KitchenCounterBlock.FACING);
        boolean right = canConnect(world, pos, state.getValue(KitchenCounterBlock.FACING).getCounterClockWise());
        boolean left = canConnect(world, pos, state.getValue(KitchenCounterBlock.FACING).getClockWise());
        BlockState neighborStateFacing = world.getBlockState(pos.relative(direction));
        BlockState neighborStateOpposite = world.getBlockState(pos.relative(direction.getOpposite()));
        boolean open = state.getValue(OPEN);
        if (canConnectToCounter(neighborStateFacing) && neighborStateFacing.getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                if (direction2 == direction.getCounterClockWise()) {
                    switch (direction) {
                        case NORTH: {
                            if (open) {
                                return OUTER_CORNER_OPEN;
                            }
                            return OUTER_CORNER;
                        }
                        case SOUTH: {
                            if (open) {
                                return OUTER_CORNER_OPEN_SOUTH;
                            }
                            return OUTER_CORNER_SOUTH;
                        }
                        case EAST: {
                            if (open) {
                                return OUTER_CORNER_OPEN_EAST;
                            }
                            return OUTER_CORNER_EAST;
                        }
                        default: {
                            if (open) {
                                return OUTER_CORNER_OPEN_WEST;
                            }
                            return OUTER_CORNER_WEST;
                        }
                    }
                }
                else {
                    switch (direction) {
                        case NORTH: {
                            if (open) {
                                return OUTER_CORNER_OPEN_EAST;
                            }
                            return OUTER_CORNER_EAST;
                        }
                        case SOUTH: {
                            if (open) {
                                return OUTER_CORNER_OPEN_WEST;
                            }
                            return OUTER_CORNER_WEST;
                        }
                        case EAST: {
                            if (open) {
                                return OUTER_CORNER_OPEN_SOUTH;
                            }
                            return OUTER_CORNER_SOUTH;
                        }
                        default: {
                            if (open) {
                                return OUTER_CORNER_OPEN;
                            }
                            return OUTER_CORNER;
                        }
                    }
                }
            } else {
                switch (direction) {
                    case NORTH: {
                        if (open) {
                            return STRAIGHT_OPEN;
                        }
                        return STRAIGHT;
                    }
                    case SOUTH: {
                        if (open) {
                            return STRAIGHT_OPEN_SOUTH;
                        }
                        return STRAIGHT_SOUTH;
                    }
                    case EAST: {
                        if (open) {
                            return STRAIGHT_OPEN_EAST;
                        }
                        return STRAIGHT_EAST;
                    }
                    default: {
                        if (open) {
                            return STRAIGHT_OPEN_WEST;
                        }
                        return STRAIGHT_WEST;
                    }
                }
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
                switch (direction) {
                    case NORTH: {
                        if (open) {
                            return STRAIGHT_OPEN;
                        }
                        return STRAIGHT;
                    }
                    case SOUTH: {
                        if (open) {
                            return STRAIGHT_OPEN_SOUTH;
                        }
                        return STRAIGHT_SOUTH;
                    }
                    case EAST: {
                        if (open) {
                            return STRAIGHT_OPEN_EAST;
                        }
                        return STRAIGHT_EAST;
                    }
                    default: {
                        if (open) {
                            return STRAIGHT_OPEN_WEST;
                        }
                        return STRAIGHT_WEST;
                    }
                }
            }
        }
        else if (left && right) {
            switch (direction) {
                case NORTH: {
                    if (open) {
                        return STRAIGHT_OPEN;
                    }
                    return STRAIGHT;
                }
                case SOUTH: {
                    if (open) {
                        return STRAIGHT_OPEN_SOUTH;
                    }
                    return STRAIGHT_SOUTH;
                }
                case EAST: {
                    if (open) {
                        return STRAIGHT_OPEN_EAST;
                    }
                    return STRAIGHT_EAST;
                }
                default: {
                    if (open) {
                        return STRAIGHT_OPEN_WEST;
                    }
                    return STRAIGHT_WEST;
                }
            }
        } else if (left) {
            switch (direction) {
                case NORTH: {
                    if (open)
                        return LEFT_EDGE_OPEN;
                    return LEFT_EDGE;
                }
                case SOUTH: {
                    if (open)
                        return LEFT_EDGE_OPEN_SOUTH;
                    return LEFT_EDGE_SOUTH;
                }
                case EAST: {
                    if (open)
                        return LEFT_EDGE_OPEN_EAST;
                    return LEFT_EDGE_EAST;
                }
                default: {
                    if (open)
                        return LEFT_EDGE_OPEN_WEST;
                    return LEFT_EDGE_WEST;
                }
            }
        } else if (right) {
            switch (direction) {
                case NORTH: {
                    if (open)
                        return RIGHT_EDGE_OPEN;
                    return RIGHT_EDGE;
                }
                case SOUTH: {
                    if (open)
                        return RIGHT_EDGE_OPEN_SOUTH;
                    return RIGHT_EDGE_SOUTH;
                }
                case EAST: {
                    if (open)
                        return RIGHT_EDGE_OPEN_EAST;
                    return RIGHT_EDGE_EAST;
                }
                default: {
                    if (open)
                        return RIGHT_EDGE_OPEN_WEST;
                    return RIGHT_EDGE_WEST;
                }
            }
        } else {
            switch (direction) {
                case NORTH: {
                    if (open) {
                        return STRAIGHT_OPEN;
                    }
                    return STRAIGHT;
                }
                case SOUTH: {
                    if (open) {
                        return STRAIGHT_OPEN_SOUTH;
                    }
                    return STRAIGHT_SOUTH;
                }
                case EAST: {
                    if (open) {
                        return STRAIGHT_OPEN_EAST;
                    }
                    return STRAIGHT_EAST;
                }
                default: {
                    if (open) {
                        return STRAIGHT_OPEN_WEST;
                    }
                    return STRAIGHT_WEST;
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return GenericStorageBlockEntity9x3.getFactory().create(pos,state);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

}

