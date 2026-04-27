package com.unlikepaladin.pfm.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Ingredient.class)
public interface PFMIngredientMatchingStacksAccessor {
    @Accessor("itemStacks")
    ItemStack[] getMatchingStacks();

    @Invoker("dissolve")
    void invokeCacheMatchingStacks();
}
