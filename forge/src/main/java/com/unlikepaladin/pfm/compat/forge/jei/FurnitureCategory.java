package com.unlikepaladin.pfm.compat.forge.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICustomCraftingCategoryExtension;
import net.minecraft.block.Block;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.util.Size2i;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FurnitureCategory implements IRecipeCategory<FurnitureRecipe> {
    private final IDrawable BACKGROUND;
    public static final Identifier TEXTURE_GUI_VANILLA = new Identifier("jei:textures/gui/gui_vanilla.png");
    public final IDrawable ICON;
    public static final TranslatableText TITLE = new TranslatableText("rei.pfm.furniture");
    private final ICraftingGridHelper craftingGridHelper;
    private static final int craftOutputSlot = 0;
    private static final int craftInputSlot1 = 1;

    public FurnitureCategory(IGuiHelper guiHelper) {
        ICON = guiHelper.createDrawableIngredient(new ItemStack(PaladinFurnitureModBlocksItems.WORKING_TABLE));
        this.BACKGROUND = guiHelper.createDrawable(TEXTURE_GUI_VANILLA, 0, 60, 116, 54);
        craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1);
    }
    public static final Identifier IDENTIFIER = new Identifier(PaladinFurnitureMod.MOD_ID, "crafting");

    @Override
    public Identifier getUid() {
        return IDENTIFIER;
    }

    @Override
    public Class getRecipeClass() {
        return FurnitureRecipe.class;
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
    public void setIngredients(FurnitureRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FurnitureRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(craftOutputSlot, false, 94, 18);

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int index = craftInputSlot1 + x + (y * 3);
                guiItemStacks.init(index, true, x * 18, y * 18);
            }
        }


        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

        Size2i size = new Size2i(recipe.getWidth(), recipe.getHeight());
        if (size != null && size.width > 0 && size.height > 0) {
            craftingGridHelper.setInputs(guiItemStacks, inputs, size.width, size.height);
        } else {
            craftingGridHelper.setInputs(guiItemStacks, inputs);
            recipeLayout.setShapeless();
        }
        guiItemStacks.set(craftOutputSlot, outputs.get(0));
    }

}
