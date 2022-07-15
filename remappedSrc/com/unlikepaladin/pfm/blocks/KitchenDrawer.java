package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.DrawerBlockEntity;
import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class KitchenDrawer extends KitchenCounter implements BlockEntityProvider{
    private float height = 0.36f;
    private final Block baseBlock;
    public static final BooleanProperty OPEN = Properties.OPEN;

    private final BlockState baseBlockState;
    public KitchenDrawer(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(OPEN, false).with(SHAPE, CounterShape.STRAIGHT));
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


    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
        stateManager.add(SHAPE);
    }
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }




    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DrawerBlockEntity) {
            player.openHandledScreen((DrawerBlockEntity)blockEntity);
            player.incrementStat(StatisticsRegistry.DRAWER_SEARCHED);
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


    protected static final VoxelShape STRAIGHT = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 1, 12), createCuboidShape(0, 1, 0,16, 14, 13),createCuboidShape(0, 14, 0,16, 16, 16), createCuboidShape(1, 8, 12,15, 13, 14), createCuboidShape(1, 2, 12, 15, 7, 14), createCuboidShape(6, 4, 14, 10, 5, 15), createCuboidShape(6, 10, 14, 10, 11, 15));
    protected static final VoxelShape STRAIGHT_OPEN = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 1, 12), createCuboidShape(0, 1, 0,16, 14, 13), createCuboidShape(6, 10, 19, 10, 11, 20), createCuboidShape(1, 8, 13, 15, 13, 19), createCuboidShape(0, 14, 0, 16, 16, 16),createCuboidShape(1, 2, 12, 15, 7, 14), createCuboidShape(6, 4, 14, 10, 5, 15));
    protected static final VoxelShape OUTER_CORNER_OPEN = VoxelShapes.union(createCuboidShape(0, 0, 0,12, 1, 12),createCuboidShape(0, 1, 0,13, 14, 13),createCuboidShape(0, 14, 0,16, 16, 16),createCuboidShape(5, 10, 19,8, 11, 20),createCuboidShape(1, 8, 13,12, 13, 19),createCuboidShape(1, 2, 12,12, 7, 14),createCuboidShape(5, 4, 14,8, 5, 15),createCuboidShape(12, 8, 1,14, 13, 12),createCuboidShape(12, 2, 1,14, 7, 12),createCuboidShape(14, 4, 5,15, 5, 8),createCuboidShape(14, 10, 5,15, 11, 8));
    protected static final VoxelShape OUTER_CORNER = VoxelShapes.union(createCuboidShape(0, 0, 0,12, 1, 12),createCuboidShape(0, 1, 0,13, 14, 13),createCuboidShape(0, 14, 0,16, 16, 16),createCuboidShape(5, 10, 14,8, 11, 15),createCuboidShape(1, 8, 13,12, 13, 14),createCuboidShape(1, 2, 12,12, 7, 14),createCuboidShape(5, 4, 14,8, 5, 15),createCuboidShape(12, 8, 1,14, 13, 12),createCuboidShape(12, 2, 1,14, 7, 12),createCuboidShape(14, 4, 5,15, 5, 8),createCuboidShape(14, 10, 5,15, 11, 8));
    protected static final VoxelShape INNER_CORNER = VoxelShapes.union(createCuboidShape(4, 0, 0,16, 1, 16),createCuboidShape(0, 0, 0,4, 1, 11.9),createCuboidShape(3, 1, 0,16, 14, 16),createCuboidShape(0, 1, 0,3, 14, 13),createCuboidShape(0, 14, 0,16, 16, 16),createCuboidShape(1, 2, 12,3, 7, 14),createCuboidShape(1, 8, 12,3, 13, 14),createCuboidShape(2, 8, 14,13, 13, 15));
    protected static final VoxelShape RIGHT_EDGE = VoxelShapes.union(createCuboidShape(0,0,0,14, 1, 12),createCuboidShape(0,1,0,14, 14, 13),createCuboidShape(0,14,0,16, 16, 16),createCuboidShape(14,0,0,16, 14, 16),createCuboidShape(1,8,12,13, 13, 14),createCuboidShape(1, 2, 12,13, 7, 14),createCuboidShape(6, 4, 14,9,5,15),createCuboidShape(6,10,14,9, 11, 15));
    protected static final VoxelShape LEFT_EDGE = VoxelShapes.union(createCuboidShape(2,0,0,16, 1, 12),createCuboidShape(2,1,0,16, 14, 13),createCuboidShape(0,0,0,2, 14, 16),createCuboidShape(0,14,0,16, 16, 16),createCuboidShape(3,8,12,15, 13, 14),createCuboidShape(3, 2, 12,15, 7, 14),createCuboidShape(8,4,14,11, 5, 15),createCuboidShape(8, 10, 14,11, 11, 15));
    protected static final VoxelShape RIGHT_EDGE_OPEN =  VoxelShapes.union(createCuboidShape(0, 14, 0,16, 16, 16),createCuboidShape(14, 0, 0,16, 14, 16),createCuboidShape(0, 0, 0,14, 1, 12),createCuboidShape(0, 1, 0,14, 14, 13),createCuboidShape(6, 10, 19,10, 11, 20),createCuboidShape(1, 8, 13,13, 13, 19),createCuboidShape(1, 2, 12,13, 7, 14),createCuboidShape(6, 4, 14,10, 5, 15));
    protected static final VoxelShape LEFT_EDGE_OPEN = VoxelShapes.union(createCuboidShape(0, 14, 0,16, 16, 16),createCuboidShape(0, 0, 0,2, 14, 16),createCuboidShape(2, 0, 0,16, 1, 12),createCuboidShape(2, 1, 0,16, 14, 13),createCuboidShape(8, 10, 19,12, 11, 20),createCuboidShape(3, 8, 13,15, 13, 19),createCuboidShape(3, 2, 12,15, 7, 14),createCuboidShape(8, 4, 14,12, 5, 15));

    @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        Boolean open = state.get(OPEN);
        CounterShape shape = state.get(SHAPE);

        switch(shape) {
            case STRAIGHT:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return STRAIGHT_OPEN;
                    else
                        return STRAIGHT;
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, STRAIGHT_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, STRAIGHT);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, STRAIGHT_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, STRAIGHT);
                else
                if(open)
                    return rotateShape(Direction.NORTH, Direction.WEST, STRAIGHT_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.WEST, STRAIGHT);

            case INNER_LEFT:
                if(dir.equals(Direction.NORTH))
                        return rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER);
                else if (dir.equals(Direction.SOUTH))
                        return rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER);
                else if (dir.equals(Direction.EAST))
                        return INNER_CORNER;
                else
                    return rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER);
            case INNER_RIGHT:
                if(dir.equals(Direction.NORTH))
                        return INNER_CORNER;
                else if (dir.equals(Direction.SOUTH))
                        return rotateShape(Direction.NORTH, Direction.SOUTH, INNER_CORNER);
                else if (dir.equals(Direction.EAST))
                        return rotateShape(Direction.NORTH, Direction.EAST, INNER_CORNER);
                else
                    return rotateShape(Direction.NORTH, Direction.WEST, INNER_CORNER);
            case OUTER_LEFT:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return OUTER_CORNER_OPEN;
                    else
                        return OUTER_CORNER;
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER);
                else
                if (open)
                    return rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER);
            case OUTER_RIGHT:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, OUTER_CORNER);
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.WEST, OUTER_CORNER);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, OUTER_CORNER);
                else
                if (open)
                    return OUTER_CORNER_OPEN;
                else
                    return OUTER_CORNER;
            case LEFT_EDGE:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return LEFT_EDGE_OPEN;
                    else
                        return LEFT_EDGE;
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, LEFT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, LEFT_EDGE);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, LEFT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, LEFT_EDGE);
                else
                if (open)
                    return rotateShape(Direction.NORTH, Direction.WEST, LEFT_EDGE_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.WEST, LEFT_EDGE);
            case RIGHT_EDGE:
                if(dir.equals(Direction.NORTH))
                    if (open)
                        return RIGHT_EDGE_OPEN;
                    else
                        return RIGHT_EDGE;
                else if (dir.equals(Direction.SOUTH))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.SOUTH, RIGHT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.SOUTH, RIGHT_EDGE);
                else if (dir.equals(Direction.EAST))
                    if (open)
                        return rotateShape(Direction.NORTH, Direction.EAST, RIGHT_EDGE_OPEN);
                    else
                        return rotateShape(Direction.NORTH, Direction.EAST, RIGHT_EDGE);
                else
                if (open)
                    return rotateShape(Direction.NORTH, Direction.WEST, RIGHT_EDGE_OPEN);
                else
                    return rotateShape(Direction.NORTH, Direction.WEST, RIGHT_EDGE);
            default:
                return STRAIGHT;
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
