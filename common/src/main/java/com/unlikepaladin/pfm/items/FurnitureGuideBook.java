package com.unlikepaladin.pfm.items;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import java.util.List;

public class FurnitureGuideBook extends Item {
    public FurnitureGuideBook(Item.Properties settings) {
        super(settings);
    }
    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        return openBook(world, user, hand);
    }
    @ExpectPlatform
    public static InteractionResult openBook(Level world, Player user, InteractionHand hand) {
        throw new AssertionError();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("pfm.patchouli.guide_book.subtitle"));
        super.appendHoverText(stack, context, tooltip, type);
    }
}
