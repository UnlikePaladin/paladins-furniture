package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.LightSwitchBlockEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LightSwitchBlock extends HorizontalFacingBlockWithEntity {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final List<LightSwitchBlock> LIGHT_SWITCHES = new ArrayList<>();
    public static final MapCodec<LightSwitchBlock> CODEC = simpleCodec(LightSwitchBlock::new);

    public LightSwitchBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
        LIGHT_SWITCHES.add(this);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static Stream<LightSwitchBlock> streamlightSwitches() {
        return LIGHT_SWITCHES.stream();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(POWERED, false);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockState blockState = this.togglePower(state, world, pos, false, false);
        float f = blockState.getValue(POWERED) ? 0.9f : 0.8f;
        world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, f);
        world.gameEvent(player, blockState.getValue(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.has(DataComponents.BLOCK_ENTITY_DATA)) {
            CompoundTag nbtCompound = itemStack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).getUnsafe();
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (nbtCompound.contains("lights") && blockEntity instanceof LightSwitchBlockEntity) {
                nbtCompound.remove("lights");
                itemStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(nbtCompound));
            }
        }

    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(levelReader, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
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

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    public BlockState togglePower(BlockState state, Level world, BlockPos pos, boolean listenTo, boolean toggleTo) {
        if(listenTo) {
            state = state.setValue(POWERED, toggleTo);
        }
        else {
        state = state.cycle(POWERED);}
        world.setBlock(pos, state, Block.UPDATE_ALL);
        this.updateNeighbors(state, world, pos);
        if (world.getBlockEntity(pos) instanceof LightSwitchBlockEntity)
            ((LightSwitchBlockEntity)world.getBlockEntity(pos)).setState(state.getValue(POWERED));
        return state;
    }

    private static final VoxelShape lightSwitch = Shapes.or(box(5, 3, 15,11, 11, 16));
    private static final VoxelShape lightSwitchSouth = BasicTableBlock.rotateShape(Direction.NORTH, Direction.SOUTH, lightSwitch);
    private static final VoxelShape lightSwitchEast = BasicTableBlock.rotateShape(Direction.NORTH, Direction.EAST, lightSwitch);
    private static final VoxelShape lightSwitchWest = BasicTableBlock.rotateShape(Direction.NORTH, Direction.WEST, lightSwitch);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = getDirection(state);
        switch (facing) {
            case SOUTH -> {
                return lightSwitchSouth;
            }
            case EAST -> {
                return lightSwitchEast;
            }
            case WEST -> {
                return lightSwitchWest;
            }
            default ->  {
                return lightSwitch;
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
    @Override
    public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        if (state.getValue(POWERED) && getDirection(state) == direction) {
            return 15;
        }
        return 0;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getValue(POWERED)) {
            this.updateNeighbors(state, world, pos);
        }

        if (state.is(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LightSwitchBlockEntity lightSwitchBlockEntity) {
            lightSwitchBlockEntity.setRemoved();
        }
    }

    private void updateNeighbors(BlockState state, Level world, BlockPos pos) {
        world.updateNeighborsAt(pos, this);
        world.updateNeighborsAt(pos.relative(getDirection(state).getOpposite()), this);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (world.getBlockEntity(pos) != null) {
            this.togglePower(state, world, pos, true, false);
        }
       return super.playerWillDestroy(world, pos, state, player);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LightSwitchBlockEntity(pos, state);
    }


    protected static Direction getDirection(BlockState state) {
        return state.getValue(FACING);
    }

}
