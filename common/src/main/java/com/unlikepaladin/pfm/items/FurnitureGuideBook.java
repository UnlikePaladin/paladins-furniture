package com.unlikepaladin.pfm.items;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FurnitureGuideBook extends Item {
    public FurnitureGuideBook(Item.Properties settings) {
        super(settings);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        return openBook(world, user, hand);
    }
    @ExpectPlatform
    public static InteractionResultHolder<ItemStack> openBook(Level world, Player user, InteractionHand hand) {
        throw new AssertionError();
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(new TranslatableComponent("pfm.patchouli.guide_book.subtitle"));
        super.appendHoverText(stack, world, tooltip, context);
    }
}
