package com.unlikepaladin.pfm.compat.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.RecipeRegistry;
import com.unlikepaladin.pfm.registry.ScreenHandlersRegistry;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.Identifier;

public class PaladinFurnitureModEMIPlugin implements EmiPlugin {
    protected static EmiRecipeCategory FURNITURE;
    protected static EmiRecipeCategory FREEZER;

    public static EmiIngredient WORKBENCH_ICON = EmiStack.of(BlockItemRegistry.WORKING_TABLE);
    public static Identifier WORKBENCH_ID = new Identifier("pfm:furniture");
    public static EmiIngredient FREEZER_ICON = EmiStack.of(BlockItemRegistry.WHITE_FRIDGE);
    public static Identifier FREEZER_ID = new Identifier("pfm:freezer");
    @Override
    public void register(EmiRegistry registry) {
        FURNITURE  = new EmiRecipeCategory(WORKBENCH_ID, WORKBENCH_ICON, simplifiedRenderer(240, 240));
        FREEZER  = new EmiRecipeCategory(FREEZER_ID, FREEZER_ICON, simplifiedRenderer(240, 240));
        registry.addCategory(FURNITURE);
        registry.addWorkstation(FURNITURE, WORKBENCH_ICON);
        registry.addCategory(FREEZER);
        registry.addWorkstation(FREEZER, FREEZER_ICON);
        registry.addRecipeHandler(ScreenHandlersRegistry.WORKBENCH_SCREEN_HANDLER, new FurnitureRecipeHandler());
        registry.addRecipeHandler(ScreenHandlersRegistry.FREEZER_SCREEN_HANDLER, new FreezerRecipeHandler(FREEZER));

        for (FurnitureRecipe recipe : registry.getRecipeManager().listAllOfType(RecipeRegistry.FURNITURE_RECIPE)) {
            registry.addRecipe(new EmiFurnitureRecipe(recipe));
        }
        for (FreezingRecipe recipe : registry.getRecipeManager().listAllOfType(RecipeRegistry.FREEZING_RECIPE)) {
            registry.addRecipe(new EmiFreezingRecipe(recipe));
        }
    }

    private static EmiRenderable simplifiedRenderer(int u, int v) {
        return (matrices, x, y, delta) -> {
            RenderSystem.setShaderTexture(0, EmiRenderHelper.WIDGETS);
            DrawableHelper.drawTexture(matrices, x, y, u, v, 16, 16, 256, 256);
        };
    }
}
