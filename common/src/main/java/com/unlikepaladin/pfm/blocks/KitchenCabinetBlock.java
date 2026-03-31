package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity9x3;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
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
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenCounterBlock.rotateShape;

public class KitchenCabinetBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private final BlockState baseBlockState;
    private final Block baseBlock;
    private static final List<FurnitureBlock> WOOD_CABINETS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CABINETS = new ArrayList<>();

    public KitchenCabinetBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(OPEN, false));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(KitchenCabinetBlock.class)){
            WOOD_CABINETS.add(new FurnitureBlock(this, "kitchen_cabinet"));
        }
        else if (this.getClass().isAssignableFrom(KitchenCabinetBlock.class)){
            STONE_CABINETS.add(new FurnitureBlock(this, "kitchen_cabinet"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodCabinets() {
        return WOOD_CABINETS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneCabinets() {
        return STONE_CABINETS.stream();
    }

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    protected static final VoxelShape STRAIGHT = Shapes.or(box(0, 0, 0,16, 16, 8), box(0, 1, 8,16, 16, 9), box(6, 3, 9,7, 7, 10), box(9, 3, 9,10, 7, 10));
    protected static final VoxelShape INNER_CORNER = Shapes.or(box(0, 0, 0,8, 16, 8),box(1, 3, 9,2, 7, 10), box(0, 1, 8,8, 16, 9),box(7, 1, 9,8, 16, 16),box(8, 0, 0,16, 16, 16),box(6, 3, 13,7, 7, 14));
    protected static final VoxelShape OUTER_CORNER = Shapes.or(box(0, 0, 0,8, 16, 8),box(6, 3, 9,7, 7, 10),box(0, 1, 8,8, 16, 9),box(8, 1, 0,9, 16, 8),box(9, 3, 6,10, 7, 7));

    protected static final VoxelShape STRAIGHT_OPEN = Shapes.or(box(0, 0, 0,16, 16, 8), box(16, 3, 14,17, 7, 15), box(15, 1, 8,16, 16, 16), box(-1, 3, 14,0, 7, 15),box(0, 1, 8,1, 16, 16));
    protected static final VoxelShape INNER_CORNER_OPEN = Shapes.or(box(7, 1, 9,8, 16, 16),box(8, 0, 8,16, 16, 16), box(6, 3, 13,7, 7, 14),box(0, 0, 0,16, 16, 8));
    protected static final VoxelShape OUTER_CORNER_OPEN = Shapes.or(box(0, 0, 0,8, 16, 8),box(0, 1, 8,1, 16, 16),box(-1, 3, 14,0, 7, 15),box(8, 1, 0,9, 16, 8),box(9, 3, 6,10, 7, 7));

    protected static final VoxelShape STRAIGHT_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, STRAIGHT);
    protected static final VoxelShape STRAIGHT_EAST = rotateShape(Direction.NORTH, Direction.EAST, STRAIGHT);
    protected static final VoxelShape STRAIGHT_WEST = rotateShape(Direction.NORTH, Direction.WEST, STRAIGHT);
    protected static final VoxelShape STRAIGHT_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, STRAIGHT_OPEN);
    protected static final VoxelShape STRAIGHT_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, STRAIGHT_OPEN);
    protected static final VoxelShape STRAIGHT_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, STRAIGHT_OPEN);

    protected static final VoxelShape INNER_CORNER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER);
    protected static final VoxelShape INNER_CORNER_EAST = rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER);
    protected static final VoxelShape INNER_CORNER_WEST = rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER);
    protected static final VoxelShape INNER_CORNER_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER_OPEN);
    protected static final VoxelShape INNER_CORNER_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER_OPEN);
    protected static final VoxelShape INNER_CORNER_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER_OPEN);

    protected static final VoxelShape OUTER_CORNER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER);
    protected static final VoxelShape OUTER_CORNER_EAST = rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER);
    protected static final VoxelShape OUTER_CORNER_WEST = rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER);
    protected static final VoxelShape OUTER_CORNER_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER_OPEN);
    protected static final VoxelShape OUTER_CORNER_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER_OPEN);
    protected static final VoxelShape OUTER_CORNER_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER_OPEN);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(KitchenCounterBlock.FACING);
        BlockState neighborStateFacing = view.getBlockState(pos.relative(direction));
        BlockState neighborStateOpposite = view.getBlockState(pos.relative(direction.getOpposite()));
        boolean open = state.getValue(OPEN);
        if (isCabinet(neighborStateFacing) && neighborStateFacing.getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(state, view, pos, direction2.getOpposite())) {
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
        else if (isCabinet(neighborStateOpposite) && neighborStateOpposite.getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction3;
            if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
            }
            else {
                direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
            }
            if (direction3.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(state, view, pos, direction3)) {
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
        else {
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
    public boolean isCabinet(BlockState state) {
        return state.getBlock() instanceof KitchenCabinetBlock;
    }

    public boolean isDifferentOrientation(BlockState state, BlockGetter world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.relative(dir));
        return !this.isCabinet(blockState);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(state.getBlock())) {
            this.baseBlockState.neighborChanged(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onPlace(this.baseBlockState, world, pos, oldState, false);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GenericStorageBlockEntity9x3) {
            player.openMenu((GenericStorageBlockEntity9x3)blockEntity);
            player.awardStat(Statistics.CABINET_SEARCHED);
            PiglinAi.angerNearbyPiglins(player, true);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomHoverName() && (blockEntity = world.getBlockEntity(pos)) instanceof GenericStorageBlockEntity9x3) {
            ((GenericStorageBlockEntity9x3)blockEntity).setCustomName(itemStack.getHoverName());
        }
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return GenericStorageBlockEntity9x3.getFactory().create(pos, state);
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}
