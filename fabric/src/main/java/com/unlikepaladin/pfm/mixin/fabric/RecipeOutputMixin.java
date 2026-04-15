package com.unlikepaladin.pfm.mixin.fabric;

import net.minecraft.data.recipes.RecipeOutput;
import org.spongepowered.asm.mixin.Mixin;
import net.fabricmc.fabric.api.datagen.v1.recipe.FabricRecipeExporter;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.fabricmc.fabric.api.datagen.v1.recipe.FabricRecipeExporter;

// Workaround for FAPI
@Mixin(RecipeOutput.class)
@Restriction(require = @Condition(value = "fabric-api", versionPredicates = { ">=0.116.4 <0.118.0", ">=0.127.0" }))
public interface RecipeOutputMixin extends FabricRecipeExporter {}
