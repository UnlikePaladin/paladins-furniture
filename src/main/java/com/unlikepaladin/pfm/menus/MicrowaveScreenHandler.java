package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.registry.ScreenHandlersRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.PropertyDelegate;

public class MicrowaveScreenHandler
        extends AbstractMicrowaveScreenHandler {
    public MicrowaveScreenHandler( int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(null, ScreenHandlersRegistry.MICROWAVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory, buf);
    }

    public MicrowaveScreenHandler(MicrowaveBlockEntity microwaveBlockEntity, int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(microwaveBlockEntity, ScreenHandlersRegistry.MICROWAVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory, inventory, propertyDelegate);
    }




}

