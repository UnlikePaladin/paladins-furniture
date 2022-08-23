package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.SmokerRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class StoveScreen extends AbstractFurnaceScreen<StoveScreenHandler> {
    //You can replace the background with whatever you like, just remember there will always be the recipe book button
    private static final Identifier BACKGROUND = new Identifier("textures/gui/container/smoker.png");

    public StoveScreen(StoveScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new SmokerRecipeBookScreen(), inventory, title, BACKGROUND);
    }
}
