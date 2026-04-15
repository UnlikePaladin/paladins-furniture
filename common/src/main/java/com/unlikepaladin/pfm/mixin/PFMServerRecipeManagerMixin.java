package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class PFMServerRecipeManagerMixin {
    @Mutable
    @Shadow @Final private static Map<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor> RECIPE_PROPERTY_SETS;

    @Shadow
    private static RecipeManager.IngredientExtractor forSingleInput(RecipeType<? extends SingleItemRecipe> expectedType) {
        return null;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onServerRecipeManagerInit(CallbackInfo ci) {
        RECIPE_PROPERTY_SETS = new HashMap<>(RECIPE_PROPERTY_SETS);
        RECIPE_PROPERTY_SETS.put(RecipeTypes.FREEZING_INPUT, forSingleInput(RecipeTypes.FREEZING_RECIPE));
        RECIPE_PROPERTY_SETS = Collections.unmodifiableMap(RECIPE_PROPERTY_SETS);
    }
}
