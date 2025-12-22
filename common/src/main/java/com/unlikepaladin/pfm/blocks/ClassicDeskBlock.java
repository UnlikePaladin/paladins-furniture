package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ClassicDeskBlock extends HorizontalFacingBlock {
    private final Block baseBlock;

    private static final List<FurnitureBlock> WOOD_CLASSIC_DESKS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_DESKS = new ArrayList<>();
    private final BlockState baseBlockState;
    public final FurnitureBlock deskBlock;
    public ClassicDeskBlock(Settings settings) {
        super(settings.luminance((state) -> 0).emissiveLighting((blockstate, b, c) -> false));
        setDefaultState(this.getStateManager().getDefaultState());
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
        this.deskBlock = new FurnitureBlock(this, "desk_classic");
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ClassicDeskBlock.class)){
            WOOD_CLASSIC_DESKS.add(deskBlock);
        }
        else if (this.getClass().isAssignableFrom(ClassicDeskBlock.class)){
            STONE_CLASSIC_DESKS.add(deskBlock);
        }
    }

    public static Stream<FurnitureBlock> streamWoodClassicDesks() {
        return WOOD_CLASSIC_DESKS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicDesks() {
        return STONE_CLASSIC_DESKS.stream();
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.baseBlockState.neighborUpdate(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }

    public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    public boolean canConnect(BlockState blockState) {
        if (blockState.getBlock() instanceof ClassicDeskCabinetBlock && !PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect()) {
            return (deskBlock.getBaseMaterial() == ((ClassicDeskCabinetBlock)blockState.getBlock()).deskCabinetBlock.getBaseMaterial());
        } else if (blockState.getBlock() instanceof ClassicDeskCabinetBlock){
            return true;
        } else if (blockState.getBlock() instanceof ClassicDeskBlock && !PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect()) {
            return (deskBlock.getBaseMaterial() == ((ClassicDeskBlock)blockState.getBlock()).deskBlock.getBaseMaterial());
        } else if (blockState.getBlock() instanceof ClassicDeskBlock) {
            return true;
        }
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    /** Method to rotate VoxelShapes from this random Forge Forums thread: https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/ */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, VoxelShapes.empty() };

        int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    final static VoxelShape TABLE_CLASSIC_BASE = createCuboidShape(0, 14, 0, 16, 16, 16);
    final static VoxelShape TABLE_CLASSIC_NORTH_EAST_LEG = createCuboidShape(13, 0, 1, 15, 14, 3);
    final static VoxelShape TABLE_CLASSIC_EAST_NORTH = createCuboidShape(13, 2, 0,15, 4, 3);
    final static VoxelShape TABLE_CLASSIC_NORTH_EAST = createCuboidShape(13, 2, 1,16, 4, 3);
    final static VoxelShape TABLE_CLASSIC_NORTH_WEST_LEG = createCuboidShape(1, 0, 1, 3, 14, 3);
    final static VoxelShape TABLE_CLASSIC_SOUTH_WEST_LEG = createCuboidShape(1, 0, 13, 3, 14, 15);
    final static VoxelShape TABLE_CLASSIC_SOUTH_EAST_LEG = createCuboidShape(13, 0, 13, 15, 14, 15);
    final static VoxelShape TABLE_CLASSIC_SOUTH_EAST = createCuboidShape(13, 2, 13, 15, 4, 16);
    final static VoxelShape TABLE_CLASSIC_EAST = createCuboidShape(13, 2, 3, 15, 4, 13);
    final static VoxelShape TABLE_CLASSIC_NORTH_WEST = createCuboidShape(0, 2, 1,3, 4, 3);
    final static VoxelShape TABLE_CLASSIC_NORTH = createCuboidShape(3, 2, 1,13, 4, 3);
    final static VoxelShape TABLE_CLASSIC_WEST_NORTH = createCuboidShape(1, 2, 0, 3, 4, 3);
    final static VoxelShape TABLE_CLASSIC_WEST_SOUTH = createCuboidShape(1, 2, 13,3, 4, 16);
    final static VoxelShape TABLE_CLASSIC_WEST = createCuboidShape(1, 2, 3,3, 4, 13);

    // Cursed I know
    final static Map<String, VoxelShape> VOXEL_SHAPES = new HashMap<>();
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

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
            newVoxelShape = VoxelShapes.union(newVoxelShape, TABLE_CLASSIC_NORTH_EAST_LEG, TABLE_CLASSIC_SOUTH_WEST_LEG, TABLE_CLASSIC_SOUTH_EAST_LEG, TABLE_CLASSIC_NORTH_WEST_LEG);
        }
        if (!north && !east) {
            newVoxelShape = VoxelShapes.union(newVoxelShape, TABLE_CLASSIC_NORTH_EAST_LEG);
        }
        if (!north && !west)  {
            newVoxelShape = VoxelShapes.union(newVoxelShape, TABLE_CLASSIC_NORTH_WEST_LEG);
        }
        if (!south && !east)  {
            newVoxelShape = VoxelShapes.union(newVoxelShape, TABLE_CLASSIC_SOUTH_EAST_LEG);
        }
        if (!south && !west)  {
            newVoxelShape = VoxelShapes.union(newVoxelShape, TABLE_CLASSIC_SOUTH_WEST_LEG);
        }
        VOXEL_SHAPES.put(key, newVoxelShape);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    public boolean canConnect(BlockView world, BlockState state, BlockPos neighborPos, BlockPos pos){
        BlockState neighborState = world.getBlockState(neighborPos);
        if (neighborState.contains(FACING)) {
            return canConnect(neighborState);
        }
        return false;
    }
}


