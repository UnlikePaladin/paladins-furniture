package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.neoforge.FreezerBlockEntityImpl;
import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.balm.world.ContainerUtils;
import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.entity.FridgeBlockEntity;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.*;
import java.util.function.Function;

public class FreezerBlockEntityBalm extends FreezerBlockEntityImpl implements BalmContainerProvider, KitchenItemProviderHolder {
    private final KitchenItemProvider itemProvider;

    public FreezerBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemProvider = new ContainerKitchenItemProvider(this){
            private final ItemStack snowStack;
            private final ItemStack iceStack;
            {
                this.snowStack = new ItemStack(Items.SNOWBALL);
                this.iceStack = new ItemStack(Blocks.ICE);
            }

            @Override
            public IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
                IngredientToken result = applyIceUnit(ingredient::test);
                if (result != null)
                    return result;

                return super.findIngredient(ingredient, ingredientTokens, cacheHint);
            }

            @Override
            public IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
                IngredientToken result = applyIceUnit(stack -> ItemStack.areItemsEqual(stack, itemStack));
                if (result != null)
                    return result;

                return super.findIngredient(itemStack, ingredientTokens, cacheHint);
            }

            private @Nullable IngredientToken applyIceUnit(Function<ItemStack, Boolean> predicate) {
                if (predicate.apply(this.snowStack))
                    return new FridgeBlockEntity.IceUnitIngredientToken(ContainerUtils.copyStackWithSize(this.snowStack, 64));
                else
                    return predicate.apply(this.iceStack) ? new FridgeBlockEntity.IceUnitIngredientToken(ContainerUtils.copyStackWithSize(this.iceStack, 64)) : null;
            }
        };
    }

    @Override
    public Inventory getContainer() {
        return this;
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }
}