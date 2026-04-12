package com.unlikepaladin.pfm.recipes;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface FurnitureRecipe extends Recipe<FurnitureRecipe.FurnitureRecipeInput> {
    @Override
    default RecipeType<?> getType() {
        return RecipeTypes.FURNITURE_RECIPE;
    }

    List<CraftableFurnitureRecipe> getInnerRecipes();

    String outputClass();

    default List<CraftableFurnitureRecipe> getAvailableOutputs(FurnitureRecipe.FurnitureRecipeInput inventory, HolderLookup.Provider registryManager) {
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

    default int getOutputCount(HolderLookup.Provider registryManager) {
        return getResultItem(registryManager).getCount();
    }

    default List<? extends CraftableFurnitureRecipe> getInnerRecipesForVariant(ResourceLocation identifier) {
        return Collections.singletonList(getInnerRecipes().get(0));
    }

    default String getName(HolderLookup.Provider registryManager) {
        return getResultItem(registryManager).getHoverName().getString();
    }

    interface CraftableFurnitureRecipe extends Comparable<CraftableFurnitureRecipe> {
        List<Ingredient> getIngredients();
        ItemStack getResultItem(HolderLookup.Provider registryManager);
        ItemStack assemble(FurnitureRecipe.FurnitureRecipeInput inventory, HolderLookup.Provider registryManager);
        boolean matches(FurnitureRecipe.FurnitureRecipeInput playerInventory, Level world);
        FurnitureRecipe parent();
        ItemStack getRecipeOuput();

        @Override
        default int compareTo(@NotNull FurnitureRecipe.CraftableFurnitureRecipe o) {
            return getRecipeOuput().toString().compareTo(o.getRecipeOuput().toString());
        }

        default ItemStack craftAndRemoveItems(FurnitureRecipe.FurnitureRecipeInput input, HolderLookup.Provider registryManager) {
            ItemStack output = getResultItem(registryManager).copy();
            List<Ingredient> ingredients = getIngredients();
            PlayerInventory playerInventory = input.playerInventory();
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
    public static record FurnitureRecipeInput(PlayerInventory playerInventory) implements RecipeInput {

        @Override
        public ItemStack getStackInSlot(int slot) {
            return playerInventory.getStack(slot);
        }

        @Override
        public int getSize() {
            return playerInventory.size();
        }

        @Override
        public boolean isEmpty() {
            return playerInventory.isEmpty();
        }
    }
}
