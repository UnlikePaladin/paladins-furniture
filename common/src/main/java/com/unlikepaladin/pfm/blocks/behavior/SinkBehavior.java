package com.unlikepaladin.pfm.blocks.behavior;

import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Prediction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;
import java.util.function.Predicate;

public interface SinkBehavior extends CauldronInteraction {

    SinkBehavior FILL_SINK_WITH_WATER = (state, world, pos, player, hand, stack) -> SinkBehavior.fillCauldron(world, pos, player, hand, stack, state.setValue(KitchenSinkBlock.LEVEL_4, 3), SoundEvents.BUCKET_EMPTY);
    Dispatcher WATER_SINK_BEHAVIOR = CauldronInteractions.newDispatcher("sink");
    CauldronInteraction CLEAN_SHULKER_BOX = (state, world, pos, player, hand, stack) -> {
        if (state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        Block block = Block.byItem(stack.getItem());
        if (!(block instanceof ShulkerBoxBlock)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!world.isClientSide()) {
            player.setItemInHand(hand, stack.transmuteCopy(Blocks.SHULKER_BOX, 1));
            player.awardStat(Stats.CLEAN_SHULKER_BOX);
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.SUCCESS;
    };

    CauldronInteraction CLEAN_DYEABLE_ITEM = (state, world, pos, player, hand, stack) -> {
       if (state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
           return InteractionResult.TRY_WITH_EMPTY_HAND;
       }
        if (!stack.has(DataComponents.DYED_COLOR)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!world.isClientSide()) {
            stack.remove(DataComponents.DYED_COLOR);
            player.awardStat(Stats.CLEAN_ARMOR);
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.SUCCESS;
};
    CauldronInteraction CLEAN_BANNER = (state, world, pos, player, hand, stack) -> {
        BannerPatternLayers bannerPatternsComponent = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        if (bannerPatternsComponent.layers().isEmpty() || state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!world.isClientSide()) {
            ItemStack itemStack = stack.copyWithCount(1);
            itemStack.set(DataComponents.BANNER_PATTERNS, bannerPatternsComponent.removeLast());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (stack.isEmpty()) {
                player.setItemInHand(hand, itemStack);
            } else if (player.getInventory().add(itemStack)) {
                player.inventoryMenu.sendAllDataToRemote();
            } else {
                player.drop(itemStack, false, Prediction.PREDICTED);
            }
            player.awardStat(Stats.CLEAN_BANNER);
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.SUCCESS;
    };

    static InteractionResult fillCauldron(Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
        if (!world.isClientSide()) {
            Item item = stack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
            player.awardStat(Statistics.SINK_FILLED);
            player.awardStat(Stats.ITEM_USED.get(item));
            world.setBlockAndUpdate(pos, state);
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }
        return InteractionResult.SUCCESS;
    }

    static InteractionResult emptyCauldron(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, ItemStack output, Predicate<BlockState> predicate, SoundEvent soundEvent) {
        if (!predicate.test(state)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!world.isClientSide()) {
            Item item = stack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, output));
            player.awardStat(Statistics.USE_SINK);
            player.awardStat(Stats.ITEM_USED.get(item));
            world.setBlockAndUpdate(pos, state.setValue(KitchenSinkBlock.LEVEL_4, 0));
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return InteractionResult.SUCCESS;
    }

    static void registerBucketBehavior(Dispatcher behavior) {
        behavior.put(Items.WATER_BUCKET, FILL_SINK_WITH_WATER);
    }
    static void registerBehavior() {
        WATER_SINK_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            PotionContents potionContentsComponent = stack.get(DataComponents.POTION_CONTENTS);
            if (potionContentsComponent != null && !potionContentsComponent.is(Potions.WATER)) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
            if (!world.isClientSide()) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Statistics.USE_SINK);
                player.awardStat(Stats.ITEM_USED.get(item));
                world.setBlockAndUpdate(pos, state.setValue(KitchenSinkBlock.LEVEL_4, state.getValue(KitchenSinkBlock.LEVEL_4) + 1));
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.SUCCESS;
        });



        SinkBehavior.registerBucketBehavior(WATER_SINK_BEHAVIOR);
        WATER_SINK_BEHAVIOR.put(Items.BUCKET, (state2, world, pos, player, hand, stack) -> SinkBehavior.emptyCauldron(state2, world, pos, player, hand, stack, new ItemStack(Items.WATER_BUCKET), state -> state.getValue(KitchenSinkBlock.LEVEL_4) == 3, SoundEvents.BUCKET_FILL));
        WATER_SINK_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClientSide()) {
                if (state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                }
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, ItemUtils.createFilledResult(stack, player, PotionContents.createItemStack(Items.POTION, Potions.WATER))));
                player.awardStat(Statistics.USE_SINK);
                player.awardStat(Stats.ITEM_USED.get(item));
                KitchenSinkBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return InteractionResult.SUCCESS;
        });
        WATER_SINK_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            PotionContents potionContentsComponent = stack.get(DataComponents.POTION_CONTENTS);
            if (state.getValue(KitchenSinkBlock.LEVEL_4) == 3 || potionContentsComponent != null && !potionContentsComponent.is(Potions.WATER)) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
            if (!world.isClientSide()) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Statistics.USE_SINK);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                world.setBlockAndUpdate(pos, state.cycle(KitchenSinkBlock.LEVEL_4));
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.SUCCESS;
        });
        WATER_SINK_BEHAVIOR.put(ItemTags.CAULDRON_CAN_REMOVE_DYE, CLEAN_DYEABLE_ITEM);
        Items.BANNER.forEach(banner -> WATER_SINK_BEHAVIOR.put(banner, CLEAN_BANNER));
        Items.DYED_SHULKER_BOX.forEach(shulkerBox -> WATER_SINK_BEHAVIOR.put(shulkerBox, CLEAN_SHULKER_BOX));
        }
    }
