package com.unlikepaladin.pfm.blocks.behavior;

import com.unlikepaladin.pfm.blocks.AbstractSinkBlock;
import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;
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
import java.util.Map;
import java.util.function.Predicate;

public interface SinkBehavior {

    SinkBehavior FILL_SINK_WITH_WATER = (state, world, pos, player, hand, stack) -> SinkBehavior.fillCauldron(world, pos, player, hand, stack, state.setValue(AbstractSinkBlock.LEVEL_4, 3), SoundEvents.BUCKET_EMPTY);
    Map<Item, SinkBehavior> WATER_SINK_BEHAVIOR = SinkBehavior.createMap();


    static Object2ObjectOpenHashMap<Item, SinkBehavior> createMap() {
        return (Object2ObjectOpenHashMap) Util.make(new Object2ObjectOpenHashMap(), (map) -> {
            map.defaultReturnValue(null);});
    }

    public InteractionResult interact(BlockState var1, Level var2, BlockPos var3, Player var4, InteractionHand var5, ItemStack var6);

    SinkBehavior CLEAN_SHULKER_BOX = (state, world, pos, player, hand, stack) -> {
        if (state.getValue(AbstractSinkBlock.LEVEL_4) == 0) {
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

    SinkBehavior CLEAN_DYEABLE_ITEM = (state, world, pos, player, hand, stack) -> {
       if (state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
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
            KitchenSinkBlock.decrementFluidLevel(state, world, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
};
    SinkBehavior CLEAN_BANNER = (state, world, pos, player, hand, stack) -> {
        if (BannerBlockEntity.getPatternCount(stack) <= 0 || state.getValue(KitchenSinkBlock.LEVEL_4) == 0) {
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
            } else if (player.inventory.add(itemStack)) {
                ((ServerPlayer)player).refreshContainer(player.containerMenu);
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
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    static void registerBucketBehavior(Map<Item, SinkBehavior> behavior) {
        behavior.put(Items.WATER_BUCKET, FILL_SINK_WITH_WATER);
    }
    static void registerBehavior() {
        WATER_SINK_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
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
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        });



        SinkBehavior.registerBucketBehavior(WATER_SINK_BEHAVIOR);
        WATER_SINK_BEHAVIOR.put(Items.BUCKET, (state2, world, pos, player, hand, stack) -> SinkBehavior.emptyCauldron(state2, world, pos, player, hand, stack, new ItemStack(Items.WATER_BUCKET), state -> state.getValue(KitchenSinkBlock.LEVEL_4) == 3, SoundEvents.BUCKET_FILL));
        WATER_SINK_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
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
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        });
        WATER_SINK_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (state.getValue(KitchenSinkBlock.LEVEL_4) == 3 || PotionUtils.getPotion(stack) != Potions.WATER) {
                return InteractionResult.PASS;
            }
            if (!world.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Statistics.USE_SINK);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                world.setBlockAndUpdate(pos, state.cycle(KitchenSinkBlock.LEVEL_4));
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
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
