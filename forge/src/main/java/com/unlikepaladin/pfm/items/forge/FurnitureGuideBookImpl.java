package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.items.FurnitureGuideBook;
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
import net.minecraftforge.fml.ModList;
import vazkii.patchouli.api.PatchouliAPI;


public class FurnitureGuideBookImpl extends FurnitureGuideBook {
    public FurnitureGuideBookImpl(Item.Settings settings) {
        super(settings);
    }

    public static TypedActionResult<ItemStack> openBook(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && ModList.get().isLoaded("patchouli")) {
            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) user, new Identifier("pfm:guide_book"));
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        else if (world.isClient && !ModList.get().isLoaded("patchouli"))
        {
            user.sendMessage(new TranslatableText("message.pfm.patchouli_not_installed"),false);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
