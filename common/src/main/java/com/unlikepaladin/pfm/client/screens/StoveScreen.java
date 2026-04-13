package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmokingRecipeBookComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class StoveScreen extends AbstractFurnaceScreen<StoveScreenHandler> {
    // You can replace the background with whatever you like, just remember there will always be the recipe book button
    private static final ResourceLocation BACKGROUND = ResourceLocation.parse("textures/gui/container/smoker.png");
    private static final ResourceLocation LIT_PROGRESS_TEXTURE = ResourceLocation.parse("container/smoker/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.parse("container/smoker/burn_progress");
    private static final Component TOGGLE_SMOKABLE_TEXT = Component.translatable("gui.recipebook.toggleRecipes.smokable");
    private static final List<RecipeBookWidget.Tab> TABS = List.of(
            new RecipeBookWidget.Tab(RecipeBookType.SMOKER), new RecipeBookWidget.Tab(Items.PORKCHOP, RecipeBookCategories.SMOKER_FOOD)
    );

    public StoveScreen(StoveScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, TOGGLE_SMOKABLE_TEXT, BACKGROUND, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE, TABS);
    }
}
