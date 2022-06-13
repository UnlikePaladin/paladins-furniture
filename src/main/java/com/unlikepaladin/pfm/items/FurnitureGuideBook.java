package com.unlikepaladin.pfm.items;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class FurnitureGuideBook extends Item {
    public FurnitureGuideBook(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && FabricLoader.getInstance().isModLoaded("patchouli")) {
            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) user, new Identifier("pfm:guide_book"));
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        else if (world.isClient && !FabricLoader.getInstance().isModLoaded("patchouli"))
        {
            user.sendMessage(Text.translatable("message.pfm.patchouli_not_installed"),false);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("pfm.patchouli.guide_book.subtitle"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
