package com.unlikepaladin.pfm.blocks.behavior;

import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Map;
import java.util.function.Predicate;

public interface SinkBehavior extends CauldronBehavior {

    SinkBehavior FILL_SINK_WITH_WATER = (state, world, pos, player, hand, stack) -> SinkBehavior.fillCauldron(world, pos, player, hand, stack, state.with(KitchenSinkBlock.LEVEL_4, 3), SoundEvents.ITEM_BUCKET_EMPTY);
    Map<Item, CauldronBehavior> WATER_SINK_BEHAVIOR = CauldronBehavior.createMap();
    CauldronBehavior CLEAN_SHULKER_BOX = (state, world, pos, player, hand, stack) -> {
        if (state.get(KitchenSinkBlock.LEVEL_4) == 0) {
            return ActionResult.PASS;
        }
        Block block = Block.getBlockFromItem(stack.getItem());
        if (!(block instanceof ShulkerBoxBlock)) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.SHULKER_BOX);
            if (stack.hasNbt()) {
                itemStack.setNbt(stack.getNbt().copy());
            }
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.CLEAN_SHULKER_BOX);
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return ActionResult.success(world.isClient);
    };

    CauldronBehavior CLEAN_DYEABLE_ITEM = (state, world, pos, player, hand, stack) -> {
       if (state.get(KitchenSinkBlock.LEVEL_4) == 0) {
           return ActionResult.PASS;
       }
        Item item = stack.getItem();
        if (!(item instanceof DyeableItem)) {
            return ActionResult.PASS;
        }
        DyeableItem dyeableItem = (DyeableItem)((Object)item);
        if (!dyeableItem.hasColor(stack)) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            dyeableItem.removeColor(stack);
            player.incrementStat(Stats.CLEAN_ARMOR);
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return ActionResult.success(world.isClient);
};
    CauldronBehavior CLEAN_BANNER = (state, world, pos, player, hand, stack) -> {
        if (BannerBlockEntity.getPatternCount(stack) <= 0 || state.get(KitchenSinkBlock.LEVEL_4) == 0) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            ItemStack itemStack = stack.copy();
            itemStack.setCount(1);
            BannerBlockEntity.loadFromItemStack(itemStack);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            if (stack.isEmpty()) {
                player.setStackInHand(hand, itemStack);
            } else if (player.getInventory().insertStack(itemStack)) {
                player.playerScreenHandler.syncState();
            } else {
                player.dropItem(itemStack, false);
            }
            player.incrementStat(Stats.CLEAN_BANNER);
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return ActionResult.success(world.isClient);
    };

    static ActionResult fillCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
        if (!world.isClient) {
            Item item = stack.getItem();
            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
            player.incrementStat(Statistics.SINK_FILLED);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.setBlockState(pos, state);
            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
        }
        return ActionResult.success(world.isClient);
    }

    static ActionResult emptyCauldron(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, ItemStack output, Predicate<BlockState> predicate, SoundEvent soundEvent) {
        if (!predicate.test(state)) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            Item item = stack.getItem();
            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, output));
            player.incrementStat(Statistics.USE_SINK);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.setBlockState(pos, state.with(KitchenSinkBlock.LEVEL_4, 0));
            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return ActionResult.success(world.isClient);
    }

    static void registerBucketBehavior(Map<Item, CauldronBehavior> behavior) {
        behavior.put(Items.WATER_BUCKET, FILL_SINK_WITH_WATER);
    }
    static void registerBehavior() {
        WATER_SINK_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (PotionUtil.getPotion(stack) != Potions.WATER) {
                return ActionResult.PASS;
            }
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Statistics.USE_SINK);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.setBlockState(pos, state.with(KitchenSinkBlock.LEVEL_4, state.get(KitchenSinkBlock.LEVEL_4) + 1));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ActionResult.success(world.isClient);
        });



        SinkBehavior.registerBucketBehavior(WATER_SINK_BEHAVIOR);
        WATER_SINK_BEHAVIOR.put(Items.BUCKET, (state2, world, pos, player, hand, stack) -> SinkBehavior.emptyCauldron(state2, world, pos, player, hand, stack, new ItemStack(Items.WATER_BUCKET), state -> state.get(KitchenSinkBlock.LEVEL_4) == 3, SoundEvents.ITEM_BUCKET_FILL));
        WATER_SINK_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                if (state.get(KitchenSinkBlock.LEVEL_4) == 0) {
                    return ActionResult.PASS;
                }
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                player.incrementStat(Statistics.USE_SINK);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                KitchenSinkBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ActionResult.success(world.isClient);
        });
        WATER_SINK_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (state.get(KitchenSinkBlock.LEVEL_4) == 3 || PotionUtil.getPotion(stack) != Potions.WATER) {
                return ActionResult.PASS;
            }
            if (!world.isClient) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Statistics.USE_SINK);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                world.setBlockState(pos, state.cycle(KitchenSinkBlock.LEVEL_4));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ActionResult.success(world.isClient);
        });
        WATER_SINK_BEHAVIOR.put(Items.LEATHER_BOOTS, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.put(Items.LEATHER_LEGGINGS, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.put(Items.LEATHER_CHESTPLATE, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.put(Items.LEATHER_HELMET, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.put(Items.LEATHER_HORSE_ARMOR, CLEAN_DYEABLE_ITEM);
        WATER_SINK_BEHAVIOR.put(Items.WHITE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.GRAY_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.BLACK_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.BLUE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.BROWN_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.CYAN_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.GREEN_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.LIGHT_BLUE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.LIGHT_GRAY_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.LIME_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.MAGENTA_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.ORANGE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.PINK_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.PURPLE_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.RED_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.YELLOW_BANNER, CLEAN_BANNER);
        WATER_SINK_BEHAVIOR.put(Items.WHITE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.GRAY_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.BLACK_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.BLUE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.BROWN_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.CYAN_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.GREEN_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.LIGHT_BLUE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.LIGHT_GRAY_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.LIME_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.MAGENTA_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.ORANGE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.PINK_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.PURPLE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.RED_SHULKER_BOX, CLEAN_SHULKER_BOX);
        WATER_SINK_BEHAVIOR.put(Items.YELLOW_SHULKER_BOX, CLEAN_SHULKER_BOX);
        }
    }
