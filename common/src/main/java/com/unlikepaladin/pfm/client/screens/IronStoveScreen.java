package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.menus.IronStoveScreenHandler;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.SmokerRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IronStoveScreen extends AbstractFurnaceScreen<IronStoveScreenHandler> {
    //You can replace the background with whatever you like, just remember there will always be the recipe book button
    private static final Identifier BACKGROUND = Identifier.of("textures/gui/container/smoker.png");

    private static final Identifier LIT_PROGRESS_TEXTURE = Identifier.of("container/smoker/lit_progress");
    private static final Identifier BURN_PROGRESS_TEXTURE = Identifier.of("container/smoker/burn_progress");
    public IronStoveScreen(IronStoveScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new SmokerRecipeBookScreen(), inventory, title, BACKGROUND, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE);
    }
}
