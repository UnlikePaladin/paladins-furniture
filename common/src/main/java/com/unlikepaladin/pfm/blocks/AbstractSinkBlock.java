package com.unlikepaladin.pfm.blocks;

import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.blocks.blockentities.SinkBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

import static com.unlikepaladin.pfm.blocks.BasicShowerHandleBlock.FACING;
import static com.unlikepaladin.pfm.blocks.BasicToiletBlock.createTicketHelper;

public abstract class AbstractSinkBlock extends AbstractCauldronBlock implements EntityBlock {
    public static final IntegerProperty LEVEL_4 = IntegerProperty.create("level", 0, 3);
    final CauldronInteraction.InteractionMap behaviorMap;
    final Biome.Precipitation precipitationPredicate;

    public AbstractSinkBlock(BlockBehaviour.Properties settings, Biome.Precipitation precipitationPredicate, CauldronInteraction.InteractionMap behaviorMap) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false), behaviorMap);
        this.behaviorMap = behaviorMap;
        this.precipitation = precipitation;
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(LEVEL_4, 0));

        if (CODEC == null) {
            CODEC = RecordCodecBuilder.mapCodec((instance) -> {
                return instance.group(Biome.Precipitation.CODEC.fieldOf("precipitation").forGetter((block) -> {
                    return block.precipitation;
                }), CauldronBehavior.CODEC.fieldOf("interactions").forGetter((block) -> {
                    return block.behaviorMap;
                }), createSettingsCodec()).apply(instance, (precipitation1, cauldronBehaviorMap, settings1) -> getSinkConstructor().apply(settings1, precipitation1, cauldronBehaviorMap));
            });
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
        stateManager.add(LEVEL_4);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection());
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos sourcePos = pos.below().below();
        ItemStack itemStack = player.getItemInHand(hand);
        CauldronInteraction sinkBehavior = this.behaviorMap.map().get(itemStack.getItem());
        if (sinkBehavior != null && itemStack.getItem() != Items.AIR) {
            return sinkBehavior.interact(state, world, pos, player, hand, itemStack);
        }
        if (state.getValue(LEVEL_4) < 3) {
            BlockState sourceState = world.getBlockState(sourcePos);
            if (sourceState.getFluidState().getType() == Fluids.WATER && !sourceState.getFluidState().isEmpty()) {
                if (sourceState.getProperties().contains(BlockStateProperties.WATERLOGGED)) {
                    world.setBlockAndUpdate(sourcePos, sourceState.setValue(BlockStateProperties.WATERLOGGED, false));
                }
                else {
                    world.setBlockAndUpdate(sourcePos, Blocks.AIR.defaultBlockState());
                }
                SinkBlockEntity blockEntity = (SinkBlockEntity) world.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.setFilling(true);
                }
                world.setBlockAndUpdate(pos, state.setValue(LEVEL_4, 3));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return state.getValue(LEVEL_4);
    }

    @Override
    protected void receiveStalactiteDrip(BlockState state, Level world, BlockPos pos, Fluid fluid) {
        if (this.isFull(state)) {
            return;
        }
        world.setBlockAndUpdate(pos, state.setValue(LEVEL_4, state.getValue(LEVEL_4) + 1));
        world.levelEvent(LevelEvent.SOUND_DRIP_WATER_INTO_CAULDRON, pos, 0);
    }

    protected static boolean canFillWithPrecipitation(Level world, Biome.Precipitation precipitation) {
        if (precipitation == Biome.Precipitation.RAIN) {
            return world.getRandom().nextFloat() < 0.05f;
        }
        if (precipitation == Biome.Precipitation.SNOW) {
            return world.getRandom().nextFloat() < 0.1f;
        }
        return false;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTicketHelper(type, BlockEntities.SINK_BLOCK_ENTITY, SinkBlockEntity::tick);
    }

    @Override
    public void handlePrecipitation(BlockState state, Level world, BlockPos pos, Biome.Precipitation precipitation) {
        if (!canFillWithPrecipitation(world, precipitation) || state.getValue(LEVEL_4) == 3 || precipitation != this.precipitation) {
            return;
        }
        world.setBlockAndUpdate(pos, state.cycle(LEVEL_4));
    }

    public static void spawnParticles(Direction facing, Level world, BlockPos pos) {
        if (world.isClientSide) {
            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            if (facing == Direction.EAST) {
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.76, y + 1.19, z + 0.5, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.76, y + 1.19, z + 0.5, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.76, y + 1.19, z + 0.5, 0.0, 0.0, 0.0);
            }
            else if (facing == Direction.SOUTH){
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 1.19, z + 0.76, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 1.19, z + 0.76, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 1.19, z + 0.76, 0.0, 0.0, 0.0);
            }
            else if (facing == Direction.NORTH){
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 1.19, z + 0.24, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 1.19, z + 0.24, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 1.19, z + 0.24, 0.0, 0.0, 0.0);
            }
            else {
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.24, y + 1.19, z + 0.5, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.24, y + 1.19, z + 0.5, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.24, y + 1.19, z + 0.5, 0.0, 0.0, 0.0);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return SinkBlockEntity.getFactory().create(pos, state);
    }

    @Override
    protected double getContentHeight(BlockState state) {
        return (6.0 + (double) state.getValue(LEVEL_4).intValue() * 3.0) / 16.0;
    }

    @Override
    protected boolean canReceiveStalactiteDrip(Fluid fluid) {
        return fluid == Fluids.WATER && this.precipitation == Biome.Precipitation.RAIN;
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL_4) == 3;
    }

    public static void decrementFluidLevel(BlockState state, Level world, BlockPos pos) {
        int i = state.getValue(LEVEL_4) - 1;
        world.setBlockAndUpdate(pos, state.setValue(LEVEL_4, i));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected void onFireCollision(BlockState state, Level world, BlockPos pos) {
        if (state.getValue(LEVEL_4) > 0)
            AbstractSinkBlock.decrementFluidLevel(state, world, pos);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!world.isClientSide && entity.isOnFire() && this.isEntityInsideContent(state, pos, entity)) {
            entity.clearFire();
            if (entity.mayInteract(world, pos)) {
                this.onFireCollision(state, world, pos);
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public static MapCodec<AbstractSinkBlock> CODEC = null;

    @Override
    protected MapCodec<? extends AbstractCauldronBlock> getCodec() {
        return CODEC;
    }

    public abstract Function3<Settings, Biome.Precipitation, CauldronBehavior.CauldronBehaviorMap, AbstractSinkBlock> getSinkConstructor();
}
