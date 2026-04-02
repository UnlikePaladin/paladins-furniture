package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
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

public class ClassicDeskBlock extends HorizontalDirectionalBlock {
    private final Block baseBlock;

    public static final MapCodec<ClassicDeskBlock> CODEC = createCodec(ClassicDeskBlock::new);
    private static final List<FurnitureBlock> WOOD_CLASSIC_DESKS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_DESKS = new ArrayList<>();
    private final BlockState baseBlockState;
    public final FurnitureBlock deskBlock;
    public ClassicDeskBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any());
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        this.deskBlock = new FurnitureBlock(this, "desk_classic");
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(ClassicDeskBlock.class)){
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
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(state.getBlock())) {
            this.baseBlockState.neighborChanged(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onPlace(this.baseBlockState, world, pos, oldState, false);
        }
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (AbstractSittableBlock.isWoodBased(this.defaultBlockState())) {
            return 20;
        }
        return 0;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    /** Method to rotate VoxelShapes from this random Forge Forums thread: https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/ */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, Shapes.empty() };

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    final static VoxelShape TABLE_CLASSIC_BASE = box(0, 14, 0, 16, 16, 16);
    final static VoxelShape TABLE_CLASSIC_NORTH_EAST_LEG = box(13, 0, 1, 15, 14, 3);
    final static VoxelShape TABLE_CLASSIC_EAST_NORTH = box(13, 2, 0,15, 4, 3);
    final static VoxelShape TABLE_CLASSIC_NORTH_EAST = box(13, 2, 1,16, 4, 3);
    final static VoxelShape TABLE_CLASSIC_NORTH_WEST_LEG = box(1, 0, 1, 3, 14, 3);
    final static VoxelShape TABLE_CLASSIC_SOUTH_WEST_LEG = box(1, 0, 13, 3, 14, 15);
    final static VoxelShape TABLE_CLASSIC_SOUTH_EAST_LEG = box(13, 0, 13, 15, 14, 15);
    final static VoxelShape TABLE_CLASSIC_SOUTH_EAST = box(13, 2, 13, 15, 4, 16);
    final static VoxelShape TABLE_CLASSIC_EAST = box(13, 2, 3, 15, 4, 13);
    final static VoxelShape TABLE_CLASSIC_NORTH_WEST = box(0, 2, 1,3, 4, 3);
    final static VoxelShape TABLE_CLASSIC_NORTH = box(3, 2, 1,13, 4, 3);
    final static VoxelShape TABLE_CLASSIC_WEST_NORTH = box(1, 2, 0, 3, 4, 3);
    final static VoxelShape TABLE_CLASSIC_WEST_SOUTH = box(1, 2, 13,3, 4, 16);
    final static VoxelShape TABLE_CLASSIC_WEST = box(1, 2, 3,3, 4, 13);

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

    public boolean canConnect(BlockGetter world, BlockState state, BlockPos neighborPos, BlockPos pos){
        BlockState neighborState = world.getBlockState(neighborPos);
        if (neighborState.hasProperty(FACING)) {
            return canConnect(neighborState);
        }
        return false;
    }
}


