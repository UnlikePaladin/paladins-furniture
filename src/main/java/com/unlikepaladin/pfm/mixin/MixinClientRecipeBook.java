package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.registry.RecipeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientRecipeBook.class)
public abstract class MixinClientRecipeBook {
    @Inject(method = "getGroupForRecipe", at = @At(value = "HEAD"), cancellable = true)
    private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> callbackInfoReturnable) {
        RecipeType<?> type = recipe.getType();
        if (type == RecipeRegistry.FREEZING_RECIPE || type == RecipeRegistry.FURNITURE_RECIPE) {
            callbackInfoReturnable.setReturnValue(RecipeBookGroup.UNKNOWN);
        }
    }
}
