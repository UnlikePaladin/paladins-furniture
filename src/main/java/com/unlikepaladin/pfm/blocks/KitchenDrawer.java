package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.DrawerBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;


public class KitchenDrawer extends KitchenCounter implements BlockEntityProvider{
    private float height = 0.36f;
    private final Block baseBlock;
    public static final BooleanProperty OPEN = Properties.OPEN;

    private final BlockState baseBlockState;
    public KitchenDrawer(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
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
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
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
        return blockState.with(OPEN, false);
    }



    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        return direction.getAxis().isHorizontal() ? state.with(OPEN, false) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DrawerBlockEntity) {
            player.openHandledScreen((DrawerBlockEntity)blockEntity);
            //player.incrementStat(Stats.OPE);
            PiglinBrain.onGuardedBlockInteracted(player, true);
        }
        return ActionResult.CONSUME;
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


    protected static final VoxelShape DRAWER = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 1, 12), createCuboidShape(0, 1, 0,16, 14, 13),createCuboidShape(0, 14, 0,16, 16, 16), createCuboidShape(1, 8, 12,15, 13, 14), createCuboidShape(1, 2, 12, 15, 7, 14), createCuboidShape(6, 4, 14, 10, 5, 15), createCuboidShape(6, 10, 14, 10, 11, 15));
    protected static final VoxelShape DRAWER_OPEN = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 1, 12), createCuboidShape(0, 1, 0,16, 8, 13), createCuboidShape(0, 8, 0, 1, 14, 13), createCuboidShape(15, 8, 0, 16, 14, 13), createCuboidShape(1, 8, 0, 15, 14, 4),createCuboidShape(1, 13, 12, 15, 14, 13), createCuboidShape(1, 9, 3, 2, 13, 18), createCuboidShape(1, 8, 3, 15, 9, 18), createCuboidShape(14, 9, 3, 15, 13, 18), createCuboidShape(6, 10, 19, 10, 11, 20), createCuboidShape(1, 8, 18, 15, 13, 19), createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(1, 2, 12, 15, 7, 14), createCuboidShape(6, 4, 14,10, 5, 15));

    @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        Boolean open = state.get(OPEN);
        switch(dir) {
            case NORTH:
                if(open)
                    return DRAWER_OPEN;
                else
                    return DRAWER;
            case SOUTH:
                if(open)
                    return rotateShape(Direction.NORTH, Direction.SOUTH, DRAWER_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.SOUTH, DRAWER);
            case EAST:
                if(open)
                    return rotateShape(Direction.NORTH, Direction.EAST, DRAWER_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.EAST, DRAWER);
            case WEST:
            default:
                if(open)
                    return rotateShape(Direction.NORTH, Direction.WEST, DRAWER_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.WEST, DRAWER);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof DrawerBlockEntity) {
            ((DrawerBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DrawerBlockEntity(pos,state);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

}

