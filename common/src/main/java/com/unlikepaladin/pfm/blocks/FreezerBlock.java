package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class FreezerBlock extends HorizontalFacingBlockWithEntity {
    public static final BooleanProperty OPEN = Properties.OPEN;
    private final Block baseBlock;
    private final BlockState baseBlockState;
    private Supplier<FridgeBlock> fridge;
    private static final List<FurnitureBlock> FREEZERS = new ArrayList<>();

    public FreezerBlock(Settings settings, Supplier<FridgeBlock> fridge) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(OPEN, false));
        this.baseBlockState = this.getDefaultState();
        this.fridge = fridge;
        this.baseBlock = baseBlockState.getBlock();
        FREEZERS.add(new FurnitureBlock(this, "freezer"));
    }

    public static Stream<FurnitureBlock> streamFreezers() {
        return FREEZERS.stream();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
         if (!world.isClient) {
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    player.incrementStat(Statistics.FREEZER_OPENED);
                    player.openHandledScreen(screenHandlerFactory);
                    PiglinBrain.onGuardedBlockInteracted(player, true);
                }
            }
            return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return super.getPickStack(world, pos, state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);

    }

    protected void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    protected static final Map<Direction, VoxelShape> FREEZER = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.5, -16, 3,15.5, 16, 16),createCuboidShape(0.5, 5, 2,14.83, 16, 3.1),createCuboidShape(13, 5.19, 0.09,14, 15.19, 1.09),createCuboidShape(13, 5.19, 0.98,14, 6.19, 2.98),createCuboidShape(13, 14.19, 1.06,14, 15.19, 3.06)));}};
    protected static final Map<Direction, VoxelShape> FREEZER_SINGLE = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.51, 0.131, 2.609,15.19, 1.1, 3.609),createCuboidShape(0.5, 1, 3,15.5, 16, 16),createCuboidShape(0.5, 0, 2.844,15.5, 1, 15.844),createCuboidShape(12.979, 3, 0.027,13.979, 14, 1.027),createCuboidShape(12.979, 3, 0.918,13.979, 4, 2.918),createCuboidShape(12.979, 13, 0.918,13.979, 14, 2.918),createCuboidShape(0.479, 1, 1.933,14.174, 16, 3.033),createCuboidShape(13.814, 1, 2.081,14.814, 16, 3.481)));}};
    protected static final Map<Direction, VoxelShape> FREEZER_OPEN = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.5, -16, 2.8,15.5, 16, 16),createCuboidShape(0.5, 5, -11.29,1.5, 16, 3.05),createCuboidShape(-1.41, 5.19, -10.45,-0.41, 15.19, -9.45),createCuboidShape(-0.52, 5.19, -10.45,1.48, 6.19, -9.45),createCuboidShape(-0.44, 14.19, -10.45,1.26, 15.19, -9.45)));}};
    protected static final Map<Direction, VoxelShape> FREEZER_SINGLE_OPEN = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.5, 1, 3,15.5, 16, 16),createCuboidShape(0.5, 0, 2.844,15.5, 1, 15.844),createCuboidShape(-1.407, 3, -10.505,-0.407, 14, -9.505),createCuboidShape(-0.516, 3, -10.505,1.484, 4, -9.505),createCuboidShape(-0.516, 13, -10.505,1.484, 14, -9.505),createCuboidShape(0.5, 1, -10.7,1.6, 16, 2.995),createCuboidShape(0.647, 1, -11.34,2.047, 16, -10.34),createCuboidShape(0.688, 0.3, 2.612,15.116, 15.82, 4.013),createCuboidShape(1.4, 9.5, -10.7,4.4, 12.5, 2.3),createCuboidShape(1.4, 2, -10.7,4.4, 5, 2.3)));}};

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING).getOpposite();
        Boolean open = state.get(OPEN);
        boolean hasFridge = world.getBlockState(pos.down()).getBlock() instanceof FridgeBlock && !(world.getBlockState(pos.down()).getBlock() instanceof IronFridgeBlock);
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
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            this.onBreakInCreative(world, pos,state, player);
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Inventory) {
            ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

   @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
       super.onBroken(world, pos, state);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return FreezerBlockEntity.getFactory().create(pos,state);
    }
    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, BlockEntities.FREEZER_BLOCK_ENTITY);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends FreezerBlockEntity> expectedType) {
        return world.isClient ? null : FreezerBlock.checkType(givenType, expectedType, FreezerBlockEntity::tick);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
