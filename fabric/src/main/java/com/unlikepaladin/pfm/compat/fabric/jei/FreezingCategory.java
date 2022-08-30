package com.unlikepaladin.pfm.compat.fabric.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class FreezingCategory implements IRecipeCategory<FreezingRecipe>  {
    public static final TranslatableText TITLE = new TranslatableText("rei.pfm.freezer");
    public final IDrawable ICON;
    public static final Identifier IDENTIFIER = new Identifier(PaladinFurnitureMod.MOD_ID, "freezing");
    private final IDrawable BACKGROUND;
    public static final Identifier FREEZE_GUI = new Identifier(PaladinFurnitureMod.MOD_ID, "textures/gui/container/freezer.png");
    protected final IDrawableStatic staticFreezeIcon;
    protected final IDrawableAnimated animatedFreezeIcon;
    private final int regularFreezeTime;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public FreezingCategory(IGuiHelper guiHelper) {
        ICON = guiHelper.createDrawableIngredient(new ItemStack(PaladinFurnitureModBlocksItems.WHITE_FRIDGE));
        this.BACKGROUND = guiHelper.createDrawable(FREEZE_GUI,55, 16, 82, 54);
        staticFreezeIcon = guiHelper.createDrawable(FREEZE_GUI, 176, 0, 12, 12);
        animatedFreezeIcon = guiHelper.createAnimatedDrawable(staticFreezeIcon, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.regularFreezeTime = 100;
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer freezeTime) {
                        return guiHelper.drawableBuilder(FREEZE_GUI, 176, 14, 24, 17)
                                .buildAnimated(freezeTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    @Override
    public Identifier getUid() {
        return IDENTIFIER;
    }

    @Override
    public Class getRecipeClass() {
        return FreezingRecipe.class;
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
    public void setIngredients(FreezingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    protected static final int inputSlot = 0;
    protected static final int fuelSlot = 1;
    protected static final int outputSlot = 2;

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FreezingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot, true, 0, 0);
        guiItemStacks.init(outputSlot, false, 60, 18);

        guiItemStacks.set(ingredients);
    }

    protected IDrawableAnimated getArrow(FreezingRecipe recipe) {
        int freezeTime = recipe.getCookTime();
        if (freezeTime <= 0) {
            freezeTime = regularFreezeTime;
        }
        return this.cachedArrows.getUnchecked(freezeTime);
    }

    protected void drawFreezeTime(FreezingRecipe recipe, MatrixStack poseStack, int y) {
        int freezeTime = recipe.getCookTime();
        if (freezeTime > 0) {
            int freezeTimeSeconds = freezeTime / 20;
            Text timeString = Text.of(freezeTimeSeconds + "s");
            MinecraftClient minecraft = MinecraftClient.getInstance();
            TextRenderer fontRenderer = minecraft.textRenderer;
            int stringWidth = fontRenderer.getWidth(timeString);
            fontRenderer.draw(poseStack, timeString, BACKGROUND.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected void drawExperience(FreezingRecipe recipe, MatrixStack poseStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Text experienceString = Text.of(experience + " XP");
            MinecraftClient minecraft = MinecraftClient.getInstance();
            TextRenderer fontRenderer = minecraft.textRenderer;
            int stringWidth = fontRenderer.getWidth(experienceString);
            fontRenderer.draw(poseStack, experienceString, BACKGROUND.getWidth() - stringWidth, y, 0xFF808080);
        }
    }


    @Override
    public void draw(FreezingRecipe recipe, MatrixStack poseStack, double mouseX, double mouseY) {
        animatedFreezeIcon.draw(poseStack, 1, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 24, 18);

        drawExperience(recipe, poseStack, 0);
        drawFreezeTime(recipe, poseStack, 45);
    }
}
