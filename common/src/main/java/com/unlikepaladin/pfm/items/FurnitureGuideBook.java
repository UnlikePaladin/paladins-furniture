package com.unlikepaladin.pfm.items;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

public class FurnitureGuideBook extends Item {
    public FurnitureGuideBook(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        return openBook(world, user, hand);
    }
    @ExpectPlatform
    public static ActionResult openBook(World world,PlayerEntity user, Hand hand) {
        throw new AssertionError();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.translatable("pfm.patchouli.guide_book.subtitle"));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
