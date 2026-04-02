package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class FridgeBlock extends HorizontalFacingBlockWithEntity {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    private final Block baseBlock;
    private final BlockState baseBlockState;
    private static final List<FurnitureBlock> FRIDGES = new ArrayList<>();
    private final Supplier<FreezerBlock> freezer;
    public FridgeBlock(Properties settings, Supplier<FreezerBlock> freezer) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(OPEN, false));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        FRIDGES.add(new FurnitureBlock(this, "fridge"));
        this.freezer = freezer;
    }

    public static Stream<FurnitureBlock> streamFridges() {
        return FRIDGES.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
    }

    // Todo: Also implement this codec properly
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return super.canSurvive(state, world, pos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomHoverName() && (blockEntity = world.getBlockEntity(pos)) instanceof FridgeBlockEntity) {
            ((FridgeBlockEntity)blockEntity).setCustomName(itemStack.getHoverName());
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
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
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction.getAxis().isVertical() && neighborState.getBlock() == this ? neighborState.getValue(FACING) == state.getValue(FACING) ? state.setValue(OPEN, neighborState.getValue(OPEN)) : state : state;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FridgeBlockEntity) {
            player.openMenu((FridgeBlockEntity)blockEntity);
            player.awardStat(Statistics.FRIDGE_OPENED);
            PiglinAi.angerNearbyPiglins(player, true);
        }
        return InteractionResult.CONSUME;
    }

    public void onBreakInCreative(Level world, BlockPos pos, BlockState state, Player player) {
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && player.isCreative()) {
            this.onBreakInCreative(world, pos, state, player);
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        if (stateFrom.is(this)) {
            return true;
        }
        return super.skipRendering(state, stateFrom, direction);
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1.0f;
    }

    protected static final Map<Direction, VoxelShape> FRIDGE_SINGLE = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(12, 3, -0.5,13, 14, 1), box(1, 1, 1,15, 16, 2), box(1, 0, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_TOP = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(12, 0, -0.5, 13, 15, 1), box(1, 0, 1, 15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(12, 0, -0.5, 13, 16, 1), box(1, 0, 1,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_BOTTOM = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(12, 4, -0.5,13, 16, 1), box(1, 1, 1,15, 16, 2), box(1, 0, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(12, 4, -0.5, 13, 20, 1), box(1, 1, 1,15, 21, 2),box(1, 0, 2,15, 32, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE_FREEZER = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(12, 0, -0.5,13, 20, 1),box(1, 0, 1,15, 21, 16),box(1, 21, 2,15, 32, 16)));}};

    protected static final Map<Direction, VoxelShape> FRIDGE_SINGLE_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(-0.5, 3, -10,1, 14, -9), box(1, 1, -12,3, 16, 2),box(1, 0, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_TOP_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(-0.5, 0, -10,1, 15, -9), box(1, 0, -12,3, 16, 2),box(1, 0, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(-0.5, 0, -10,1, 16, -9),box(1, 0, -12,3, 16, 2),box(1, 0, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_BOTTOM_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(-0.5, 4, -10,1, 16, -9),box(1, 1, -12,3, 16, 2),box(1, 0, 3,15, 16, 16),box(1, 1, 2,15, 16, 3)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(-0.5, 4, -10,1, 20, -9),box(1, 1, -12,3, 20, 2),box(1, 20, 1,15, 21, 2),box(1, 0, 2,15, 32, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE_FREEZER_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(-0.5, 0, -10,1, 20, -9),box(1, 0, -12,3, 20, 2),box(1, 0, 2,15, 32, 16),box(1, 20, 1,15, 21, 2)));}};


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING).getOpposite();
        boolean open = state.getValue(OPEN);
        boolean bottom = state.is(world.getBlockState(pos.above()).getBlock());
        boolean top = state.is(world.getBlockState(pos.below()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.above()).getBlock() instanceof FreezerBlock && !(world.getBlockState(pos.above()).getBlock() instanceof IronFridgeBlock);

        if (top && hasFreezer) {
            if (open) {
                if (!FRIDGE_MIDDLE_FREEZER_OPEN.containsKey(dir))
                    FRIDGE_MIDDLE_FREEZER_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_MIDDLE_FREEZER_OPEN.get(Direction.NORTH)));
                return FRIDGE_MIDDLE_FREEZER_OPEN.get(dir);
            }
                if (!FRIDGE_MIDDLE_FREEZER.containsKey(dir))
                    FRIDGE_MIDDLE_FREEZER.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_MIDDLE_FREEZER.get(Direction.NORTH)));
                return FRIDGE_MIDDLE_FREEZER.get(dir);
        }
        else if (top && bottom) {
            if (open) {
                if (!FRIDGE_MIDDLE_OPEN.containsKey(dir))
                    FRIDGE_MIDDLE_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_MIDDLE_OPEN.get(Direction.NORTH)));
                return FRIDGE_MIDDLE_OPEN.get(dir);
            }
                if (!FRIDGE_MIDDLE.containsKey(dir))
                    FRIDGE_MIDDLE.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_MIDDLE.get(Direction.NORTH)));
                return FRIDGE_MIDDLE.get(dir);
        }
        else if (bottom) {
            if (open) {
                if (!FRIDGE_BOTTOM_OPEN.containsKey(dir))
                    FRIDGE_BOTTOM_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_BOTTOM_OPEN.get(Direction.NORTH)));
                return FRIDGE_BOTTOM_OPEN.get(dir);
            }
                if (!FRIDGE_BOTTOM.containsKey(dir))
                    FRIDGE_BOTTOM.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_BOTTOM.get(Direction.NORTH)));
                return FRIDGE_BOTTOM.get(dir);
        }
        else if (top) {
            if (open) {
                if (!FRIDGE_TOP_OPEN.containsKey(dir))
                    FRIDGE_TOP_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_TOP_OPEN.get(Direction.NORTH)));
                return FRIDGE_TOP_OPEN.get(dir);
            }
                if (!FRIDGE_TOP.containsKey(dir))
                    FRIDGE_TOP.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_TOP.get(Direction.NORTH)));
                return FRIDGE_TOP.get(dir);
        }
        else if (hasFreezer) {
            if (open) {
                if (!FRIDGE_OPEN.containsKey(dir))
                    FRIDGE_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_OPEN.get(Direction.NORTH)));
                return FRIDGE_OPEN.get(dir);
            }
                if (!FRIDGE.containsKey(dir))
                    FRIDGE.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE.get(Direction.NORTH)));
                return FRIDGE.get(dir);
        }
        else {
            if (open) {
                if (!FRIDGE_SINGLE_OPEN.containsKey(dir))
                    FRIDGE_SINGLE_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_SINGLE_OPEN.get(Direction.NORTH)));
                return FRIDGE_SINGLE_OPEN.get(dir);
            }
                if (!FRIDGE_SINGLE.containsKey(dir))
                    FRIDGE_SINGLE.put(dir, rotateShape(Direction.NORTH, dir, FRIDGE_SINGLE.get(Direction.NORTH)));
                return FRIDGE_SINGLE.get(dir);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return FridgeBlockEntity.getFactory().create(pos, state);
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
