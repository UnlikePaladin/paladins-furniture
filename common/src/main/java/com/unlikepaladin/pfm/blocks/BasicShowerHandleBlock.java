package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.ShowerHandleBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BasicShowerHandleBlock extends HorizontalFacingBlockWithEntity {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final List<BasicShowerHandleBlock> SHOWER_HANDLE_BLOCKS = new ArrayList<>();
    public BasicShowerHandleBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
        SHOWER_HANDLE_BLOCKS.add(this);
    }

    public static Stream<BasicShowerHandleBlock> streamShowerHandleBlocks() {
        return SHOWER_HANDLE_BLOCKS.stream();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(POWERED, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockState blockState = this.toggleOpen(state, world, pos, false, false);
        float f = blockState.getValue(POWERED) ? 0.9f : 0.8f;
        world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, f);
        world.gameEvent(player, blockState.getValue(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasTag()) {
            CompoundTag nbtCompound = itemStack.getTagElement("BlockEntityTag");
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (nbtCompound.contains("showerHead") && blockEntity instanceof ShowerHandleBlockEntity) {
                ((ShowerHandleBlockEntity)blockEntity).saveAdditional(nbtCompound);
                itemStack.setTag(null);
            }
        }

    }
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(POWERED);
    }

    public BlockState toggleOpen(BlockState state, Level world, BlockPos pos, boolean listenTo, boolean toggleTo) {
        if(listenTo) {
            state = state.setValue(POWERED, toggleTo);
        }
        else {
        state = state.cycle(POWERED);}
        world.setBlock(pos, state, Block.UPDATE_ALL);
        this.updateNeighbors(state, world, pos);
        if (world.getBlockEntity(pos) instanceof ShowerHandleBlockEntity)
            ((ShowerHandleBlockEntity)(world.getBlockEntity(pos))).setState(state.getValue(POWERED));
        return state;
    }

    private static final VoxelShape SHOWER_HANDLE = Shapes.or(box(11.5, 8, 14, 12.5, 9, 15), box(11, 7.5, 15, 13, 9.5, 16), box(11.5, 6.5, 13, 12.5, 10.5, 14), box(10, 8, 13, 14, 9, 14), box(3.5, 8, 14, 4.5, 9, 15), box(3, 7.5, 15, 5, 9.5, 16), box(3.5, 6.5, 13, 4.5, 10.5, 14), box(2, 8, 13, 6, 9, 14));
    private static final VoxelShape SHOWER_HANDLE_SOUTH = BasicTableBlock.rotateShape(Direction.NORTH, Direction.SOUTH, SHOWER_HANDLE);
    private static final VoxelShape SHOWER_HANDLE_EAST = BasicTableBlock.rotateShape(Direction.NORTH, Direction.EAST, SHOWER_HANDLE);
    private static final VoxelShape SHOWER_HANDLE_WEST = BasicTableBlock.rotateShape(Direction.NORTH, Direction.WEST, SHOWER_HANDLE);

    private static final VoxelShape SHOWER_HANDLE_OPEN = Shapes.or(box(11, 7.5, 15, 13, 9.5, 16), box(11.5, 8, 13, 12.5, 9, 15), box(12.5, 9, 13, 13.5, 10, 14), box(12.5, 7, 13, 13.5, 8, 14), box(10.5, 9, 13, 11.5, 10, 14), box(10.5, 7, 13, 11.5, 8, 14), box(3.5, 8, 13, 4.5, 9, 15), box(4.5, 9, 13,5.5, 10, 14), box(4.5, 7, 13,5.5, 8, 14), box(2.5, 9, 13,3.5, 10, 14), box(2.5, 7, 13, 3.5, 8, 14), box(3, 7.5, 15,5, 9.5, 16));
    private static final VoxelShape SHOWER_HANDLE_SOUTH_OPEN = BasicTableBlock.rotateShape(Direction.NORTH, Direction.SOUTH, SHOWER_HANDLE_OPEN);
    private static final VoxelShape SHOWER_HANDLE_EAST_OPEN = BasicTableBlock.rotateShape(Direction.NORTH, Direction.EAST, SHOWER_HANDLE_OPEN);
    private static final VoxelShape SHOWER_HANDLE_WEST_OPEN = BasicTableBlock.rotateShape(Direction.NORTH, Direction.WEST, SHOWER_HANDLE_OPEN);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = getDirection(state);
        boolean open = state.getValue(POWERED);
        switch (facing) {
            case SOUTH: {
                if(open)
                    return SHOWER_HANDLE_SOUTH_OPEN;
                else
                    return SHOWER_HANDLE_SOUTH;
            }
            case EAST: {
                if (open)
                    return SHOWER_HANDLE_EAST_OPEN;
                else
                    return SHOWER_HANDLE_EAST;
            }
            case WEST: {
                if (open)
                    return SHOWER_HANDLE_WEST_OPEN;
                else
                    return SHOWER_HANDLE_WEST;
            }
            default:  {
                if (open)
                    return SHOWER_HANDLE_OPEN;
                else
                    return SHOWER_HANDLE;
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    private void updateNeighbors(BlockState state, Level world, BlockPos pos) {
        world.updateNeighborsAt(pos, this);
        world.updateNeighborsAt(pos.relative(getDirection(state).getOpposite()), this);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (world.getBlockEntity(pos) != null) {
            ((ShowerHandleBlockEntity)(world.getBlockEntity(pos))).setState(false);
        }
        this.spawnDestroyParticles(world, player, pos, state);
        if (state.is(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinAi.angerNearbyPiglins(player, false);
        }
        world.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShowerHandleBlockEntity(pos, state);
    }

    protected static Direction getDirection(BlockState state) {
        return state.getValue(FACING);
    }
}
