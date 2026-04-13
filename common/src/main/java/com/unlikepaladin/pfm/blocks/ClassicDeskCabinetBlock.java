package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity3x3;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
    public static BooleanProperty OPEN = BlockStateProperties.OPEN;


    public static final MapCodec<ClassicDeskCabinetBlock> CODEC = simpleCodec(ClassicDeskCabinetBlock::new);
    private static final List<FurnitureBlock> WOOD_CLASSIC_DESK_CABINETS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_DESK_CABINETS = new ArrayList<>();
    private final BlockState baseBlockState;
    protected FurnitureBlock deskCabinetBlock;
    public ClassicDeskCabinetBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any().setValue(OPEN, false));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        this.deskCabinetBlock = new FurnitureBlock(this, "desk_cabinet_classic");
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(ClassicDeskCabinetBlock.class)){
            WOOD_CLASSIC_DESK_CABINETS.add(deskCabinetBlock);
        }
        else if (this.getClass().isAssignableFrom(ClassicDeskCabinetBlock.class)){
            STONE_CLASSIC_DESK_CABINETS.add(deskCabinetBlock);
        }
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamWoodClassicDeskCabinets() {
        return WOOD_CLASSIC_DESK_CABINETS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicDeskCabinets() {
        return STONE_CLASSIC_DESK_CABINETS.stream();
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(state.getBlock())) {
            this.onPlace(this.baseBlockState, world, pos, oldState, false);
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(OPEN);
        super.createBlockStateDefinition(stateManager);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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

    public boolean canConnect(BlockGetter world, BlockState state, BlockPos neighborPos, BlockPos pos){
        BlockState neighborState = world.getBlockState(neighborPos);
        if (neighborState.hasProperty(FACING)) {
            return canConnect(neighborState);
        }
        return false;
    }

    public boolean isDifferentOrientation(BlockGetter world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.relative(dir));
        return !canConnect(blockState); //|| blockState.getValue(FACING) != state.getValue(FACING);
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
    final static VoxelShape TABLE_CLASSIC_NORTH_WEST_LEG = box(1, 0, 1, 3, 14, 3);
    final static VoxelShape TABLE_CLASSIC_SOUTH_WEST_LEG = box(1, 0, 13, 3, 14, 15);
    final static VoxelShape TABLE_CLASSIC_SOUTH_EAST_LEG = box(13, 0, 13, 15, 14, 15);

    final static VoxelShape DESK_SINGLE_CLOSED = Shapes.or(box(3, 2, 3, 13, 14, 13), box(3, 9, 2,13, 13, 3), box(3, 4, 2,13, 8, 3),box(6.5, 5.5, 1, 9.5, 6.5, 2), box(6.5, 10.5, 1,9.5, 11.5, 2));
    final static VoxelShape DESK_SINGLE_OPEN = Shapes.or(box(3, 2, 3, 13, 14, 13), box(3, 9, 2,13, 13, 3), box(3, 4, -2,13, 8, 3),box(6.5, 5.5, -3, 9.5, 6.5, -2), box(6.5, 10.5, 1,9.5, 11.5, 2));

    final static VoxelShape DESK_LEFT_CLOSED = Shapes.or(box(1, 9, 2, 11, 13, 3), box(1, 4, 2,11, 8, 3), box(4.5, 5.5, 1,7.5, 6.5, 2),box(4.5, 10.5, 1, 7.5, 11.5, 2), box(0, 2, 3,13, 14, 13));
    final static VoxelShape DESK_LEFT_OPEN= Shapes.or(box(1, 9, 2, 11, 13, 3), box(4.5, 10.5, 1,7.5, 11.5, 2), box(0, 2, 3,13, 14, 13),box(1, 4, -2, 11, 8, 3), box(4.5, 5.5, -3,7.5, 6.5, -2));

    final static VoxelShape DESK_RIGHT_CLOSED = Shapes.or(box(5, 9, 2, 15, 13, 3), box(5, 4, 2,15, 8, 3), box(8.5, 5.5, 1,11.5, 6.5, 2),box(8.5, 10.5, 1, 11.5, 11.5, 2), box(3, 2, 3,16, 14, 13));
    final static VoxelShape DESK_RIGHT_OPEN = Shapes.or(box(5, 9, 2, 15, 13, 3), box(8.5, 10.5, 1,11.5, 11.5, 2), box(3, 2, 3,16, 14, 13),box(5, 4, -2, 15, 8, 3), box(8.5, 5.5, -3,11.5, 6.5, -2));

    final static VoxelShape DESK_MIDDLE_CLOSED = Shapes.or(box(3, 9, 2, 13, 13, 3), box(3, 4, 2,13, 8, 3), box(6.5, 5.5, 1,9.5, 6.5, 2),box(6.5, 10.5, 1, 9.5, 11.5, 2), box(0, 2, 3,16, 14, 13));
    final static VoxelShape DESK_MIDDLE_OPEN = Shapes.or(box(3, 9, 2, 13, 13, 3), box(6.5, 10.5, 1,9.5, 11.5, 2), box(0, 2, 3,16, 14, 13),box(3, 4, -2, 13, 8, 3), box(6.5, 5.5, -3,9.5, 6.5, -2));

    final static VoxelShape DESK_OUTER_CORNER_CLOSED = Shapes.or(box(3, 2, 3, 16, 14, 13), box(3, 2, 13,13, 14, 16), box(4, 9, 2,14, 13, 3),box(4, 4, 2, 14, 8, 3), box(7.5, 5.5, 1,10.5, 6.5, 2), box(7.5, 10.5, 1,10.5, 11.5, 2),box(2, 9, 4,3, 13, 14),box(2, 4, 4,3, 8, 14),box(1, 5.5, 7.5,2, 6.5, 10.5),box(1, 10.5, 7.5,2, 11.5, 10.5));
    final static VoxelShape DESK_OUTER_CORNER_OPEN = Shapes.or(box(3, 2, 3, 13, 14, 16), box(-2, 4, 4,3, 8, 14), box(-3, 5.5, 7.5,-2, 6.5, 10.5),box(13, 2, 3, 16, 14, 13), box(4, 9, 2,14, 13, 3), box(4, 4, 2,14, 8, 3),box(7.5, 5.5, 1,10.5, 6.5, 2),box(7.5, 10.5, 1,10.5, 11.5, 2),box(2, 9, 4,3, 13, 14),box(1, 10.5, 7.5,2, 11.5, 10.5));

    final static VoxelShape DESK_INSIDE_CORNER = box(0, 2, 0,13, 14, 13);

    // Cursed I know
    final static Map<String, VoxelShape> VOXEL_SHAPES = new HashMap<>();
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

        Boolean north = canConnect(world.getBlockState(pos.north()));
        boolean east = canConnect(world.getBlockState(pos.east()));
        boolean west = canConnect(world.getBlockState(pos.west()));
        boolean south = canConnect(world.getBlockState(pos.south()));
        boolean open = state.getValue(OPEN);
        Direction isFacing = state.getValue(ClassicDeskCabinetBlock.FACING);

        BlockState rightState = world.getBlockState(pos.relative(isFacing.getClockWise()));
        boolean right = canConnect(rightState) && rightState.getBlock() instanceof ClassicDeskCabinetBlock;

        BlockState leftState = world.getBlockState(pos.relative(isFacing.getCounterClockWise()));
        boolean left = canConnect(leftState) && leftState.getBlock() instanceof ClassicDeskCabinetBlock;

        BlockState neighborStateFacing = world.getBlockState(pos.relative(isFacing.getOpposite()));
        BlockState neighborStateOpposite = world.getBlockState(pos.relative(isFacing));

        boolean rotatedCorner = false;
        String corner;
        if (canConnect(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction neighborFacing = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
            // inner corner
            if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(world, pos, neighborFacing)) {
                corner = "inner";
                if (neighborFacing != isFacing.getCounterClockWise()) {
                    rotatedCorner = true;
                }
            } else {
                corner = "none";
            }
        } else if (canConnect(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction neighborFacing = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
            // outer corner
            if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isDifferentOrientation(world, pos, neighborFacing.getOpposite())) {
                corner = "outer";
                if (neighborFacing != isFacing.getCounterClockWise()) {
                   rotatedCorner = true;
                }
            } else {
                corner = "none";
            }
        } else {
            corner = "none";
        }


        String key = north.toString()+ east + west + south + left + right + corner + rotatedCorner + open + isFacing.getSerializedName();
        if (!VOXEL_SHAPES.containsKey(key)) {
            generateVoxelShape(key, north, east, west, south, left, right, corner, rotatedCorner, open, isFacing);
        }
        return VOXEL_SHAPES.get(key);
    }
    private static void generateVoxelShape(String key, Boolean north, Boolean east, Boolean west, Boolean south, boolean left, boolean right, String corner, boolean rotatedCorner, boolean open, Direction facing) {
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
        if (corner == "none") {
            newVoxelShape = Shapes.or(newVoxelShape, rotateShape(Direction.SOUTH, facing, middleDeskShape(right, left, open)));
        } else if (corner == "outer") {
            if (!rotatedCorner) {
                if (open) {
                    newVoxelShape = Shapes.or(newVoxelShape, rotateShape(Direction.SOUTH, facing, DESK_OUTER_CORNER_OPEN));
                } else {
                    newVoxelShape = Shapes.or(newVoxelShape, rotateShape(Direction.SOUTH, facing, DESK_OUTER_CORNER_CLOSED));
                }
            } else {
                if (open) {
                    newVoxelShape = Shapes.or(newVoxelShape, rotateShape(Direction.EAST, facing, DESK_OUTER_CORNER_OPEN));
                } else {
                    newVoxelShape = Shapes.or(newVoxelShape, rotateShape(Direction.EAST, facing, DESK_OUTER_CORNER_CLOSED));
                }
            }
        } else {
            if (!rotatedCorner)
                newVoxelShape = Shapes.or(newVoxelShape, rotateShape(Direction.SOUTH, facing, DESK_INSIDE_CORNER));
            else
                newVoxelShape = Shapes.or(newVoxelShape, rotateShape(Direction.EAST, facing, DESK_INSIDE_CORNER));
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
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return GenericStorageBlockEntity3x3.getFactory().create(pos, state);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GenericStorageBlockEntity3x3 && world instanceof ServerWorld) {
            player.openMenu((GenericStorageBlockEntity3x3)blockEntity);
            player.awardStat(Statistics.DRAWER_SEARCHED);
            PiglinAi.angerNearbyPiglins((ServerWorld) world, player, true);
        }
        return InteractionResult.CONSUME;
    }
}


