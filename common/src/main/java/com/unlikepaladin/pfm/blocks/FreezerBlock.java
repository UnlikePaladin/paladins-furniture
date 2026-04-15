package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class FreezerBlock extends HorizontalFacingBlockWithEntity {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    private Supplier<FridgeBlock> fridge;
    private static final List<FurnitureBlock> FREEZERS = new ArrayList<>();
  //  public static final MapCodec<FreezerBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createSettingsCodec(), ).apply(instance, FreezerBlock::new));
    public FreezerBlock(Properties settings, Supplier<FridgeBlock> fridge) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(OPEN, false));
        this.fridge = fridge;
        FREEZERS.add(new FurnitureBlock(this, "freezer"));
    }

    public static Stream<FurnitureBlock> streamFreezers() {
        return FREEZERS.stream();
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
         if (!world.isClientSide) {
                MenuProvider screenHandlerFactory = state.getMenuProvider(world, pos);
                if (screenHandlerFactory != null) {
                    player.awardStat(Statistics.FREEZER_OPENED);
                    player.openMenu(screenHandlerFactory);
                    PiglinAi.angerNearbyPiglins((ServerLevel) world, player, true);
                }
            }
            return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
    }

    // Todo: Figure out how i can implement this properly, supplier to a block is kinda weird, it is ok to return null in 1.20.4 for now
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        return super.getCloneItemStack(world, pos, state, includeData);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);

    }

    protected void onBreakInCreative(Level world, BlockPos pos, BlockState state, Player player) {
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    protected static final Map<Direction, VoxelShape> FREEZER = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(12, 5, -0.5,13, 15, 1),box(1, 5, 1,15, 16, 2),box(1, -16, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FREEZER_SINGLE = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(12, 4, -0.5,13, 15, 1),box(1, 1, 1,15, 16, 2),box(1, 0, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FREEZER_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(-0.5, 5, -10,1, 15, -9),box(1, 5, -12,3, 16, 2),box(1, 5, 2,15, 16, 16),box(1, -16, 1,15, 5, 16)));}};
    protected static final Map<Direction, VoxelShape> FREEZER_SINGLE_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(-0.5, 4, -10,1, 15, -9),box(1, 1, -12,3, 16, 2),box(1, 0, 2,15, 16, 16)));}};

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING).getOpposite();
        Boolean open = state.getValue(OPEN);
        boolean hasFridge = world.getBlockState(pos.below()).getBlock() instanceof FridgeBlock && !(world.getBlockState(pos.below()).getBlock() instanceof IronFridgeBlock);
        if (hasFridge) {
            if (open) {
                if (!FREEZER_OPEN.containsKey(dir))
                    FREEZER_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FREEZER_OPEN.get(Direction.NORTH)));
                return FREEZER_OPEN.get(dir);
            }
            if (!FREEZER.containsKey(dir))
                FREEZER.put(dir, rotateShape(Direction.NORTH, dir, FREEZER.get(Direction.NORTH)));
            return FREEZER.get(dir);
        } else {
            if (open) {
                if (!FREEZER_SINGLE_OPEN.containsKey(dir))
                    FREEZER_SINGLE_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FREEZER_SINGLE_OPEN.get(Direction.NORTH)));
                return FREEZER_SINGLE_OPEN.get(dir);
            }
            if (!FREEZER_SINGLE.containsKey(dir))
                FREEZER_SINGLE.put(dir, rotateShape(Direction.NORTH, dir, FREEZER_SINGLE.get(Direction.NORTH)));
            return FREEZER_SINGLE.get(dir);
        }
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && player.isCreative()) {
            this.onBreakInCreative(world, pos,state, player);
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return super.canSurvive(state, world, pos);
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
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
       super.destroy(world, pos, state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return FreezerBlockEntity.getFactory().create(pos,state);
    }
    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(world, type, BlockEntities.FREEZER_BLOCK_ENTITY);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTickerHelper(Level world, BlockEntityType<T> givenType, BlockEntityType<? extends FreezerBlockEntity> expectedType) {
        return world.isClientSide ? null : createTickerHelper(givenType, expectedType, FreezerBlockEntity::serverTick);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
