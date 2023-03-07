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
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class FreezerBlock extends HorizontalFacingBlockWithEntity {
    public static final BooleanProperty OPEN = Properties.OPEN;
    private final Block baseBlock;
    private final BlockState baseBlockState;
    private Supplier<Block> fridge;
    private static final List<FurnitureBlock> FREEZERS = new ArrayList<>();

    public FreezerBlock(Settings settings, Supplier<Block> fridge) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(OPEN, false));
        this.baseBlockState = this.getDefaultState();
        this.fridge = fridge;
        this.baseBlock = baseBlockState.getBlock();
        FREEZERS.add(new FurnitureBlock(this, "freezer_"));
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
        return this.fridge.get().getPickStack(world, pos, state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!(direction.getAxis() == Direction.Axis.Y == (direction == Direction.UP) || neighborState.getBlock() instanceof FridgeBlock)) {
            return Blocks.AIR.getDefaultState();
        }
        if (direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);

    }

    protected void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockPos;
        BlockState blockState = world.getBlockState(blockPos = pos.down());
        if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof FridgeBlock) {
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

    protected static final VoxelShape FREEZER = VoxelShapes.union(createCuboidShape(0.5, -16, 3,15.5, 16, 16),createCuboidShape(0.5, 5, 2,14.83, 16, 3.1),createCuboidShape(13, 5.19, 0.09,14, 15.19, 1.09),createCuboidShape(13, 5.19, 0.98,14, 6.19, 2.98),createCuboidShape(13, 14.19, 1.06,14, 15.19, 3.06));
    protected static final VoxelShape FREEZER_OPEN = VoxelShapes.union(createCuboidShape(0.5, -16, 2.8,15.5, 16, 16),createCuboidShape(0.5, 5, -11.29,1.5, 16, 3.05),createCuboidShape(-1.41, 5.19, -10.45,-0.41, 15.19, -9.45),createCuboidShape(-0.52, 5.19, -10.45,1.48, 6.19, -9.45),createCuboidShape(-0.44, 14.19, -10.45,1.26, 15.19, -9.45));

    protected static final VoxelShape FREEZER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, FREEZER);
    protected static final VoxelShape FREEZER_SOUTH_OPEN = rotateShape(Direction.NORTH, Direction.SOUTH, FREEZER_OPEN);
    protected static final VoxelShape FREEZER_EAST = rotateShape(Direction.NORTH, Direction.EAST, FREEZER);
    protected static final VoxelShape FREEZER_EAST_OPEN = rotateShape(Direction.NORTH, Direction.EAST, FREEZER_OPEN);
    protected static final VoxelShape FREEZER_WEST = rotateShape(Direction.NORTH, Direction.WEST, FREEZER);
    protected static final VoxelShape FREEZER_WEST_OPEN = rotateShape(Direction.NORTH, Direction.WEST, FREEZER_OPEN);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        Boolean open = state.get(OPEN);
        switch (dir) {
            case NORTH:
                if (open)
                    return FREEZER_SOUTH_OPEN;
                else
                    return FREEZER_SOUTH;
            case SOUTH:
                if (open)
                    return FREEZER_OPEN;
                else
                    return FREEZER;
            case EAST:
                if (open)
                    return FREEZER_WEST_OPEN;
                else
                    return FREEZER_WEST;
            case WEST:
            default:
                if (open)
                    return FREEZER_EAST_OPEN;
                else
                    return FREEZER_EAST;
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
        return world.getBlockState(pos.down()).getBlock() instanceof FridgeBlock;
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
       BlockPos blockPos;
       BlockState blockState = world.getBlockState(blockPos = pos.down());
       if (blockState.isOf(state.getBlock()) || blockState.getBlock() instanceof FridgeBlock) {
           ItemScatterer.spawn((World) world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.fridge.get().asItem()));
           BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
           world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
       }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof IronFreezerBlock) {
            return new FreezerBlockEntity(BlockEntities.IRON_FREEZER_BLOCK_ENTITY, pos,state);
        }
        return new FreezerBlockEntity(BlockEntities.FREEZER_BLOCK_ENTITY, pos,state);
    }
    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (state.getBlock() instanceof IronFreezerBlock) {
            return checkType(world, type, BlockEntities.IRON_FREEZER_BLOCK_ENTITY);
        }
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
