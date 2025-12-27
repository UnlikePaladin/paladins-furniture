package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity3x3;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ClassicDeskCabinetBlock extends HorizontalFacingBlockWithEntity {
    private final Block baseBlock;
    public static BooleanProperty OPEN = Properties.OPEN;


    public static final MapCodec<ClassicDeskCabinetBlock> CODEC = createCodec(ClassicDeskCabinetBlock::new);
    private static final List<FurnitureBlock> WOOD_CLASSIC_DESK_CABINETS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_DESK_CABINETS = new ArrayList<>();
    private final BlockState baseBlockState;
    protected FurnitureBlock deskCabinetBlock;
    public ClassicDeskCabinetBlock(Settings settings) {
        super(settings.luminance((state) -> 0).emissiveLighting((blockstate, b, c) -> false));
        setDefaultState(this.getStateManager().getDefaultState().with(OPEN, false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
        this.deskCabinetBlock = new FurnitureBlock(this, "desk_cabinet_classic");
        if(AbstractSittableBlock.isWoodBased(this.getDefaultState()) && this.getClass().isAssignableFrom(ClassicDeskCabinetBlock.class)){
            WOOD_CLASSIC_DESK_CABINETS.add(deskCabinetBlock);
        }
        else if (this.getClass().isAssignableFrom(ClassicDeskCabinetBlock.class)){
            STONE_CLASSIC_DESK_CABINETS.add(deskCabinetBlock);
        }
    }


    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamWoodClassicDeskCabinets() {
        return WOOD_CLASSIC_DESK_CABINETS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicDeskCabinets() {
        return STONE_CLASSIC_DESK_CABINETS.stream();
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }

    public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction face) {
        if (AbstractSittableBlock.isWoodBased(this.getDefaultState())) {
            return 20;
        }
        return 0;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(OPEN);
        super.appendProperties(stateManager);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public boolean canConnect(BlockState blockState) {
        if (blockState.getBlock() instanceof ClassicDeskCabinetBlock && !PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect()) {
            return (deskCabinetBlock.getBaseMaterial() == ((ClassicDeskCabinetBlock)blockState.getBlock()).deskCabinetBlock.getBaseMaterial());
        } else if (blockState.getBlock() instanceof ClassicDeskCabinetBlock){
            return true;
        } else if (blockState.getBlock() instanceof ClassicDeskBlock && !PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect()) {
            return (deskCabinetBlock.getBaseMaterial() == ((ClassicDeskBlock)blockState.getBlock()).deskBlock.getBaseMaterial());
        } else if (blockState.getBlock() instanceof ClassicDeskBlock) {
            return true;
        }
        return false;
    }

    public boolean canConnect(BlockView world, BlockState state, BlockPos neighborPos, BlockPos pos){
        BlockState neighborState = world.getBlockState(neighborPos);
        if (neighborState.contains(FACING)) {
            return canConnect(neighborState);
        }
        return false;
    }

    public boolean isDifferentOrientation(BlockView world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.offset(dir));
        return !canConnect(blockState); //|| blockState.get(FACING) != state.get(FACING);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    /** Method to rotate VoxelShapes from this random Forge Forums thread: https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/ */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, VoxelShapes.empty() };

        int times = (to.getHorizontalQuarterTurns() - from.getHorizontalQuarterTurns() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    final static VoxelShape TABLE_CLASSIC_BASE = createCuboidShape(0, 14, 0, 16, 16, 16);
    final static VoxelShape TABLE_CLASSIC_NORTH_EAST_LEG = createCuboidShape(13, 0, 1, 15, 14, 3);
    final static VoxelShape TABLE_CLASSIC_NORTH_WEST_LEG = createCuboidShape(1, 0, 1, 3, 14, 3);
    final static VoxelShape TABLE_CLASSIC_SOUTH_WEST_LEG = createCuboidShape(1, 0, 13, 3, 14, 15);
    final static VoxelShape TABLE_CLASSIC_SOUTH_EAST_LEG = createCuboidShape(13, 0, 13, 15, 14, 15);

    final static VoxelShape DESK_SINGLE_CLOSED = VoxelShapes.union(createCuboidShape(3, 2, 3, 13, 14, 13), createCuboidShape(3, 9, 2,13, 13, 3), createCuboidShape(3, 4, 2,13, 8, 3),createCuboidShape(6.5, 5.5, 1, 9.5, 6.5, 2), createCuboidShape(6.5, 10.5, 1,9.5, 11.5, 2));
    final static VoxelShape DESK_SINGLE_OPEN = VoxelShapes.union(createCuboidShape(3, 2, 3, 13, 14, 13), createCuboidShape(3, 9, 2,13, 13, 3), createCuboidShape(3, 4, -2,13, 8, 3),createCuboidShape(6.5, 5.5, -3, 9.5, 6.5, -2), createCuboidShape(6.5, 10.5, 1,9.5, 11.5, 2));

    final static VoxelShape DESK_LEFT_CLOSED = VoxelShapes.union(createCuboidShape(1, 9, 2, 11, 13, 3), createCuboidShape(1, 4, 2,11, 8, 3), createCuboidShape(4.5, 5.5, 1,7.5, 6.5, 2),createCuboidShape(4.5, 10.5, 1, 7.5, 11.5, 2), createCuboidShape(0, 2, 3,13, 14, 13));
    final static VoxelShape DESK_LEFT_OPEN= VoxelShapes.union(createCuboidShape(1, 9, 2, 11, 13, 3), createCuboidShape(4.5, 10.5, 1,7.5, 11.5, 2), createCuboidShape(0, 2, 3,13, 14, 13),createCuboidShape(1, 4, -2, 11, 8, 3), createCuboidShape(4.5, 5.5, -3,7.5, 6.5, -2));

    final static VoxelShape DESK_RIGHT_CLOSED = VoxelShapes.union(createCuboidShape(5, 9, 2, 15, 13, 3), createCuboidShape(5, 4, 2,15, 8, 3), createCuboidShape(8.5, 5.5, 1,11.5, 6.5, 2),createCuboidShape(8.5, 10.5, 1, 11.5, 11.5, 2), createCuboidShape(3, 2, 3,16, 14, 13));
    final static VoxelShape DESK_RIGHT_OPEN = VoxelShapes.union(createCuboidShape(5, 9, 2, 15, 13, 3), createCuboidShape(8.5, 10.5, 1,11.5, 11.5, 2), createCuboidShape(3, 2, 3,16, 14, 13),createCuboidShape(5, 4, -2, 15, 8, 3), createCuboidShape(8.5, 5.5, -3,11.5, 6.5, -2));

    final static VoxelShape DESK_MIDDLE_CLOSED = VoxelShapes.union(createCuboidShape(3, 9, 2, 13, 13, 3), createCuboidShape(3, 4, 2,13, 8, 3), createCuboidShape(6.5, 5.5, 1,9.5, 6.5, 2),createCuboidShape(6.5, 10.5, 1, 9.5, 11.5, 2), createCuboidShape(0, 2, 3,16, 14, 13));
    final static VoxelShape DESK_MIDDLE_OPEN = VoxelShapes.union(createCuboidShape(3, 9, 2, 13, 13, 3), createCuboidShape(6.5, 10.5, 1,9.5, 11.5, 2), createCuboidShape(0, 2, 3,16, 14, 13),createCuboidShape(3, 4, -2, 13, 8, 3), createCuboidShape(6.5, 5.5, -3,9.5, 6.5, -2));

    final static VoxelShape DESK_OUTER_CORNER_CLOSED = VoxelShapes.union(createCuboidShape(3, 2, 3, 16, 14, 13), createCuboidShape(3, 2, 13,13, 14, 16), createCuboidShape(4, 9, 2,14, 13, 3),createCuboidShape(4, 4, 2, 14, 8, 3), createCuboidShape(7.5, 5.5, 1,10.5, 6.5, 2), createCuboidShape(7.5, 10.5, 1,10.5, 11.5, 2),createCuboidShape(2, 9, 4,3, 13, 14),createCuboidShape(2, 4, 4,3, 8, 14),createCuboidShape(1, 5.5, 7.5,2, 6.5, 10.5),createCuboidShape(1, 10.5, 7.5,2, 11.5, 10.5));
    final static VoxelShape DESK_OUTER_CORNER_OPEN = VoxelShapes.union(createCuboidShape(3, 2, 3, 13, 14, 16), createCuboidShape(-2, 4, 4,3, 8, 14), createCuboidShape(-3, 5.5, 7.5,-2, 6.5, 10.5),createCuboidShape(13, 2, 3, 16, 14, 13), createCuboidShape(4, 9, 2,14, 13, 3), createCuboidShape(4, 4, 2,14, 8, 3),createCuboidShape(7.5, 5.5, 1,10.5, 6.5, 2),createCuboidShape(7.5, 10.5, 1,10.5, 11.5, 2),createCuboidShape(2, 9, 4,3, 13, 14),createCuboidShape(1, 10.5, 7.5,2, 11.5, 10.5));

    final static VoxelShape DESK_INSIDE_CORNER = createCuboidShape(0, 2, 0,13, 14, 13);

    // Cursed I know
    final static Map<String, VoxelShape> VOXEL_SHAPES = new HashMap<>();
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        Boolean north = canConnect(world.getBlockState(pos.north()));
        boolean east = canConnect(world.getBlockState(pos.east()));
        boolean west = canConnect(world.getBlockState(pos.west()));
        boolean south = canConnect(world.getBlockState(pos.south()));
        boolean open = state.get(OPEN);
        Direction isFacing = state.get(ClassicDeskCabinetBlock.FACING);

        BlockState rightState = world.getBlockState(pos.offset(isFacing.rotateYClockwise()));
        boolean right = canConnect(rightState) && rightState.getBlock() instanceof ClassicDeskCabinetBlock;

        BlockState leftState = world.getBlockState(pos.offset(isFacing.rotateYCounterclockwise()));
        boolean left = canConnect(leftState) && leftState.getBlock() instanceof ClassicDeskCabinetBlock;

        BlockState neighborStateFacing = world.getBlockState(pos.offset(isFacing.getOpposite()));
        BlockState neighborStateOpposite = world.getBlockState(pos.offset(isFacing));

        boolean rotatedCorner = false;
        String corner;
        if (canConnect(neighborStateFacing) && neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
            Direction neighborFacing = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
            // inner corner
            if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(world, pos, neighborFacing)) {
                corner = "inner";
                if (neighborFacing != isFacing.rotateYCounterclockwise()) {
                    rotatedCorner = true;
                }
            } else {
                corner = "none";
            }
        } else if (canConnect(neighborStateOpposite) && neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
            Direction neighborFacing = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
            // outer corner
            if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(world, pos, neighborFacing.getOpposite())) {
                corner = "outer";
                if (neighborFacing != isFacing.rotateYCounterclockwise()) {
                   rotatedCorner = true;
                }
            } else {
                corner = "none";
            }
        } else {
            corner = "none";
        }


        String key = north.toString()+ east + west + south + left + right + corner + rotatedCorner + open + isFacing.asString();
        if (!VOXEL_SHAPES.containsKey(key)) {
            generateVoxelShape(key, north, east, west, south, left, right, corner, rotatedCorner, open, isFacing);
        }
        return VOXEL_SHAPES.get(key);
    }
    private static void generateVoxelShape(String key, Boolean north, Boolean east, Boolean west, Boolean south, boolean left, boolean right, String corner, boolean rotatedCorner, boolean open, Direction facing) {
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
        if (corner == "none") {
            newVoxelShape = VoxelShapes.union(newVoxelShape, rotateShape(Direction.SOUTH, facing, middleDeskShape(right, left, open)));
        } else if (corner == "outer") {
            if (!rotatedCorner) {
                if (open) {
                    newVoxelShape = VoxelShapes.union(newVoxelShape, rotateShape(Direction.SOUTH, facing, DESK_OUTER_CORNER_OPEN));
                } else {
                    newVoxelShape = VoxelShapes.union(newVoxelShape, rotateShape(Direction.SOUTH, facing, DESK_OUTER_CORNER_CLOSED));
                }
            } else {
                if (open) {
                    newVoxelShape = VoxelShapes.union(newVoxelShape, rotateShape(Direction.EAST, facing, DESK_OUTER_CORNER_OPEN));
                } else {
                    newVoxelShape = VoxelShapes.union(newVoxelShape, rotateShape(Direction.EAST, facing, DESK_OUTER_CORNER_CLOSED));
                }
            }
        } else {
            if (!rotatedCorner)
                newVoxelShape = VoxelShapes.union(newVoxelShape, rotateShape(Direction.SOUTH, facing, DESK_INSIDE_CORNER));
            else
                newVoxelShape = VoxelShapes.union(newVoxelShape, rotateShape(Direction.EAST, facing, DESK_INSIDE_CORNER));
        }

        VOXEL_SHAPES.put(key, newVoxelShape);
    }

    private static VoxelShape middleDeskShape(boolean left, boolean right, boolean open) {
        if (left && right) {
            if (open) {
                return DESK_MIDDLE_OPEN;
            }
            return DESK_MIDDLE_CLOSED;
        }  else if (left) {
            if (open) {
                return DESK_LEFT_OPEN;
            }
            return DESK_LEFT_CLOSED;
        } else if (right) {
            if (open) {
                return DESK_RIGHT_OPEN;
            }
            return DESK_RIGHT_CLOSED;
        } else {
            if (open) {
                return DESK_SINGLE_OPEN;
            }
            return DESK_SINGLE_CLOSED;
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return GenericStorageBlockEntity3x3.getFactory().create(pos, state);
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GenericStorageBlockEntity3x3 && world instanceof ServerWorld) {
            player.openHandledScreen((GenericStorageBlockEntity3x3)blockEntity);
            player.incrementStat(Statistics.DRAWER_SEARCHED);
            PiglinBrain.onGuardedBlockInteracted((ServerWorld) world, player, true);
        }
        return ActionResult.CONSUME;
    }
}


