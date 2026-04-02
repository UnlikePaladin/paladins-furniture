package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.menus.IronStoveScreenHandler;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmokingRecipeBookComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class IronStoveScreen extends AbstractFurnaceScreen<IronStoveScreenHandler> {
    //You can replace the background with whatever you like, just remember there will always be the recipe book button
    private static final ResourceLocation BACKGROUND = new ResourceLocation("textures/gui/container/smoker.png");

    private static final Identifier LIT_PROGRESS_TEXTURE = new Identifier("container/smoker/lit_progress");
    private static final Identifier BURN_PROGRESS_TEXTURE = new Identifier("container/smoker/burn_progress");
    public IronStoveScreen(IronStoveScreenHandler handler, Inventory inventory, Component title) {
        super(handler, new SmokingRecipeBookComponent(), inventory, title, BACKGROUND, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE);
    }
}
