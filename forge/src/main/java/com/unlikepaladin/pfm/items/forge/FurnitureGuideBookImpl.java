package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.items.FurnitureGuideBook;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import vazkii.patchouli.api.PatchouliAPI;

import java.net.URI;


public class FurnitureGuideBookImpl extends FurnitureGuideBook {
    public FurnitureGuideBookImpl(Item.Properties settings) {
        super(settings);
    }

    public static InteractionResult openBook(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide() && ModList.get().isLoaded("patchouli")) {
            PatchouliAPI.get().openBookGUI((ServerPlayer) user, ResourceLocation.parse("pfm:guide_book"));
            return InteractionResult.SUCCESS;
        }
        else if (world.isClientSide && !ModList.get().isLoaded("patchouli"))
        {
            Component text = Component.translatable("message.pfm.patchouli_not_installed").setStyle(Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://github.com/UnlikePaladin/paladins-furniture/wiki"))));
            user.displayClientMessage(text,false);
        }
        return InteractionResult.PASS;
    }
}
