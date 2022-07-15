package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class ClassicNightstand extends HorizontalFacingBlockWEntity{
    public static BooleanProperty OPEN = Properties.OPEN;
    private static final List<FurnitureBlock> WOOD_NIGHTSTAND = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_NIGHTSTAND = new ArrayList<>();

    public ClassicNightstand(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ClassicNightstand.class)){
            WOOD_NIGHTSTAND.add(new FurnitureBlock(this, "classic_nightstand"));
        }
        else if (this.getClass().isAssignableFrom(ClassicNightstand.class)){
            STONE_NIGHTSTAND.add(new FurnitureBlock(this, "classic_nightstand"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodClassicNightstands() {
        return WOOD_NIGHTSTAND.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicNightstands() {
        return STONE_NIGHTSTAND.stream();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(OPEN);
        super.appendProperties(stateManager);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GenericStorageBlockEntity) {
            player.openHandledScreen((GenericStorageBlockEntity)blockEntity);
            player.incrementStat(StatisticsRegistry.CABINET_SEARCHED);
            PiglinBrain.onGuardedBlockInteracted(player, true);
        }
        return ActionResult.CONSUME;
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
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

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GenericStorageBlockEntity(pos, state);
    }

    static final VoxelShape NIGHT_STAND = VoxelShapes.union(createCuboidShape(0, 14, 0,16, 16, 16),createCuboidShape(3, 1, 1,13, 3, 2),createCuboidShape(13, 1, 1,15, 14, 2),createCuboidShape(1, 1, 2,15, 14, 15),createCuboidShape(0.5, 0, 0,3.5, 1, 16),createCuboidShape(12.5, 0, 0,15.5, 1, 16),createCuboidShape(4, 9, 1,12, 13, 2),createCuboidShape(4, 4, 1,12, 8, 2),createCuboidShape(6.5, 5.5, 0,9.5, 6.5, 1),createCuboidShape(6.5, 10.5, 0,9.5, 11.5, 1));
    static final VoxelShape NIGHT_STAND_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND);
    static final VoxelShape NIGHT_STAND_EAST = rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND);
    static final VoxelShape NIGHT_STAND_WEST = rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND);
    static final VoxelShape NIGHT_STAND_OPEN = VoxelShapes.union(createCuboidShape(0, 14, 0,16, 16, 16),createCuboidShape(3, 1, 1,13, 3, 2),createCuboidShape(1, 1, 1,3, 14, 2),createCuboidShape(13, 1, 1,15, 14, 2),createCuboidShape(1, 1, 2,15, 14, 15),createCuboidShape(0.5, 0, 0,3.5, 1, 16),createCuboidShape(12.5, 0, 0,15.5, 1, 16),createCuboidShape(4, 9, 1,12, 13, 2),createCuboidShape(4, 4, -6,12, 8, 2),createCuboidShape(6.5, 10.5, 0,9.5, 11.5, 1),createCuboidShape(6.5, 5.5, -7,9.5, 6.5, -6));
    static final VoxelShape NIGHT_STAND_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND_OPEN);
    static final VoxelShape NIGHT_STAND_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND_OPEN);
    static final VoxelShape NIGHT_STAND_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND_OPEN);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        boolean open = state.get(OPEN);
        switch (dir)
            {
                case NORTH -> {
                    if (open) {
                        return NIGHT_STAND_OPEN;
                    }
                    return NIGHT_STAND;
                }
                case SOUTH -> {
                    if (open) {
                        return NIGHT_STAND_OPEN_SOUTH;
                    }
                    return NIGHT_STAND_SOUTH;
                }
                case EAST -> {
                    if (open) {
                        return NIGHT_STAND_OPEN_EAST;
                    }
                    return NIGHT_STAND_EAST;
                }
                default -> {
                    if (open) {
                        return NIGHT_STAND_OPEN_WEST;
                    }
                    return NIGHT_STAND_WEST;
                }
            }
        }
    }