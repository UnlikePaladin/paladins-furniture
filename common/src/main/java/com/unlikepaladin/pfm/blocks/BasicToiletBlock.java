package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.ToiletBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.SoundIDs;
import com.unlikepaladin.pfm.registry.Statistics;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.DinnerTableBlock.rotateShape;

public class BasicToiletBlock extends AbstractSittableBlock implements EntityBlock {
    private static final List<FurnitureBlock> BASIC_TOILET = new ArrayList<>();
    public static final EnumProperty<ToiletState> TOILET_STATE = EnumProperty.create("toilet", ToiletState.class);
    public BasicToiletBlock(BlockBehaviour.Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(TOILET_STATE, ToiletState.EMPTY));
        if(this.getClass().isAssignableFrom(BasicToiletBlock.class)){
            BASIC_TOILET.add(new FurnitureBlock(this, "basic_toilet"));
        }
    }
    public static Stream<FurnitureBlock> streamBasicToilet() {
        return BASIC_TOILET.stream();
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(TOILET_STATE);
        super.createBlockStateDefinition(stateManager);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            player.awardStat(Statistics.TOILET_USED);
        }
        if (!world.isClientSide && state.getValue(TOILET_STATE) == ToiletState.EMPTY && (player.getItemInHand(hand).getItem() == Items.POTION) && PotionUtils.getPotion(player.getItemInHand(hand)) == Potions.WATER) {
            world.setBlockAndUpdate(pos, state.setValue(TOILET_STATE, ToiletState.CLEAN));
            if (!player.isCreative())
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
             world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        else if (!world.isClientSide && state.getValue(TOILET_STATE) == ToiletState.EMPTY && (player.getItemInHand(hand).getItem() == Items.WATER_BUCKET)) {
            world.setBlockAndUpdate(pos, state.setValue(TOILET_STATE, ToiletState.CLEAN));
            if (!player.isCreative())
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        if (!world.isClientSide && state.getValue(TOILET_STATE) == ToiletState.EMPTY) {
            BlockPos sourcePos = pos.below().below();
            BlockState sourceState = world.getBlockState(sourcePos);
            if (sourceState.getFluidState().getType() == Fluids.WATER && !sourceState.getFluidState().isEmpty()) {
                if (sourceState.getProperties().contains(BlockStateProperties.WATERLOGGED)) {
                    world.setBlockAndUpdate(sourcePos, sourceState.setValue(BlockStateProperties.WATERLOGGED, false)); }
                else {
                    world.setBlockAndUpdate(sourcePos, Blocks.AIR.defaultBlockState());
                }
                world.setBlockAndUpdate(pos, state.setValue(TOILET_STATE, ToiletState.CLEAN));
                return InteractionResult.SUCCESS;
            } else if (world.getEntitiesOfClass(ChairEntity.class, new AABB(pos), Entity::isVehicle).isEmpty()) {
                player.displayClientMessage(new TranslatableComponent("message.pfm.toilet_use"), false);
            }
        }
        else if (!world.isClientSide && (state.getValue(TOILET_STATE) == ToiletState.DIRTY)) {
            world.setBlockAndUpdate(pos, state.setValue(TOILET_STATE, ToiletState.FLUSHING));
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundIDs.TOILET_FLUSHING_EVENT, SoundSource.BLOCKS, 0.3f, 1.0f);
            ToiletBlockEntity blockEntity = (ToiletBlockEntity) world.getBlockEntity(pos);
            blockEntity.setFlushTimer(0);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        super.animateTick(state, world, pos, random);
        if(state.getValue(TOILET_STATE) != ToiletState.DIRTY) {
            return;
        }
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        if (random.nextDouble() < 0.2)
            world.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, x, y + 0.6, z, 0.8, 1.0, 0.0);
        if (random.nextDouble() < 0.009) {
            world.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, x, y + 0.6, z, 0.18, 0.0, 0.34);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockGetter world) {
        return new ToiletBlockEntity();
    }


    public static void setClean(BlockState state, Level world, BlockPos pos) {
        BlockPos sourcePos = pos.below().below();
        BlockState sourceState = world.getBlockState(sourcePos);
        if (sourceState.getFluidState().getType() == Fluids.WATER && !sourceState.getFluidState().isEmpty()) {
            if (sourceState.getProperties().contains(BlockStateProperties.WATERLOGGED)) {
                world.setBlockAndUpdate(sourcePos, sourceState.setValue(BlockStateProperties.WATERLOGGED, false)); }
            else {
                world.setBlockAndUpdate(sourcePos, Blocks.AIR.defaultBlockState());
            }
            world.setBlockAndUpdate(pos, state.setValue(TOILET_STATE, ToiletState.CLEAN));
            return;
        }
        world.setBlock(pos, state.setValue(BasicToiletBlock.TOILET_STATE, ToiletState.EMPTY), 3);
    }
    protected static final VoxelShape TOILET_WEST = Shapes.or(box(2, 1, 4.2,14, 6, 11.7),box(1, 0, 3.2,15, 1, 12.7),box(5, 5, 2.5,16, 10, 13.5),box(0, 6, 2.5,5, 20, 13.5),box(5, 8, 3.5,6, 21, 12.5));
    protected static final VoxelShape TOILET_NORTH = rotateShape(Direction.WEST, Direction.NORTH, TOILET_WEST);
    protected static final VoxelShape TOILET_EAST = rotateShape(Direction.WEST, Direction.EAST, TOILET_WEST);
    protected static final VoxelShape TOILET_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, TOILET_WEST);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        switch (dir){
            case NORTH:  return TOILET_NORTH;
            case SOUTH:  return TOILET_SOUTH;
            case EAST:  return TOILET_EAST;
            default:  return TOILET_WEST;
        }
    }
}

