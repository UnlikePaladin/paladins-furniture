package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.function.Predicate;

public class KitchenSink extends AbstractCauldronBlock {
    private final BlockState baseBlockState;
    private final Block baseBlock;
    private final Predicate<Biome.Precipitation> precipitationPredicate;
    public static final IntProperty LEVEL_4 = IntProperty.of("level", 0, 3);

    public KitchenSink(Settings settings,  Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronBehavior> map) {
        super(settings, map);
        this.setDefaultState(this.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(LEVEL_4, 0));
        this.baseBlockState = this.getDefaultState();
        this.precipitationPredicate = precipitationPredicate;
        this.baseBlock = baseBlockState.getBlock();
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
      //  super.appendProperties(stateManager);
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(LEVEL_4);

    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing());
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockPos blockPos = pos.down().down();
        if (state.get(LEVEL_4) <= 3) {
            BlockState source = world.getBlockState(blockPos);
            if (source.getFluidState().getFluid() == Fluids.WATER && !source.getFluidState().isEmpty()) {
                if (source.getProperties().contains(Properties.WATERLOGGED)) {
                world.setBlockState(blockPos, source.with(Properties.WATERLOGGED, false)); }
                else {
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                }
                world.setBlockState(pos, state.with(LEVEL_4, 3));
            }
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity.isOnFire() && this.isEntityTouchingFluid(state, pos, entity)) {
            entity.extinguish();
            if (entity.canModifyAt(world, pos)) {
                this.onFireCollision(state, world, pos);
            }
        }
    }

    protected void onFireCollision(BlockState state, World world, BlockPos pos) {
        KitchenSink.decrementFluidLevel(state, world, pos);
    }

    public static void decrementFluidLevel(BlockState state, World world, BlockPos pos) {
        int i = state.get(LEVEL_4) - 1;
        world.setBlockState(pos, state.with(LEVEL_4, i));
    }


    @Override
    public boolean isFull(BlockState state) {
        return state.get(LEVEL_4) == 3;
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return fluid == Fluids.WATER && this.precipitationPredicate == LeveledCauldronBlock.RAIN_PREDICATE;
    }

    @Override
    protected double getFluidHeight(BlockState state) {
        return (6.0 + (double)state.get(LEVEL_4).intValue() * 3.0) / 16.0;
    }




    protected static boolean canFillWithPrecipitation(World world, Biome.Precipitation precipitation) {
        if (precipitation == Biome.Precipitation.RAIN) {
            return world.getRandom().nextFloat() < 0.05f;
        }
        if (precipitation == Biome.Precipitation.SNOW) {
            return world.getRandom().nextFloat() < 0.1f;
        }
        return false;
    }

    @Override
    public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        if (!canFillWithPrecipitation(world, precipitation) || state.get(LEVEL_4) == 3 || !this.precipitationPredicate.test(precipitation)) {
            return;
        }
        world.setBlockState(pos, state.cycle(LEVEL_4));
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LEVEL_4);
    }


    @Override
    protected void fillFromDripstone(BlockState state, World world, BlockPos pos, Fluid fluid) {
        if (this.isFull(state)) {
            return;
        }
        world.setBlockState(pos, (BlockState)state.with(LEVEL_4, state.get(LEVEL_4) + 1));
        world.syncWorldEvent(WorldEvents.POINTED_DRIPSTONE_DRIPS_WATER_INTO_CAULDRON, pos, 0);
    }
}
