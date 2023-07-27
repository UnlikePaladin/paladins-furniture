package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
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

public class FridgeBlock extends HorizontalFacingBlockWithEntity {
    public static final BooleanProperty OPEN = Properties.OPEN;
    private final Block baseBlock;
    private final BlockState baseBlockState;
    private static final List<FurnitureBlock> FRIDGES = new ArrayList<>();
    private final Supplier<FreezerBlock> freezer;
    public FridgeBlock(Settings settings, Supplier<FreezerBlock> freezer) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(OPEN, false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
        FRIDGES.add(new FurnitureBlock(this, "fridge"));
        this.freezer = freezer;
    }

    public static Stream<FurnitureBlock> streamFridges() {
        return FRIDGES.stream();
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
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof FridgeBlockEntity) {
            ((FridgeBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
        super.onPlaced(world, pos, state, placer, itemStack);
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
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction.getAxis().isVertical() && neighborState.getBlock() == this ? neighborState.get(FACING) == state.get(FACING) ? state.with(OPEN, neighborState.get(OPEN)) : state : state;
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FridgeBlockEntity) {
            player.openHandledScreen((FridgeBlockEntity)blockEntity);
            player.incrementStat(Statistics.FRIDGE_OPENED);
            PiglinBrain.onGuardedBlockInteracted(player, true);
        }
        return ActionResult.CONSUME;
    }

    public void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            this.onBreakInCreative(world, pos, state, player);
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (stateFrom.isOf(this)) {
            return true;
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    protected static final Map<Direction, VoxelShape> FRIDGE_SINGLE = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.51, 0.131, 2.609,14.49, 1.1, 3.609), createCuboidShape(0.5, 1, 3,15.5, 16, 16), createCuboidShape(0.5, 0, 2.844,15.5, 1, 15.844), createCuboidShape(12.979, 4, 0.027,13.979, 15, 1.027), createCuboidShape(12.979, 4, 0.918,13.979, 5, 2.918), createCuboidShape(12.979, 14, 0.918,13.979, 15, 2.918), createCuboidShape(0.479, 1, 1.933,14.174, 16, 3.033), createCuboidShape(13.814, 1, 2.081,14.814, 16, 3.481)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_TOP = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.5, 0, 3, 15.5, 16, 16), createCuboidShape(0.5, 0, 2, 14.2, 16, 3), createCuboidShape(13.84, -0.05, 2.15,14.84, 16, 3.55), createCuboidShape(13, -0.01, 0.09, 14, 15.19, 1.09), createCuboidShape(13, 14.19, 1.06, 14, 15.19, 3.06)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.5, 0, 3, 15.5, 16.2, 16), createCuboidShape(12.979, 0, 0.027,13.979, 16.2, 1.027), createCuboidShape(0.479, 0, 1.933,14.174, 16.2, 3.033), createCuboidShape(13.81385, 0, 2.08103,14.81385, 16.2, 3.48103)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_BOTTOM = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.51, 0.13, 2.61,14.49, 1.1, 3.61), createCuboidShape(0.48563, 1, 3,15.5, 16, 16), createCuboidShape(0.5, 0, 2.84,15.5, 1, 15.84), createCuboidShape(12.98, 4, 0.03,13.98, 16, 1.03), createCuboidShape(12.98, 4, 0.92,13.98, 5, 2.92), createCuboidShape(0.48, 1, 1.93,14.17, 16, 3.03), createCuboidShape(13.81, 1, 2.08,14.81, 16, 3.48)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.5, 0, 3, 15.5, 32, 16), createCuboidShape(12.98, 4, 0.03,13.98, 20, 1.03),createCuboidShape(12.98, 4, 0.92,13.98, 5, 2.92),createCuboidShape(12.98, 18.98, 1,13.98, 19.98, 2.9),createCuboidShape(0.5, 1, 1.93,14.78, 19.98, 3.03)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE_FREEZER = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.5, 0, 3,15.5, 16, 16),createCuboidShape(12.979, 0, 0.027,13.979, 16, 1.027),createCuboidShape(12.979, 15.984, 0.027,13.979, 19.984, 1.027),createCuboidShape(12.979, 18.984, 0.996,13.979, 19.984, 2.896),createCuboidShape(0.479, 0, 1.933,14.174, 20, 3.033),createCuboidShape(13.814, 0, 2.081,14.814, 20, 3.481),createCuboidShape(0.487, 0, 2.62,15.123, 21, 4.013)));}};

    protected static final Map<Direction, VoxelShape> FRIDGE_SINGLE_OPEN = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.5, 1, 3,15.5, 16, 16), createCuboidShape(0.5, 0, 2.844,15.5, 1, 15.844),createCuboidShape(-1.407, 4, -10.505,-0.407, 15, -9.505),createCuboidShape(-0.516, 4, -10.505,1.484, 5, -9.505),createCuboidShape(-0.516, 14, -10.505,1.484, 15, -9.505),createCuboidShape(0.5, 1, -10.7,1.6, 16, 3.019),createCuboidShape(0.647, 1, -11.34,2.047, 16, -10.34),createCuboidShape(1.688, 0.3, 2.612,15.116, 15.82, 4.013),createCuboidShape(1.4, 9.5, -10.7,4.4, 12.5, 2.3),createCuboidShape(1.4, 2, -10.7,4.4, 5, 2.3)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_TOP_OPEN = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.425, 0, 3,15.495, 16, 16), createCuboidShape(1.4, 9.5, -10.7,4.4, 12.5, 2.3),createCuboidShape(1.4, 2, -10.7,4.4, 5, 2.3),createCuboidShape(0.426, 0, -10.633,1.426, 16, 3.067),createCuboidShape(0.773, 0, -11.476,1.773, 16, -10.076),createCuboidShape(-1.481, 0.188, -10.433,-0.481, 15.188, -9.433),createCuboidShape(-0.512, 14.188, -10.433,1.288, 15.188, -9.433),createCuboidShape(0.51, -0.037, 2.609,15.19, 15.931, 3.609)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE_OPEN = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.484, 0, 3,15.5, 16, 16),createCuboidShape(-1.43, 0, -10.505,-0.43, 16, -9.505),createCuboidShape(0.476, 0, -10.7,1.576, 16, 3.058),createCuboidShape(0.624, 0, -11.34,2.024, 16, -10.34),createCuboidShape(1.688, 0, 2.612,15.116, 16, 4.013),createCuboidShape(1.4, 9.5, -9.7,4.4, 12.5, 3.3),createCuboidShape(1.4, 2, -9.7,4.4, 5, 3.3)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_BOTTOM_OPEN = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.51, 0.131, 2.609,15.19, 16.1, 3.609),createCuboidShape(0.5, 1, 3,15.5, 16, 16),createCuboidShape(0.5, 0, 2.844,15.5, 1, 15.844),createCuboidShape(-1.469, 4, -10.405,-0.469, 16, -9.405),createCuboidShape(-0.579, 4, -10.405,1.421, 5, -9.405),createCuboidShape(0.437, 1, -10.6,1.537, 16, 3.095),createCuboidShape(0.585, 1, -11.24,1.985, 16, -10.24),createCuboidShape(1.4, 9.5, -9.7,4.4, 12.5, 3.3),createCuboidShape(1.4, 2, -9.7,4.4, 5, 3.3)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_OPEN = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(0.5, 0, 3,15.5, 32, 16),createCuboidShape(-1.41, 4, -10.39,-0.41, 19.98, -9.39),createCuboidShape(-0.52, 4, -10.39,1.48, 5, -9.39),createCuboidShape(-0.45, 18.98, -10.39,1.45, 19.98, -9.39),createCuboidShape(0.5, 1, -11.59,1.48, 20, 3.11),createCuboidShape(0.75, 7.7, -10.42,3.75, 10.8, 2.98),createCuboidShape(0.75, 12.2, -10.42,3.75, 15.3, 2.98)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE_FREEZER_OPEN = new HashMap<>() {{put(Direction.NORTH, VoxelShapes.union(createCuboidShape(-1.415, 0, -10.391,-0.415, 20, -9.391),createCuboidShape(-0.446, 18.984, -10.391,1.454, 19.984, -9.391),createCuboidShape(0.491, 0, -10.591,1.491, 20, 3.109),createCuboidShape(0.634, 0, -11.221,2.034, 20, -10.221),createCuboidShape(1.4, 9, -10.7,4.4, 12, 2.3),createCuboidShape(1.4, 2, -10.7,4.4, 5, 2.3),createCuboidShape(1.4, 15.5, -10.7,4.4, 18.5, 2.3),createCuboidShape(0.487, 0, 2.62,15.123, 21, 4.013),createCuboidShape(0.5, 0, 3,15.5, 16, 16)));}};


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING).getOpposite();
        boolean open = state.get(OPEN);
        boolean bottom = state.isOf(world.getBlockState(pos.up()).getBlock());
        boolean top = state.isOf(world.getBlockState(pos.down()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.up()).getBlock() instanceof FreezerBlock && !(world.getBlockState(pos.up()).getBlock() instanceof IronFridgeBlock);

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
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return FridgeBlockEntity.getFactory().create(pos, state);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
