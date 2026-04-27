package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.block.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSittableBlock extends HorizontalDirectionalBlock {
    private final BlockState baseBlockState;
    private final Block baseBlock;

    public AbstractSittableBlock(Properties settings) {
        super(settings.lightLevel((state) -> {return 0;}).emissiveRendering((blockstate, b, c) -> {return false;}));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));

        this.height = 0.36f;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = PaladinFurnitureMod.getPFMConfig().doChairsFacePlayer() ? ctx.getHorizontalDirection() : ctx.getHorizontalDirection().getOpposite();
            return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            if (state.getValue(BlockStateProperties.WATERLOGGED))
                world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    public float height;
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.CONSUME;
        }

        if (player.isSpectator() || player.isShiftKeyDown()) {
            return InteractionResult.FAIL;
        }

        List<ChairEntity> active = world.getEntitiesOfClass(ChairEntity.class, new AABB(pos), Entity::isVehicle);
        if (active == null)
            return InteractionResult.FAIL;

        List<Entity> hasPassenger = new ArrayList<>();
        active.forEach(chairEntity -> hasPassenger.add(chairEntity.getControllingPassenger()));
        if (hasPassenger.stream().anyMatch(entity -> entity instanceof Player)) {
            return InteractionResult.FAIL;
        }
        else if (!active.isEmpty()) {
            hasPassenger.forEach(Entity::stopRiding);
            return InteractionResult.SUCCESS;
        }

        if (sitEntity(world, pos, state, player) == InteractionResult.SUCCESS) {
            if (!(state.getBlock() instanceof BasicToiletBlock))
                player.awardStat(Statistics.CHAIR_USED);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }


    public InteractionResult sitEntity(Level world, BlockPos pos, BlockState state, Entity entityToSit) {
        double px;
        double pz;
        if (state.getBlock() instanceof BasicChairBlock) {
            Direction direction = state.getValue(FACING);
            if (state.getValue(BasicChairBlock.TUCKED)) {
                switch (direction) {
                    case EAST : {
                        px = pos.getX() + 0.1;
                        pz = pos.getZ() + 0.5;
                        break;
                    }
                    case WEST : {
                        px = pos.getX() + 0.9;
                        pz = pos.getZ() + 0.5;
                        break;
                    }
                    case SOUTH : {
                        px = pos.getX() + 0.5;
                        pz = pos.getZ() + 0.1;
                        break;
                    }
                    default : {
                        px = pos.getX() + 0.5;
                        pz = pos.getZ() + 0.9;
                        break;
                    }
                }
            }
            else {
                px =  pos.getX() + 0.5;
                pz = pos.getZ() + 0.5;
            }
        }
        else {
            px =  pos.getX() + 0.5;
            pz = pos.getZ() + 0.5;
        }
        double py = pos.getY() + this.height;
        float yaw = state.getValue(FACING).getOpposite().toYRot();
        ChairEntity chairEntity = Entities.CHAIR.create(world);
        chairEntity.moveTo(px, py, pz, yaw, 0);
        chairEntity.setNoGravity(true);
        chairEntity.setSilent(true);
        chairEntity.setInvisible(false);
        chairEntity.setInvulnerable(true);
        chairEntity.setNoAi(true);
        if (world.addFreshEntity(chairEntity)) {
            entityToSit.startRiding(chairEntity, true);
            entityToSit.setYHeadRot(yaw);
            chairEntity.setYBodyRot(yaw);
            chairEntity.setYHeadRot(yaw);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        super.entityInside(state, world, pos, entity);
        List<ChairEntity> active = world.getEntitiesOfClass(ChairEntity.class, new AABB(pos), Entity::isVehicle);
        if (active == null || !active.isEmpty())
            return;

        if (entity instanceof Player || entity instanceof IronGolem || entity instanceof AbstractMinecart || entity.isPassenger() || !(entity instanceof LivingEntity) || entity instanceof ChairEntity) {
            return;
        }
        if (!PaladinFurnitureMod.getPFMConfig().doMobsSitOnChairs())
            return;

        sitEntity(world, pos, state, entity);
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}

