package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity9x3;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.Container;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClassicNightstandBlock extends HorizontalFacingBlockWithEntity {
    public static BooleanProperty OPEN = BlockStateProperties.OPEN;
    private static final List<FurnitureBlock> WOOD_NIGHTSTAND = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_NIGHTSTAND = new ArrayList<>();
    public static final MapCodec<ClassicNightstandBlock> CODEC = simpleCodec(ClassicNightstandBlock::new);

    public ClassicNightstandBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(ClassicNightstandBlock.class)){
            WOOD_NIGHTSTAND.add(new FurnitureBlock(this, "classic_nightstand"));
        }
        else if (this.getClass().isAssignableFrom(ClassicNightstandBlock.class)){
            STONE_NIGHTSTAND.add(new FurnitureBlock(this, "classic_nightstand"));
        }
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamWoodClassicNightstands() {
        return WOOD_NIGHTSTAND.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicNightstands() {
        return STONE_NIGHTSTAND.stream();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(OPEN);
        super.createBlockStateDefinition(stateManager);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.CONSUME;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GenericStorageBlockEntity9x3) {
            player.openMenu((GenericStorageBlockEntity9x3)blockEntity);
            player.awardStat(Statistics.CABINET_SEARCHED);
            PiglinAi.angerNearbyPiglins(player, true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.is(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Container) {
            Containers.dropContents(world, pos, (Container) blockEntity);
            world.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return GenericStorageBlockEntity9x3.getFactory().create(pos, state);
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (AbstractSittableBlock.isWoodBased(state)) {
            return 20;
        }
        return 0;
    }

    public boolean isStand(BlockGetter world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.relative(direction));
        if(state.getBlock() == this)
        {
            Direction sourceDirection = state.getValue(FACING);
            return sourceDirection.equals(tableDirection);
        }
        return false;
    }

    static final VoxelShape NIGHT_STAND = Shapes.or(box(0, 14, 0,16, 16, 16),box(3, 1, 1,13, 3, 2),box(13, 1, 1,15, 14, 2),box(1, 1, 2,15, 14, 15),box(0.5, 0, 0,3.5, 1, 16),box(12.5, 0, 0,15.5, 1, 16),box(4, 9, 1,12, 13, 2),box(4, 4, 1,12, 8, 2),box(6.5, 5.5, 0,9.5, 6.5, 1),box(6.5, 10.5, 0,9.5, 11.5, 1));
    static final VoxelShape NIGHT_STAND_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND);
    static final VoxelShape NIGHT_STAND_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND);
    static final VoxelShape NIGHT_STAND_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND);
    static final VoxelShape NIGHT_STAND_OPEN = Shapes.or(box(0, 14, 0,16, 16, 16),box(3, 1, 1,13, 3, 2),box(1, 1, 1,3, 14, 2),box(13, 1, 1,15, 14, 2),box(1, 1, 2,15, 14, 15),box(0.5, 0, 0,3.5, 1, 16),box(12.5, 0, 0,15.5, 1, 16),box(4, 9, 1,12, 13, 2),box(4, 4, -6,12, 8, 2),box(6.5, 10.5, 0,9.5, 11.5, 1),box(6.5, 5.5, -7,9.5, 6.5, -6));
    static final VoxelShape NIGHT_STAND_OPEN_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND_OPEN);
    static final VoxelShape NIGHT_STAND_OPEN_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND_OPEN);
    static final VoxelShape NIGHT_STAND_OPEN_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND_OPEN);

    static final VoxelShape NIGHT_STAND_MIDDLE = Shapes.or(box(0, 14, 0, 2, 16, 16),box(15, 14, 0, 16, 16, 16),box(1, 1, 0, 2, 3, 16),box(2, 1, 0, 15, 16, 16),box(1, 9, 2.5, 2, 13, 13.5),box(0, 10.5, 6.5, 1, 11.5, 9.5),box(0, 10.5, 6.5, 1, 11.5, 9.5));
    static final VoxelShape NIGHT_STAND_MIDDLE_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND_MIDDLE);
    static final VoxelShape NIGHT_STAND_MIDDLE_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND_MIDDLE);
    static final VoxelShape NIGHT_STAND_MIDDLE_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND_MIDDLE);
    static final VoxelShape NIGHT_STAND_MIDDLE_OPEN = Shapes.or(box(0, 14, 0, 2, 16, 16),box(15, 14, 0, 16, 16, 16),box(1, 1, 0, 2, 3, 16),box(2, 1, 0, 15, 16, 16),box(1, 9, 2.5, 2, 13, 13.5),box(0, 10.5, 6.5, 1, 11.5, 9.5),box(0, 10.5, 6.5, 1, 11.5, 9.5));
    static final VoxelShape NIGHT_STAND_MIDDLE_OPEN_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND_MIDDLE_OPEN);
    static final VoxelShape NIGHT_STAND_MIDDLE_OPEN_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND_MIDDLE_OPEN);
    static final VoxelShape NIGHT_STAND_MIDDLE_OPEN_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND_MIDDLE_OPEN);

    static final VoxelShape NIGHT_STAND_LEFT = Shapes.or(box(0, 14, 0, 2, 16, 16),box(15, 14, 1, 16, 16, 16),box(2, 14, 0, 16, 16, 1),box(1, 1, 3, 2, 3, 16),box(2, 1, 1, 15, 16, 16),box(1, 1, 1, 2, 14, 3),box(0, 0, 0.5, 16, 1, 3.5),box(1, 9, 4, 2, 13, 15),box(1, 4, 4, 2, 8, 15),box(0, 5.5, 8, 1, 6.5, 11),box(0, 10.5, 8, 1, 11.5, 11));
    static final VoxelShape NIGHT_STAND_LEFT_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND_LEFT);
    static final VoxelShape NIGHT_STAND_LEFT_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND_LEFT);
    static final VoxelShape NIGHT_STAND_LEFT_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND_LEFT);
    static final VoxelShape NIGHT_STAND_LEFT_OPEN = Shapes.or(box(0, 14, 1, 2, 16, 16),box(15, 14, 1, 16, 16, 16),box(0, 14, 0, 16, 16, 1),box(1, 1, 3, 2, 3, 16),box(-7, 5.5, 8, -6, 6.5, 11),box(-6, 4, 4, 2, 8, 15),box(1, 1, 1, 2, 14, 3),box(0, 0, 0.5, 16, 1, 3.5),box(1, 9, 4, 2, 13, 15),box(0, 10.5, 8, 1, 11.5, 11),box(2, 1, 1, 15, 16, 16));
    static final VoxelShape NIGHT_STAND_LEFT_OPEN_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND_LEFT_OPEN);
    static final VoxelShape NIGHT_STAND_LEFT_OPEN_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND_LEFT_OPEN);
    static final VoxelShape NIGHT_STAND_LEFT_OPEN_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND_LEFT_OPEN);

    static final VoxelShape NIGHT_STAND_RIGHT = Shapes.or(box(0, 14, 0, 2, 16, 15),box(15, 14, 0, 16, 16, 15),box(0, 14, 15, 16, 16, 16),box(1, 1, 0, 2, 3, 13),box(1, 1, 13, 2, 14, 15),box(0, 0, 12.5, 16, 1, 15.5),box(1, 9, 1, 2, 13, 12),box(0, 10.5, 5, 1, 11.5, 8),box(0, 5.5, 5, 1, 6.5, 8),box(2, 1, 0, 15, 16, 15),box(1, 4, 1, 2, 8, 12));
    static final VoxelShape NIGHT_STAND_RIGHT_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND_RIGHT);
    static final VoxelShape NIGHT_STAND_RIGHT_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND_RIGHT);
    static final VoxelShape NIGHT_STAND_RIGHT_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND_RIGHT);
    static final VoxelShape NIGHT_STAND_RIGHT_OPEN = Shapes.or(box(0, 14, 0, 2, 16, 15),box(15, 14, 0, 16, 16, 15),box(0, 14, 15, 16, 16, 16),box(1, 1, 0, 2, 3, 13),box(1, 1, 13, 2, 14, 15),box(0, 0, 12.5, 16, 1, 15.5),box(1, 9, 1, 2, 13, 12),box(0, 10.5, 5, 1, 11.5, 8),box(-7, 5.5, 5, -6, 6.5, 8),box(2, 1, 0, 15, 16, 15),box(-6, 4, 1, 2, 8, 12));
    static final VoxelShape NIGHT_STAND_RIGHT_OPEN_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, NIGHT_STAND_RIGHT_OPEN);
    static final VoxelShape NIGHT_STAND_RIGHT_OPEN_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, NIGHT_STAND_RIGHT_OPEN);
    static final VoxelShape NIGHT_STAND_RIGHT_OPEN_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, NIGHT_STAND_RIGHT_OPEN);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        boolean open = state.getValue(OPEN);
        boolean left = isStand(world, pos, dir.getCounterClockWise(), dir);
        boolean right = isStand(world, pos, dir.getClockWise(), dir);
        MiddleShape shape = left || right ? left && right ? MiddleShape.MIDDLE : left ? MiddleShape.LEFT : MiddleShape.RIGHT : MiddleShape.SINGLE;
        switch (shape) {
            case SINGLE -> {
                switch (dir) {
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
            case MIDDLE -> {
                switch (dir) {
                    case NORTH -> {
                        if (open) {
                            return NIGHT_STAND_MIDDLE_OPEN_EAST;
                        }
                        return NIGHT_STAND_MIDDLE_EAST;
                    }
                    case SOUTH -> {
                        if (open) {
                            return NIGHT_STAND_MIDDLE_OPEN_WEST;
                        }
                        return NIGHT_STAND_MIDDLE_WEST;
                    }
                    case EAST -> {
                        if (open) {
                            return NIGHT_STAND_MIDDLE_OPEN_SOUTH;
                        }
                        return NIGHT_STAND_MIDDLE_SOUTH;
                    }
                    default -> {
                        if (open) {
                            return NIGHT_STAND_MIDDLE_OPEN;
                        }
                        return NIGHT_STAND_MIDDLE;
                    }
                }
            }
            case LEFT -> {
                switch (dir) {
                    case NORTH -> {
                        if (open) {
                            return NIGHT_STAND_LEFT_OPEN_EAST;
                        }
                        return NIGHT_STAND_LEFT_EAST;
                    }
                    case SOUTH -> {
                        if (open) {
                            return NIGHT_STAND_LEFT_OPEN_WEST;
                        }
                        return NIGHT_STAND_LEFT_WEST;
                    }
                    case EAST -> {
                        if (open) {
                            return NIGHT_STAND_LEFT_OPEN_SOUTH;
                        }
                        return NIGHT_STAND_LEFT_SOUTH;
                    }
                    default -> {
                        if (open) {
                            return NIGHT_STAND_LEFT_OPEN;
                        }
                        return NIGHT_STAND_LEFT;
                    }
                }
            }
            default -> {
                switch (dir) {
                    case NORTH -> {
                        if (open) {
                            return NIGHT_STAND_RIGHT_OPEN_EAST;
                        }
                        return NIGHT_STAND_RIGHT_EAST;
                    }
                    case SOUTH -> {
                        if (open) {
                            return NIGHT_STAND_RIGHT_OPEN_WEST;
                        }
                        return NIGHT_STAND_RIGHT_WEST;
                    }
                    case EAST -> {
                        if (open) {
                            return NIGHT_STAND_RIGHT_OPEN_SOUTH;
                        }
                        return NIGHT_STAND_RIGHT_SOUTH;
                    }
                    default -> {
                        if (open) {
                            return NIGHT_STAND_RIGHT_OPEN;
                        }
                        return NIGHT_STAND_RIGHT;
                    }
                }
            }
        }

    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}
