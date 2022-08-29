package com.unlikepaladin.pfm.items;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FurnitureGuideBook extends Item {
    public FurnitureGuideBook(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return openBook(world, user, hand);
    }
    @ExpectPlatform
    public static TypedActionResult<ItemStack> openBook(World world,PlayerEntity user, Hand hand) {
        throw new AssertionError();
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("pfm.patchouli.guide_book.subtitle"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
