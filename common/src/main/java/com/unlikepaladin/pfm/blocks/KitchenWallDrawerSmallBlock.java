package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity3x3;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class KitchenWallDrawerSmallBlock extends KitchenWallDrawerBlock {
    private static final List<FurnitureBlock> WOOD_SMALL_WALL_DRAWERS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_SMALL_WALL_DRAWERS = new ArrayList<>();
    public KitchenWallDrawerSmallBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(KitchenWallDrawerSmallBlock.class)){
            WOOD_SMALL_WALL_DRAWERS.add(new FurnitureBlock(this, "kitchen_wall_small_drawer"));
        }
        else if (this.getClass().isAssignableFrom(KitchenWallDrawerSmallBlock.class)){
            STONE_SMALL_WALL_DRAWERS.add(new FurnitureBlock(this, "kitchen_wall_small_drawer"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodWallSmallDrawers() {
        return WOOD_SMALL_WALL_DRAWERS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneWallSmallDrawers() {
        return STONE_SMALL_WALL_DRAWERS.stream();
    }

    protected static final VoxelShape SMALL_DRAWER = Shapes.or(box(0,6,0,16, 16, 13),box(9, 9, 14,10, 12, 15),box(6, 9, 14,7, 12, 15),box(1, 7, 13,15, 15, 14));
    protected static final VoxelShape SMALL_DRAWER_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, SMALL_DRAWER);
    protected static final VoxelShape SMALL_DRAWER_EAST = rotateShape(Direction.NORTH, Direction.EAST, SMALL_DRAWER);
    protected static final VoxelShape SMALL_DRAWER_WEST = rotateShape(Direction.NORTH, Direction.WEST, SMALL_DRAWER);

    protected static final VoxelShape SMALL_DRAWER_OPEN = Shapes.or(box(0,6,0,16, 16, 13),box(0, 7, 13,1, 15, 20),box(-1, 9, 18,0, 12, 19),box(15, 7, 13,16, 15, 20),box(16, 9, 18,17, 12, 19));
    protected static final VoxelShape SMALL_DRAWER_OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, SMALL_DRAWER_OPEN);
    protected static final VoxelShape SMALL_DRAWER_OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, SMALL_DRAWER_OPEN);
    protected static final VoxelShape SMALL_DRAWER_OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, SMALL_DRAWER_OPEN);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        boolean open = state.getValue(OPEN);
        Direction direction = state.getValue(FACING);
        if (open) {
            switch (direction) {
                case NORTH: return SMALL_DRAWER_OPEN;
                case SOUTH: return SMALL_DRAWER_OPEN_SOUTH;
                case WEST: return SMALL_DRAWER_OPEN_WEST;
                default: return SMALL_DRAWER_OPEN_EAST;
            }
        }
            switch (direction) {
                case NORTH: return SMALL_DRAWER;
                case SOUTH: return SMALL_DRAWER_SOUTH;
                case WEST: return SMALL_DRAWER_WEST;
                default: return SMALL_DRAWER_EAST;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING);
        stateManager.add(OPEN);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world) {
        return GenericStorageBlockEntity3x3.getFactory().get();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GenericStorageBlockEntity3x3) {
            player.openMenu((GenericStorageBlockEntity3x3)blockEntity);
            player.awardStat(Statistics.DRAWER_SEARCHED);
            PiglinAi.angerNearbyPiglins(player, true);
        }
        return InteractionResult.CONSUME;
    }

}
