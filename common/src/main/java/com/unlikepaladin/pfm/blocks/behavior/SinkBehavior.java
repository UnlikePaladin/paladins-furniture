package com.unlikepaladin.pfm.blocks.behavior;

import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;
import java.util.function.Predicate;

public interface SinkBehavior extends CauldronInteraction {

    SinkBehavior FILL_SINK_WITH_WATER = (state, world, pos, player, hand, stack) -> SinkBehavior.fillCauldron(world, pos, player, hand, stack, state.setValue(KitchenSinkBlock.LEVEL_4, 3), SoundEvents.BUCKET_EMPTY);
    CauldronBehaviorMap WATER_SINK_BEHAVIOR = CauldronBehaviorMap.createMap("sink");
    CauldronBehaviorMap CLEAN_SHULKER_BOX = (state, world, pos, player, hand, stack) -> {
        if (state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
            return InteractionResult.PASS;
        }
        Block block = Block.byItem(stack.getItem());
        if (!(block instanceof ShulkerBoxBlock)) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide) {
            ItemStack itemStack = new ItemStack(Blocks.SHULKER_BOX);
            if (stack.hasTag()) {
                itemStack.setTag(stack.getTag().copy());
            }
            player.setItemInHand(hand, itemStack);
            player.awardStat(Stats.CLEAN_SHULKER_BOX);
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    };

    CauldronInteraction CLEAN_DYEABLE_ITEM = (state, world, pos, player, hand, stack) -> {
       if (state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
           return InteractionResult.PASS;
       }
        Item item = stack.getItem();
        if (!(item instanceof DyeableLeatherItem)) {
            return InteractionResult.PASS;
        }
        DyeableLeatherItem dyeableItem = (DyeableLeatherItem)((Object)item);
        if (!dyeableItem.hasCustomColor(stack)) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide) {
            dyeableItem.clearColor(stack);
            player.awardStat(Stats.CLEAN_ARMOR);
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
};
    CauldronInteraction CLEAN_BANNER = (state, world, pos, player, hand, stack) -> {
        if (BannerBlockEntity.getPatternCount(stack) <= 0 || state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide) {
            ItemStack itemStack = stack.copy();
            itemStack.setCount(1);
            BannerBlockEntity.removeLastPattern(itemStack);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (stack.isEmpty()) {
                player.setItemInHand(hand, itemStack);
            } else if (player.getInventory().add(itemStack)) {
                player.inventoryMenu.sendAllDataToRemote();
            } else {
                player.drop(itemStack, false);
            }
            player.awardStat(Stats.CLEAN_BANNER);
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    };

    static InteractionResult fillCauldron(Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
        if (!world.isClientSide) {
            Item item = stack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
            player.awardStat(Statistics.SINK_FILLED);
            player.awardStat(Stats.ITEM_USED.get(item));
            world.setBlockAndUpdate(pos, state);
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    static InteractionResult emptyCauldron(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, ItemStack output, Predicate<BlockState> predicate, SoundEvent soundEvent) {
        if (!predicate.test(state)) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide) {
            Item item = stack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, output));
            player.awardStat(Statistics.USE_SINK);
            player.awardStat(Stats.ITEM_USED.get(item));
            world.setBlockAndUpdate(pos, state.setValue(KitchenSinkBlock.LEVEL_4, 0));
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    static void registerBucketBehavior(Map<Item, CauldronInteraction> behavior) {
        behavior.put(Items.WATER_BUCKET, FILL_SINK_WITH_WATER);
    }
    static void registerBehavior() {
        WATER_SINK_BEHAVIOR.map().put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (PotionUtils.getPotion(stack) != Potions.WATER) {
                return InteractionResult.PASS;
            }
            if (!world.isClientSide) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Statistics.USE_SINK);
                player.awardStat(Stats.ITEM_USED.get(item));
                world.setBlockAndUpdate(pos, state.setValue(KitchenSinkBlock.LEVEL_4, state.getValue(KitchenSinkBlock.LEVEL_4) + 1));
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        });



        SinkBehavior.registerBucketBehavior(WATER_SINK_BEHAVIOR.map());
        WATER_SINK_BEHAVIOR.map().put(Items.BUCKET, (state2, world, pos, player, hand, stack) -> SinkBehavior.emptyCauldron(state2, world, pos, player, hand, stack, new ItemStack(Items.WATER_BUCKET), state -> state.getValue(KitchenSinkBlock.LEVEL_4) == 3, SoundEvents.BUCKET_FILL));
        WATER_SINK_BEHAVIOR.map().put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClientSide) {
                if (state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
                    return InteractionResult.PASS;
                }
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                player.awardStat(Statistics.USE_SINK);
                player.awardStat(Stats.ITEM_USED.get(item));
                KitchenSinkBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        });
        WATER_SINK_BEHAVIOR.map().put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (state.getValue(KitchenSinkBlock.LEVEL_4) == 3 || PotionUtils.getPotion(stack) != Potions.WATER) {
                return InteractionResult.PASS;
            }
            if (!world.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Statistics.USE_SINK);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                world.setBlockAndUpdate(pos, state.cycle(KitchenSinkBlock.LEVEL_4));
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        });
        WATER_SINK_BEHAVIOR.map().put(Items.LEATHER_BOOTS, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.map().put(Items.LEATHER_LEGGINGS, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.map().put(Items.LEATHER_CHESTPLATE, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.map().put(Items.LEATHER_HELMET, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.map().put(Items.LEATHER_HORSE_ARMOR, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.map().put(Items.WHITE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.GRAY_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.BLACK_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.BLUE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.BROWN_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.CYAN_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.GREEN_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.LIGHT_BLUE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.LIGHT_GRAY_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.LIME_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.MAGENTA_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.ORANGE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.PINK_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.PURPLE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.RED_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.YELLOW_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.map().put(Items.WHITE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.GRAY_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.BLACK_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.BLUE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.BROWN_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.CYAN_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.GREEN_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.LIGHT_BLUE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.LIGHT_GRAY_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.LIME_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.MAGENTA_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.ORANGE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.PINK_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.PURPLE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.RED_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.map().put(Items.YELLOW_SHULKER_BOX, CLEAN_SHULKER_BOX);
        }
    }
