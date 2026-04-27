package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
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

public abstract class AbstractSinkBlock extends CauldronBlock implements EntityBlock {
    public static final IntegerProperty LEVEL_4 = IntegerProperty.create("level", 0, 3);
    final Map<Item, SinkBehavior> behaviorMap;

    public AbstractSinkBlock(BlockBehaviour.Properties settings, Map<Item, SinkBehavior> behaviorMap) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        this.behaviorMap = behaviorMap;
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(LEVEL_4, 0));

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
        SinkBehavior sinkBehavior = this.behaviorMap.get(itemStack.getItem());
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
    public void handleRain(Level world, BlockPos pos) {
        if (world.random.nextInt(20) != 1) {
            return;
        }
        float f = world.getBiome(pos).getTemperature(pos);
        if (f < 0.15f) {
            return;
        }
        BlockState blockState = world.getBlockState(pos);
        if (blockState.get(LEVEL_4) < 4) {
            world.setBlockAndUpdate(pos, blockState.cycle(LEVEL_4), 2);
        }
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
    public BlockEntity newBlockEntity(BlockGetter getter) {
        return SinkBlockEntity.getFactory().get();
    }

    protected double getContentHeight(BlockState state) {
        return (6.0 + (double) state.getValue(LEVEL_4).intValue() * 3.0) / 16.0;
    }

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
    public void onEntityCollision(BlockState state, Level world, BlockPos pos, Entity entity) {
        int level = state.getValue(LEVEL);
        float waterLevel = pos.getY() + getContentHeight(state);
        if (!world.isClientSide && entity.isOnFire() && level > 0 && entity.getY() <= waterLevel){
            entity.extinguish();
            this.onFireCollision(state, world, pos);
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
}
