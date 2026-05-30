package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;

import java.util.List;

public class StoveScreen extends AbstractFurnaceScreen<StoveScreenHandler> {
    // You can replace the background with whatever you like, just remember there will always be the recipe book button
    private static final Identifier BACKGROUND = Identifier.parse("textures/gui/container/smoker.png");
    private static final Identifier LIT_PROGRESS_TEXTURE = Identifier.parse("container/smoker/lit_progress");
    private static final Identifier BURN_PROGRESS_TEXTURE = Identifier.parse("container/smoker/burn_progress");
    private static final Component TOGGLE_SMOKABLE_TEXT = Component.translatable("gui.recipebook.toggleRecipes.smokable");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.SMOKER), new RecipeBookComponent.TabInfo(Items.PORKCHOP, RecipeBookCategories.SMOKER_FOOD)
    );

    public StoveScreen(StoveScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, TOGGLE_SMOKABLE_TEXT, BACKGROUND, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE, TABS);
    }
}
