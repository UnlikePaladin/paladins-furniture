package com.unlikepaladin.pfm.blocks.behavior;

import com.unlikepaladin.pfm.blocks.BasicBathtubBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.Predicate;

public interface BathtubBehavior {
    BathtubBehavior FILL_TUB_WITH_WATER = (state, world, pos, player, hand, stack) -> BathtubBehavior.fillTub(world, pos, player, hand, stack, state, SoundEvents.BUCKET_EMPTY, true);
    Map<Item, BathtubBehavior> TUB_BEHAVIOR = BathtubBehavior.createMap();

    static Object2ObjectOpenHashMap<Item, BathtubBehavior> createMap() {
        return (Object2ObjectOpenHashMap) Util.make(new Object2ObjectOpenHashMap(), (map) -> {
            map.defaultReturnValue(null);});
    }

    public InteractionResult interact(BlockState var1, Level var2, BlockPos var3, Player var4, InteractionHand var5, ItemStack var6);

    BathtubBehavior CLEAN_SHULKER_BOX = (state, world, pos, player, hand, stack) -> {
        if (state.getValue(BasicBathtubBlock.LEVEL_8) == 0) {
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
            BasicBathtubBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    };

    BathtubBehavior CLEAN_DYEABLE_ITEM = (state, world, pos, player, hand, stack) -> {
       if (state.getValue(BasicBathtubBlock.LEVEL_8) == 0) {
           return InteractionResult.PASS;
       }
        Item item = stack.getItem();
        if (!(item instanceof DyeableLeatherItem)) {
            return InteractionResult.PASS;
        }
        DyeableLeatherItem dyeableItem = (DyeableLeatherItem) item;
        if (!dyeableItem.hasCustomColor(stack)) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide) {
            dyeableItem.clearColor(stack);
            player.awardStat(Stats.CLEAN_ARMOR);
            BasicBathtubBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
};
    BathtubBehavior CLEAN_BANNER = (state, world, pos, player, hand, stack) -> {
        if (BannerBlockEntity.getPatternCount(stack) <= 0 || state.getValue(BasicBathtubBlock.LEVEL_8) == 0) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide) {
            ItemStack itemStack = stack.copy();
            itemStack.setCount(1);
            BannerBlockEntity.removeLastPattern(itemStack);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            if (stack.isEmpty()) {
                player.setItemInHand(hand, itemStack);
            } else if (player.inventory.add(itemStack) && player instanceof ServerPlayer) {
                ((ServerPlayer)player).refreshContainer(player.containerMenu);
            }  else {
                player.drop(itemStack, false);
            }
            player.awardStat(Stats.CLEAN_BANNER);
            BasicBathtubBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    };

    static InteractionResult fillTub(Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState state, SoundEvent soundEvent, boolean usedBucket) {
        if (!world.isClientSide) {
            player.awardStat(Statistics.BATHTUB_FILLED);
            if (usedBucket) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
                player.awardStat(Stats.ITEM_USED.get(item));
            }
            int newLevel = (world.getBlockState(pos).getValue(BasicBathtubBlock.LEVEL_8) + 4);
            if (newLevel >= 0 && newLevel <= 8)  {
                world.setBlockAndUpdate(pos, state.setValue(BasicBathtubBlock.LEVEL_8, newLevel));
            } else {
                world.setBlockAndUpdate(pos, state.setValue(BasicBathtubBlock.LEVEL_8, 8));
            }
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    static InteractionResult emptyTub(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, ItemStack output, Predicate<BlockState> predicate, SoundEvent soundEvent) {
        if (!predicate.test(state)) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide) {
            Item item = stack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, output));
            player.awardStat(Statistics.USE_BATHTUB);
            player.awardStat(Stats.ITEM_USED.get(item));
            int newLevel = (world.getBlockState(pos).getValue(BasicBathtubBlock.LEVEL_8) - 4);
            if (newLevel >= 0)  {
                world.setBlockAndUpdate(pos, state.setValue(BasicBathtubBlock.LEVEL_8, newLevel));
            } else {
                world.setBlockAndUpdate(pos, state.setValue(BasicBathtubBlock.LEVEL_8, 0));
            }
            world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    static void registerBucketBehavior(Map<Item, BathtubBehavior> behavior) {
        behavior.put(Items.WATER_BUCKET, FILL_TUB_WITH_WATER);
    }
    static void registerBehavior() {
        BathtubBehavior.registerBucketBehavior(TUB_BEHAVIOR);
        TUB_BEHAVIOR.put(Items.BUCKET, (state2, world, pos, player, hand, stack) -> BathtubBehavior.emptyTub(state2, world, pos, player, hand, stack, new ItemStack(Items.WATER_BUCKET), state -> state.getValue(BasicBathtubBlock.LEVEL_8) >= 4, SoundEvents.BUCKET_FILL));
        TUB_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClientSide) {
                if (state.getValue(BasicBathtubBlock.LEVEL_8) == 0) {
                    return InteractionResult.PASS;
                }
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                player.awardStat(Statistics.USE_BATHTUB);
                player.awardStat(Stats.ITEM_USED.get(item));
                BasicBathtubBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        });
        TUB_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (state.getValue(BasicBathtubBlock.LEVEL_8) == 8 || PotionUtils.getPotion(stack) != Potions.WATER) {
                return InteractionResult.PASS;
            }
            if (!world.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Statistics.USE_BATHTUB);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                world.setBlockAndUpdate(pos, state.cycle(BasicBathtubBlock.LEVEL_8));
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        });
        TUB_BEHAVIOR.put(Items.LEATHER_BOOTS, CLEAN_DYEABLE_ITEM);
        TUB_BEHAVIOR.put(Items.LEATHER_LEGGINGS, CLEAN_DYEABLE_ITEM);
        TUB_BEHAVIOR.put(Items.LEATHER_CHESTPLATE, CLEAN_DYEABLE_ITEM);
        TUB_BEHAVIOR.put(Items.LEATHER_HELMET, CLEAN_DYEABLE_ITEM);
        TUB_BEHAVIOR.put(Items.LEATHER_HORSE_ARMOR, CLEAN_DYEABLE_ITEM);
        TUB_BEHAVIOR.put(Items.WHITE_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.GRAY_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.BLACK_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.BLUE_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.BROWN_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.CYAN_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.GREEN_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.LIGHT_BLUE_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.LIGHT_GRAY_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.LIME_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.MAGENTA_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.ORANGE_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.PINK_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.PURPLE_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.RED_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.YELLOW_BANNER, CLEAN_BANNER);
        TUB_BEHAVIOR.put(Items.WHITE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.GRAY_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.BLACK_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.BLUE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.BROWN_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.CYAN_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.GREEN_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.LIGHT_BLUE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.LIGHT_GRAY_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.LIME_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.MAGENTA_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.ORANGE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.PINK_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.PURPLE_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.RED_SHULKER_BOX, CLEAN_SHULKER_BOX);
        TUB_BEHAVIOR.put(Items.YELLOW_SHULKER_BOX, CLEAN_SHULKER_BOX);
        }
    }
