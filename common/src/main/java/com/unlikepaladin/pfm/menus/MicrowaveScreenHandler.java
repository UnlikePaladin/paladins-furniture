package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.network.chat.Component;

public class MicrowaveScreenHandler
        extends AbstractMicrowaveScreenHandler {
    public MicrowaveScreenHandler(int syncId, Inventory playerInventory, MicrowaveData buf) {
        super(ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookType.SMOKER, syncId, playerInventory, buf);
    }

    public MicrowaveScreenHandler(MicrowaveBlockEntity microwaveBlockEntity, int containerId, Inventory playerInventory, Container inventory, ContainerData dataAccess) {
        super(microwaveBlockEntity, ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookType.SMOKER, containerId, playerInventory, inventory, dataAccess);
    }
}

