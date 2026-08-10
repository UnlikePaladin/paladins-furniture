package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.ToiletBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.SoundIDs;
import com.unlikepaladin.pfm.registry.Statistics;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

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
import java.util.function.Function;
import java.util.stream.Stream;

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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            player.awardStat(Statistics.TOILET_USED);
        }

        if (!world.isClientSide && state.getValue(TOILET_STATE) == ToiletState.EMPTY && (stack.getItem() == Items.POTION) && stack.has(DataComponents.POTION_CONTENTS) && stack.get(DataComponents.POTION_CONTENTS) != null && stack.get(DataComponents.POTION_CONTENTS).is(Potions.WATER)) {
            world.setBlockAndUpdate(pos, state.setValue(TOILET_STATE, ToiletState.CLEAN));
            if (!player.getAbilities().instabuild)
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            return ItemInteractionResult.SUCCESS;
        } else if (!world.isClientSide && state.getValue(TOILET_STATE) == ToiletState.EMPTY && (player.getItemInHand(hand).getItem() == Items.WATER_BUCKET)) {
            world.setBlockAndUpdate(pos, state.setValue(TOILET_STATE, ToiletState.CLEAN));
            if (!player.getAbilities().instabuild)
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
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
                player.displayClientMessage(Component.translatable("message.pfm.toilet_use"), false);
            }
        }
        else if (!world.isClientSide && (state.getValue(TOILET_STATE) == ToiletState.DIRTY)) {
            world.setBlockAndUpdate(pos, state.setValue(TOILET_STATE, ToiletState.FLUSHING));
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundIDs.TOILET_FLUSHING_EVENT, SoundSource.BLOCKS, 0.3f, 1.0f);
            ToiletBlockEntity blockEntity = (ToiletBlockEntity) world.getBlockEntity(pos);
            blockEntity.setFlushTimer(0);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public Function<Properties, AbstractSittableBlock> getChairConstructor() {
        return BasicToiletBlock::new;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        super.animateTick(state, world, pos, random);
        if(state.getValue(TOILET_STATE) != ToiletState.DIRTY) {
            return;
        }
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        if (random.nextDouble() < 0.2)
            world.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1), x, y + 0.6, z, 0.8, 1.0, 0.0);
        if (random.nextDouble() < 0.009) {
            world.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1), x, y + 0.6, z, 0.18, 0.0, 0.34);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ToiletBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTicketHelper(type, BlockEntities.TOILET_BLOCK_ENTITY, ToiletBlockEntity::tick);
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
        world.setBlock(pos, state.setValue(BasicToiletBlock.TOILET_STATE, ToiletState.EMPTY), UPDATE_ALL);
    }
    protected static final VoxelShape TOILET_WEST = Shapes.or(box(2, 1, 4.2,14, 6, 11.7),box(1, 0, 3.2,15, 1, 12.7),box(5, 5, 2.5,16, 10, 13.5),box(0, 6, 2.5,5, 20, 13.5),box(5, 8, 3.5,6, 21, 12.5));
    protected static final VoxelShape TOILET_NORTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.NORTH, TOILET_WEST);
    protected static final VoxelShape TOILET_EAST = PFMShapeUtil.rotateShape(Direction.WEST, Direction.EAST, TOILET_WEST);
    protected static final VoxelShape TOILET_SOUTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.SOUTH, TOILET_WEST);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        return switch (dir){
            case NORTH -> TOILET_NORTH;
            case SOUTH -> TOILET_SOUTH;
            case EAST -> TOILET_EAST;
            default -> TOILET_WEST;
        };
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTicketHelper(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

}

