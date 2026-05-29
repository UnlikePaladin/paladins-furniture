package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.menus.OvenScreenHandler;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;

import java.util.List;

public class OvenScreen extends AbstractFurnaceScreen<OvenScreenHandler> {
    //You can replace the background with whatever you like, just remember there will always be the recipe book button
    private static final ResourceLocation BACKGROUND = ResourceLocation.parse("textures/gui/container/smoker.png");

    private static final ResourceLocation LIT_PROGRESS_TEXTURE = ResourceLocation.parse("container/smoker/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.parse("container/smoker/burn_progress");
    private static final Component TOGGLE_SMOKABLE_TEXT = Component.translatable("gui.recipebook.toggleRecipes.smokable");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.SMOKER), new RecipeBookComponent.TabInfo(Items.PORKCHOP, RecipeBookCategories.SMOKER_FOOD)
    );

    public OvenScreen(OvenScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, TOGGLE_SMOKABLE_TEXT, BACKGROUND, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE, TABS);
    }
}
