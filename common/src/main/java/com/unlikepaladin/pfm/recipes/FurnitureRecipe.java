package com.unlikepaladin.pfm.recipes;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.registry.Registry;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface FurnitureRecipe extends Recipe<Inventory> {
    @Override
    default RecipeType<?> getType() {
        return RecipeTypes.FURNITURE_RECIPE;
    }

    List<CraftableFurnitureRecipe> getInnerRecipes();

    String outputClass();

    default List<CraftableFurnitureRecipe> getAvailableOutputs(Inventory inventory) {
        return getInnerRecipes();
    }

    default CraftableFurnitureRecipe getInnerRecipeFromOutput(ItemStack stack) {
        return getInnerRecipes().get(0);
    }

    static int getSlotWithStackIgnoreNBT(Inventory inventory, ItemStack stack) {
        for(int i = 0; i < inventory.items.size(); ++i) {
            if (!inventory.items.get(i).isEmpty() && stack.is(inventory.items.get(i).getItem())) {
                return i;
            }
        }
        return -1;
    }

    default ItemStack getToastSymbol() {
        return PaladinFurnitureModBlocksItems.WORKING_TABLE.asItem().getDefaultInstance();
    }

    default int getMaxInnerRecipeSize() {
        return getIngredients().size();
    }

    default int getOutputCount() {
        return getResultItem().getCount();
    }

    default List<? extends CraftableFurnitureRecipe> getInnerRecipesForVariant(ResourceLocation identifier) {
        return Collections.singletonList(getInnerRecipes().get(0));
    }

    default String getName() {
        return getResultItem().getHoverName().getString();
    }

    interface CraftableFurnitureRecipe extends Comparable<CraftableFurnitureRecipe> {
        List<Ingredient> getIngredients();
        ItemStack getResultItem();
        ItemStack assemble(Inventory inventory);
        boolean matches(Inventory playerInventory, Level world);
        FurnitureRecipe parent();
        @Override
        default int compareTo(@NotNull FurnitureRecipe.CraftableFurnitureRecipe o) {
            return getResultItem().toString().compareTo(o.getResultItem().toString());
        }

        default ItemStack craftAndRemoveItems(Inventory playerInventory) {
            ItemStack output = getResultItem().copy();
            List<Ingredient> ingredients = getIngredients();
            for (Ingredient ingredient : ingredients) {
                for (ItemStack stack : ingredient.getItems()) {
                    int indexOfStack = FurnitureRecipe.getSlotWithStackIgnoreNBT(playerInventory, stack);
                    int count = stack.getCount();
                    if (indexOfStack != -1) {
                        if (playerInventory.getItem(indexOfStack).getCount() >= stack.getCount()) {
                            ItemStack stack1 = playerInventory.getItem(indexOfStack);
                            stack1.shrink(stack.getCount());
                            playerInventory.setItem(indexOfStack, stack1);
                            playerInventory.setChanged();
                            break;
                        } else {
                            int remainingCount = count - playerInventory.getItem(indexOfStack).getCount();
                            playerInventory.setItem(indexOfStack, ItemStack.EMPTY);

                            while (remainingCount > 0) {
                                indexOfStack = FurnitureRecipe.getSlotWithStackIgnoreNBT(playerInventory, stack);
                                if (indexOfStack != -1) {
                                    ItemStack stack1 = playerInventory.getItem(indexOfStack);
                                    if (stack1.getCount() >= remainingCount) {
                                        stack1.shrink(remainingCount);
                                        playerInventory.setItem(indexOfStack, stack1);
                                        break;
                                    } else {
                                        int stackSize = stack1.getCount();
                                        remainingCount = Math.max(remainingCount-stackSize, 0);
                                        playerInventory.setItem(indexOfStack, ItemStack.EMPTY);
                                    }
                                } else {
                                    PaladinFurnitureMod.GENERAL_LOGGER.warn("Unable to craft recipe, this should never happen");
                                    return ItemStack.EMPTY;
                                }
                            }
                            playerInventory.setChanged();
                        }
                    }
                }
            }
            return output;
        }
    }
}
