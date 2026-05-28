package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.LightSwitchItem;
import com.unlikepaladin.pfm.items.PFMComponents;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BasicLampBlock extends PowerableBlock implements EntityBlock {
    private static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final MapCodec<BasicLampBlock> CODEC = simpleCodec(BasicLampBlock::new);

    public BasicLampBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(LIT, false).setValue(POWERLOCKED, false));
    }

    @Override
    protected MapCodec<? extends PowerableBlock> codec() {
        return CODEC;
    }

    @Override
    public void setPowered(Level world, BlockPos lightPos, boolean powered) {
        BlockState state = world.getBlockState(lightPos);
        world.setBlockAndUpdate(lightPos, state.setValue(LIT, powered).setValue(POWERLOCKED,powered));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LampBlockEntity) {
            DyeColor color = itemStack.getOrDefault(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE);
            WoodVariant variant = WoodVariantRegistry.getVariant(itemStack.getOrDefault(PFMComponents.VARIANT_COMPONENT, WoodVariantRegistry.OAK.identifier));
            ((LampBlockEntity) blockEntity).setPFMColor(color);
            ((LampBlockEntity) blockEntity).setVariant(variant);
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(LIT);
        stateManager.add(POWERLOCKED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntity(pos, state);
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public MapColor defaultMapColor() {
        return super.defaultMapColor();
    }

    private static final VoxelShape SINGLE = Shapes.or(box(7, 1.5, 7, 9, 6, 9), box(3, 0, 3,13, 1.5, 13),box(1.5, 5, 1.5,14.5, 16, 14.5));
    private static final VoxelShape TOP = Shapes.or(box(7, 0, 7,9, 6, 9),box(1.5, 5, 1.5,14.5, 16, 14.5));
    private static final VoxelShape MIDDLE = Shapes.or(box(7, 0, 7,9, 16, 9));
    private static final VoxelShape BOTTOM = Shapes.or(box(7, 1.5, 7, 9, 16, 9), box(3, 0, 3,13, 1.5, 13));
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        boolean up = world.getBlockState(pos.above()).getBlock() instanceof BasicLampBlock;
        boolean down = world.getBlockState(pos.below()).getBlock() instanceof BasicLampBlock;
        if (up && down) {
            return MIDDLE;
        } else if (up) {
            return BOTTOM;
        } else if (down)
        {
            return TOP;
        }
        return SINGLE;
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof LightSwitchItem)
            return InteractionResult.PASS;

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!state.getValue(POWERLOCKED)) {
            togglePower(state, world, pos);
            float f = state.getValue(LIT) ? 0.9f : 0.8f;
            world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, f);
            world.gameEvent(player, state.getValue(LIT) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
            return InteractionResult.CONSUME;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    public BlockState togglePower(BlockState state, Level world, BlockPos pos) {
        state = state.cycle(LIT);
        world.setBlock(pos, state, Block.UPDATE_ALL);
        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return direction.getAxis().isVertical() && neighborState.getBlock() instanceof BasicLampBlock ? state.setValue(LIT, neighborState.getValue(LIT)).setValue(POWERLOCKED, neighborState.getValue(POWERLOCKED)) : super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack stack = super.getCloneItemStack(world, pos, state, includeData);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LampBlockEntity) {
            stack.set(PFMComponents.VARIANT_COMPONENT, ((LampBlockEntity) blockEntity).getVariant().identifier);
            stack.set(PFMComponents.COLOR_COMPONENT, ((LampBlockEntity) blockEntity).getPFMColor());
        }
        return stack;
    }
}
