package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.Text;

public class MicrowaveScreenHandler
        extends AbstractMicrowaveScreenHandler {
    public MicrowaveScreenHandler( int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(null, ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory, buf);
        PaladinFurnitureMod.GENERAL_LOGGER.info("it's me hi, i'm the problem it's me");
    }

    public MicrowaveScreenHandler(MicrowaveBlockEntity microwaveBlockEntity, int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(microwaveBlockEntity, ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory, inventory, propertyDelegate);
        PaladinFurnitureMod.GENERAL_LOGGER.info("i'm the good guy, duh");
    }
}

