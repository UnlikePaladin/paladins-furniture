package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.blocks.DyeableFurnitureBlock;
import com.unlikepaladin.pfm.blocks.blockentities.DyeableFurnitureBlockEntity;
import com.unlikepaladin.pfm.entity.DyeableFurnitureEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;

public class DyeKit extends Item {
    private final DyeColor color;

    public DyeKit(Properties settings, DyeColor color) {
        super(settings);
        this.color = color;
    }

    public DyeColor getColor() {
        return this.color;
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player playerEntity = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState blockState = level.getBlockState(blockPos);
        if (playerEntity.isShiftKeyDown() && stack.getItem() instanceof DyeKit) {
            boolean dyed;
            if(blockState.getBlock() instanceof DyeableFurnitureBlock) {
                level.playSound(null, blockPos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                String newBlock= blockState.getBlock().toString();
                newBlock = newBlock.replace(((DyeableFurnitureBlock) blockState.getBlock()).getPFMColor().toString(), getColor().toString()).replace("block.pfm.","").replace("Block{", "").replace("}", "");
                BlockState blockState1 = getStateWithProperties(Registry.BLOCK.get(new ResourceLocation(newBlock)), blockState);
                level.setBlock(blockPos, blockState1, 3);
                stack.shrink(1);
                dyed = true;
            }
            else if (level.getBlockEntity(blockPos) instanceof DyeableFurnitureBlockEntity<?>) {
                level.playSound(null, blockPos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                DyeableFurnitureBlockEntity<?> dyeableFurnitureBlockEntity = (DyeableFurnitureBlockEntity<?>) level.getBlockEntity(blockPos);
                dyeableFurnitureBlockEntity.setPFMColor(getColor());
                level.sendBlockUpdated(blockPos, blockState, blockState, 3);
                stack.shrink(1);
                dyed = true;
            } else {
                dyed = false;
            }

            if (dyed)
                return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    private static <T extends Comparable<T>> BlockState copyProperty(BlockState source, BlockState target, Property<T> property) {
        return (BlockState)target.setValue(property, source.getValue(property));
    }
    protected static BlockState getStateWithProperties(Block block, BlockState state) {
        BlockState blockState = block.defaultBlockState();
        for (Property<?> property : state.getBlock().getStateDefinition().getProperties()) {
            if (!blockState.hasProperty(property)) continue;
            blockState = copyProperty(state, blockState, property);
        }
        return blockState;
    }


    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof SheepEntity) {
            SheepEntity sheepEntity = (SheepEntity)entity;
            if (sheepEntity.isAlive() && !sheepEntity.isSheared() && sheepEntity.getColor() != ((DyeKit) stack.getItem()).getColor()) {
                sheepEntity.level.playSound(user, sheepEntity, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!user.level.isClientSide) {
                    sheepEntity.setColor(this.color);
                    stack.shrink(1);
                }

                return InteractionResult.sidedSuccess(user.level.isClientSide);
            }
        } else if (entity instanceof DyeableFurnitureEntity<?>) {
            if (((DyeableFurnitureEntity<?>) entity).getPFMColor() != getColor()){
                entity.level.playSound(user, entity, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!user.level.isClientSide) {
                    ((DyeableFurnitureEntity<?>) entity).setPFMColor(getColor());
                    stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(user.level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }
}
