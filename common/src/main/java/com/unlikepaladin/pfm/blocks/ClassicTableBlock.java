package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ClassicTableBlock extends Block {
    private final Block baseBlock;

    private static final List<FurnitureBlock> WOOD_CLASSIC_TABLES = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_TABLES = new ArrayList<>();
    private final BlockState baseBlockState;
    public ClassicTableBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any());
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ClassicTableBlock.class)){
            WOOD_CLASSIC_TABLES.add(new FurnitureBlock(this, "table_classic"));
        }
        else if (this.getClass().isAssignableFrom(ClassicTableBlock.class)){
            STONE_CLASSIC_TABLES.add(new FurnitureBlock(this, "table_classic"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodClassicTables() {
        return WOOD_CLASSIC_TABLES.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicTables() {
        return STONE_CLASSIC_TABLES.stream();
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(state.getBlock())) {
            this.baseBlockState.neighborChanged(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onPlace(this.baseBlockState, world, pos, oldState, false);
        }
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState();
    }

    public boolean canConnect(BlockState blockState) {
        return PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect() ? blockState.getBlock() instanceof ClassicTableBlock : blockState.getBlock() == this;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    final static VoxelShape TABLE_CLASSIC_BASE = box(0, 14, 0, 16, 16, 16);
    final static VoxelShape TABLE_CLASSIC_NORTH_EAST_LEG = box(12, 0, 2, 14, 15, 4);
    final static VoxelShape TABLE_CLASSIC_NORTH_WEST_LEG = box(2, 0, 2, 4, 15, 4);
    final static VoxelShape TABLE_CLASSIC_SOUTH_WEST_LEG = box(2, 0, 12, 4, 15, 14);
    final static VoxelShape TABLE_CLASSIC_SOUTH_EAST_LEG = box(12, 0, 12, 14, 15, 14);

    // Cursed I know
    final static Map<String, VoxelShape> VOXEL_SHAPES = new HashMap<>();
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

        Boolean north = canConnect(world.getBlockState(pos.north()));
        boolean east = canConnect(world.getBlockState(pos.east()));
        boolean west = canConnect(world.getBlockState(pos.west()));
        boolean south = canConnect(world.getBlockState(pos.south()));

        String key = north.toString()+ east + west + south;
        if (!VOXEL_SHAPES.containsKey(key)) {
            generateVoxelShape(key, north, east, west, south);
        }
        return VOXEL_SHAPES.get(key);
    }
    private static void generateVoxelShape(String key, Boolean north, Boolean east, Boolean west, Boolean south) {
        VoxelShape newVoxelShape = TABLE_CLASSIC_BASE;
        if (!north && !south && !east && !west) {
            newVoxelShape = Shapes.or(newVoxelShape, TABLE_CLASSIC_NORTH_EAST_LEG, TABLE_CLASSIC_SOUTH_WEST_LEG, TABLE_CLASSIC_SOUTH_EAST_LEG, TABLE_CLASSIC_NORTH_WEST_LEG);
        }
        if (!north && !east) {
            newVoxelShape = Shapes.or(newVoxelShape, TABLE_CLASSIC_NORTH_EAST_LEG);
        }
        if (!north && !west)  {
            newVoxelShape = Shapes.or(newVoxelShape, TABLE_CLASSIC_NORTH_WEST_LEG);
        }
        if (!south && !east)  {
            newVoxelShape = Shapes.or(newVoxelShape, TABLE_CLASSIC_SOUTH_EAST_LEG);
        }
        if (!south && !west)  {
            newVoxelShape = Shapes.or(newVoxelShape, TABLE_CLASSIC_SOUTH_WEST_LEG);
        }
        VOXEL_SHAPES.put(key, newVoxelShape);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}


