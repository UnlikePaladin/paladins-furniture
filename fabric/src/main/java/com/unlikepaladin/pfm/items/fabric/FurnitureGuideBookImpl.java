package com.unlikepaladin.pfm.items.fabric;

import com.unlikepaladin.pfm.items.FurnitureGuideBook;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.PatchouliAPI;

public class FurnitureGuideBookImpl extends FurnitureGuideBook {
    public FurnitureGuideBookImpl(Item.Properties settings) {
        super(settings);
    }

    public static InteractionResultHolder<ItemStack> openBook(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide() && FabricLoader.getInstance().isModLoaded("patchouli")) {
                PatchouliAPI.get().openBookGUI((ServerPlayer) user, ResourceLocation.parse("pfm:guide_book"));
            return InteractionResultHolder.success(user.getItemInHand(hand));
        }
        else if (world.isClientSide && !FabricLoader.getInstance().isModLoaded("patchouli"))
        {
            Component text = Component.translatable("message.pfm.patchouli_not_installed").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/UnlikePaladin/paladins-furniture/wiki")));
            user.displayClientMessage(text,false);
        }
        return InteractionResultHolder.pass(user.getItemInHand(hand));
    }
}
