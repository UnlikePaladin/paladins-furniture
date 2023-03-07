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
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class FridgeBlock extends HorizontalFacingBlockWithEntity {
    public static final BooleanProperty OPEN = Properties.OPEN;
    private final Block baseBlock;
    private final BlockState baseBlockState;
    private Supplier<Block> freezer;
    private static final List<FurnitureBlock> FRIDGES = new ArrayList<>();

    public FridgeBlock(Settings settings, Supplier<Block> freezer) {
        super(settings);
        this.freezer = freezer;
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(OPEN, false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
        FRIDGES.add(new FurnitureBlock(this, "fridge"));
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
        return world.getBlockState(pos.up()).isAir() || world.getBlockState(pos.up()).getBlock() == this.freezer;
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), this.freezer.get().getDefaultState().with(FACING, placer.getHorizontalFacing()), Block.NOTIFY_ALL);
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
        if (!(direction.getAxis() != Direction.Axis.Y != (direction == Direction.UP) || neighborState.getBlock() == this.freezer.get())) {
            return Blocks.AIR.getDefaultState();
        }
        if (direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);

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
        BlockPos blockPos;
        BlockState blockState = world.getBlockState(blockPos = pos.up());
        if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof FreezerBlock) {
            BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
        }
        blockPos = pos.down();
        blockState = world.getBlockState(blockPos);
        if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof FreezerBlock) {
            BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
        }
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        }
        return null;
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

    protected static final VoxelShape FRIDGE = VoxelShapes.union(createCuboidShape(0.5, 0, 3, 15.5, 32, 16), createCuboidShape(12.98, 4, 0.03,13.98, 20, 1.03),createCuboidShape(12.98, 4, 0.92,13.98, 5, 2.92),createCuboidShape(12.98, 18.98, 1,13.98, 19.98, 2.9),createCuboidShape(0.5, 1, 1.93,14.78, 19.98, 3.03));
    protected static final VoxelShape FRIDGE_OPEN = VoxelShapes.union(createCuboidShape(0.5, 0, 3,15.5, 32, 16),createCuboidShape(-1.41, 4, -10.39,-0.41, 19.98, -9.39),createCuboidShape(-0.52, 4, -10.39,1.48, 5, -9.39),createCuboidShape(-0.45, 18.98, -10.39,1.45, 19.98, -9.39),createCuboidShape(0.5, 1, -11.59,1.48, 20, 3.11),createCuboidShape(0.75, 7.7, -10.42,3.75, 10.8, 2.98),createCuboidShape(0.75, 12.2, -10.42,3.75, 15.3, 2.98));

    protected static final VoxelShape FRIDGE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FRIDGE);
    protected static final VoxelShape FRIDGE_SOUTH_OPEN = rotateShape(Direction.NORTH, Direction.SOUTH, FRIDGE_OPEN);
    protected static final VoxelShape FRIDGE_EAST = rotateShape(Direction.NORTH, Direction.EAST, FRIDGE);
    protected static final VoxelShape FRIDGE_EAST_OPEN = rotateShape(Direction.NORTH, Direction.EAST, FRIDGE_OPEN);
    protected static final VoxelShape FRIDGE_WEST = rotateShape(Direction.NORTH, Direction.WEST, FRIDGE);
    protected static final VoxelShape FRIDGE_WEST_OPEN = rotateShape(Direction.NORTH, Direction.WEST, FRIDGE_OPEN);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        Boolean open = state.get(OPEN);
        switch (dir) {
            case NORTH:
                if (open)
                    return FRIDGE_SOUTH_OPEN;
                else
                    return FRIDGE_SOUTH;
            case SOUTH:
                if (open)
                    return FRIDGE_OPEN;
                else
                    return FRIDGE;
            case EAST:
                if (open)
                    return FRIDGE_WEST_OPEN;
                else
                    return FRIDGE_WEST;
            default:
                if (open)
                    return FRIDGE_EAST_OPEN;
                else
                    return FRIDGE_EAST;
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FridgeBlockEntity(pos, state);
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
