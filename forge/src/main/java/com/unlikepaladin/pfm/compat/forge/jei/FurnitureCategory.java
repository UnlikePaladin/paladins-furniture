package com.unlikepaladin.pfm.compat.forge.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.block.Block;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.util.Size2i;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FurnitureCategory implements IRecipeCategory<FurnitureRecipe> {
    private final IDrawable BACKGROUND;
    public static final Identifier TEXTURE_GUI_VANILLA = new Identifier("jei", "textures/jei/gui/gui_vanilla.png");
    public final IDrawable ICON;
    public static final Text TITLE = Text.translatable("rei.pfm.furniture");
    private final ICraftingGridHelper craftingGridHelper;
    private static final int craftOutputSlot = 0;
    private static final int craftInputSlot1 = 1;

    public FurnitureCategory(IGuiHelper guiHelper) {
        ICON = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(PaladinFurnitureModBlocksItems.WORKING_TABLE));
        this.BACKGROUND = guiHelper.createDrawable(TEXTURE_GUI_VANILLA, 0, 60, 116, 54);
        craftingGridHelper = guiHelper.createCraftingGridHelper();
    }
    public static final Identifier IDENTIFIER = new Identifier(PaladinFurnitureMod.MOD_ID, "crafting");

    @Override
    public RecipeType<FurnitureRecipe> getRecipeType() {
        return PaladinFurnitureModJEIPlugin.FURNITURE_RECIPE;
    }

    @Override
    public Text getTitle() {
        return TITLE;
    }

    @Override
    public IDrawable getBackground() {
        return BACKGROUND;
    }

    @Override
    public IDrawable getIcon() {
        return ICON;
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FurnitureRecipe recipe, IFocusGroup focuses) {
        List<List<ItemStack>> inputs = recipe.getIngredients().stream()
                .map(ingredient -> List.of(ingredient.getMatchingStacks()))
                .toList();
        ItemStack resultItem = recipe.getOutput();

        int width = recipe.getWidth();
        int height = recipe.getHeight();
        craftingGridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, List.of(resultItem));
        craftingGridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, inputs, width, height);
    }

}
