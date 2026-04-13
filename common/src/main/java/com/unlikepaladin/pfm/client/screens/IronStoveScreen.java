package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.menus.IronStoveScreenHandler;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmokingRecipeBookComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.Items;

import java.util.List;

public class IronStoveScreen extends AbstractFurnaceScreen<IronStoveScreenHandler> {
    //You can replace the background with whatever you like, just remember there will always be the recipe book button
    private static final ResourceLocation BACKGROUND = ResourceLocation.parse("textures/gui/container/smoker.png");

    private static final ResourceLocation LIT_PROGRESS_TEXTURE = ResourceLocation.parse("container/smoker/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.parse("container/smoker/burn_progress");
    private static final Text TOGGLE_SMOKABLE_TEXT = Component.translatable("gui.recipebook.toggleRecipes.smokable");
    private static final List<RecipeBookWidget.Tab> TABS = List.of(
            new RecipeBookWidget.Tab(RecipeBookType.SMOKER), new RecipeBookWidget.Tab(Items.PORKCHOP, RecipeBookCategories.SMOKER_FOOD)
    );

    public IronStoveScreen(IronStoveScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, TOGGLE_SMOKABLE_TEXT, BACKGROUND, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE, TABS);
    }
}
