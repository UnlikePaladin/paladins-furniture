package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.behavior.BathtubBehavior;
import com.unlikepaladin.pfm.blocks.blockentities.BathtubBlockEntity;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Entities;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.BasicToiletBlock.createTicketHelper;
import static com.unlikepaladin.pfm.blocks.SimpleStoolBlock.rotateShape;

public class BasicBathtubBlock extends BedBlock {
    public static final IntegerProperty LEVEL_8 = IntegerProperty.create("level", 0, 8);
    private final Map<Item, BathtubBehavior> behaviorMap;
    private final Biome.Precipitation precipitation;
    private static final List<BasicBathtubBlock> basicBathtubBlocks = new ArrayList<>();
    public BasicBathtubBlock(BlockBehaviour.Properties settings, Map<Item, BathtubBehavior> map, Biome.Precipitation precipitation) {
        super(DyeColor.WHITE, settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(LEVEL_8, 0).setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false));
        this.behaviorMap = map;
        this.precipitation = precipitation;
        this.height = 0.05f;
        basicBathtubBlocks.add(this);
    }

    public static Stream<BasicBathtubBlock> basicBathtubBlockStream() {
        return basicBathtubBlocks.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(LEVEL_8);
        super.createBlockStateDefinition(stateManager);
    }

    protected void onFireCollision(BlockState state, Level world, BlockPos pos) {
        decrementFluidLevel(state, world, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        if (state.getValue(PART) == BedPart.HEAD) {
            direction = direction.getOpposite();
        }
        return world.getBlockState(pos.relative(direction)).isAir() || world.getBlockState(pos.relative(direction)).getBlock() == this;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide) {
            world.setBlock(pos.relative(state.getValue(FACING)), this.defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(PART, BedPart.HEAD), 3);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void updateEntityMovementAfterFallOn(BlockGetter blockGetter, Entity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockPos blockPos;
        BlockState blockState;
        BedPart bedPart;
        if (!world.isClientSide && player.isCreative() && (bedPart = state.getValue(PART)) == BedPart.FOOT && (blockState = world.getBlockState(blockPos = pos.relative(BasicBathtubBlock.getDirectionTowardsOtherPart(bedPart, state.getValue(FACING))))).getBlock() == this && blockState.getValue(PART) == BedPart.HEAD) {
            world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 35);
            world.levelEvent(player, 2001, blockPos, Block.getId(blockState));
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        int i = state.getValue(LEVEL_8);
        if (!world.isClientSide && entity.isOnFire() && i != 0) {
            entity.clearFire();
            this.onFireCollision(state, world, pos);
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return state.getValue(LEVEL_8);
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
    public void handlePrecipitation(BlockState state, Level world, BlockPos pos, Biome.Precipitation precipitation) {
        if (!canFillWithPrecipitation(world, precipitation) || state.getValue(LEVEL_8) == 8 || this.precipitation != precipitation) {
            return;
        }
        world.setBlockAndUpdate(pos, state.cycle(LEVEL_8));
    }

    public static void decrementFluidLevel(BlockState state, Level world, BlockPos pos) {
        int i = state.getValue(LEVEL_8) - 1;
        if (i < 0) {
            i = 0;
        }
        world.setBlockAndUpdate(pos, state.setValue(LEVEL_8, i));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = PaladinFurnitureMod.getPFMConfig().doChairsFacePlayer() ? ctx.getHorizontalDirection() : ctx.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    }

    public float height;

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos sourcePos = pos.below().below();
        ItemStack itemStack = player.getItemInHand(hand);
        BathtubBehavior sinkBehavior = this.behaviorMap.get(itemStack.getItem());
        if (sinkBehavior != null && itemStack.getItem() != Items.AIR) {
            return sinkBehavior.interact(state, world, pos, player, hand, itemStack);
        }
        if (state.getValue(LEVEL_8) > 0 && player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty()) {
            world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            decrementFluidLevel(state, world, pos);
            return InteractionResult.SUCCESS;
        }
        if (state.getValue(LEVEL_8) < 8) {
            BlockState sourceState = world.getBlockState(sourcePos);
            if (sourceState.getFluidState().getType() == Fluids.WATER && !sourceState.getFluidState().isEmpty()) {
                if (sourceState.getProperties().contains(BlockStateProperties.WATERLOGGED)) {
                    world.setBlockAndUpdate(sourcePos, sourceState.setValue(BlockStateProperties.WATERLOGGED, false));
                }
                else {
                    world.setBlockAndUpdate(sourcePos, Blocks.AIR.defaultBlockState());
                }
                BlockPos headPos = pos;
                if (state.getValue(PART) != BedPart.HEAD) {
                  headPos = headPos.relative(getDirectionTowardsOtherPart(state.getValue(PART), state.getValue(FACING)));
                }
                BathtubBlockEntity blockEntity = (BathtubBlockEntity) world.getBlockEntity(headPos);
                if (blockEntity != null) {
                    blockEntity.setFilling(true);
                }
                BathtubBehavior.fillTub(world, pos, player, hand, player.getItemInHand(hand), state, SoundEvents.WATER_AMBIENT, false);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isNight() && world.dimensionType().bedWorks()) {
            super.useWithoutItem(state, world, pos, player, hit);
            return InteractionResult.SUCCESS;
        }
        return sit(state, world, pos, player, hit);
    }

    public InteractionResult sit(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {
            if (player.isSpectator() || player.isShiftKeyDown()) {
                return InteractionResult.PASS;
            }
            double pz;
            double px;
            px = pos.getX() + 0.5;
            pz = pos.getZ() + 0.5;
            double py = pos.getY() + this.height;

            List<ChairEntity> active = world.getEntitiesOfClass(ChairEntity.class, new AABB(pos), Entity::hasExactlyOnePlayerPassenger);
            if (!active.isEmpty())
                return InteractionResult.PASS;

            float yaw = state.getValue(FACING).getOpposite().toYRot();
            if (state.getValue(PART) == BedPart.FOOT) {
                yaw = state.getValue(FACING).toYRot();
            }
            ChairEntity entity = Entities.CHAIR.create(world, EntitySpawnReason.EVENT);
            entity.moveTo(px, py, pz, yaw, 0);
            entity.setNoGravity(true);
            entity.setSilent(true);
            entity.setInvisible(false);
            entity.setInvulnerable(true);
            entity.setNoAi(true);
            entity.setYHeadRot(yaw);
            entity.setYBodyRot(yaw);
            if (world.addFreshEntity(entity)) {
                player.startRiding(entity, true);
                player.setYHeadRot(yaw);
                entity.setYBodyRot(yaw);
                entity.setYHeadRot(yaw);
                player.awardStat(Statistics.USE_BATHTUB);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        BedPart tubPart = state.getValue(PART);
        if (direction == BasicBathtubBlock.getDirectionTowardsOtherPart(tubPart, state.getValue(FACING))) {
            if (neighborState.is(this) && neighborState.getValue(PART) != tubPart) {
                return state.setValue(LEVEL_8, neighborState.getValue(LEVEL_8)).setValue(OCCUPIED, neighborState.getValue(OCCUPIED));
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    private static final VoxelShape FOOT = Shapes.join(box(0, 0, 0,16, 11, 16),box(0,2,3,13, 11, 13), BooleanOp.ONLY_FIRST);
    private static final VoxelShape FOOT_NORTH = rotateShape(Direction.WEST, Direction.NORTH, FOOT);
    private static final VoxelShape FOOT_EAST = rotateShape(Direction.WEST, Direction.EAST, FOOT);
    private static final VoxelShape FOOT_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, FOOT);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        BedPart part = state.getValue(PART);
        if (part == BedPart.FOOT) {
            switch (facing) {
                case WEST: {
                    return FOOT;
                }
                case EAST: {
                    return FOOT_EAST;
                }
                case NORTH: {
                    return FOOT_NORTH;
                }
                default: {
                    return FOOT_SOUTH;
                }
            }
        } else {
            switch (facing) {
                case WEST: {
                    return FOOT_EAST;
                }
                case EAST: {
                    return FOOT;
                }
                case NORTH: {
                    return FOOT_SOUTH;
                }
                default: {
                    return FOOT_NORTH;
                }
            }
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTicketHelper(type, BlockEntities.BATHTUB_BLOCK_ENTITY, BathtubBlockEntity::tick);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BathtubBlockEntity(pos, state);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
    
    public static void spawnParticles(Direction facing, Level world, BlockPos pos) {
        if (world.isClientSide) {
            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            if (facing == Direction.EAST) {
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.76, y + 0.8, z + 0.5, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.76, y + 0.8, z + 0.5, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.76, y + 0.8, z + 0.5, 0.0, 0.0, 0.0);
            }
            else if (facing == Direction.SOUTH){
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 0.8, z + 0.76, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 0.8, z + 0.76, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 0.8, z + 0.76, 0.0, 0.0, 0.0);
            }
            else if (facing == Direction.NORTH){
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 0.8, z + 0.24, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 0.8, z + 0.24, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.5, y + 0.8, z + 0.24, 0.0, 0.0, 0.0);
            }
            else {
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.24, y + 0.8, z + 0.5, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.24, y + 0.8, z + 0.5, 0.0, 0.0, 0.0);
                world.addParticle(ParticleIDs.WATER_DROP, true, x + 0.24, y + 0.8, z + 0.5, 0.0, 0.0, 0.0);
            }
        }
    }

    public static final MapCodec<BasicBathtubBlock> CODEC = RecordCodecBuilder.mapCodec( (instance) -> instance.group(propertiesCodec(), BathtubBehavior.CODEC.fieldOf("behaviorMap").forGetter(basicBathtubBlock -> basicBathtubBlock.behaviorMap), Biome.Precipitation.CODEC.fieldOf("precipitation").forGetter(basicBathtubBlock -> basicBathtubBlock.precipitation)).apply(instance, BasicBathtubBlock::new));
    @Override
    public MapCodec<BedBlock> codec() {
        return (MapCodec<BedBlock>)(Object)CODEC;
    }
}
