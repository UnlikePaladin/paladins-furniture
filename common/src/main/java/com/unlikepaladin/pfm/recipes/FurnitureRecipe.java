package com.unlikepaladin.pfm.recipes;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FurnitureRecipe extends Recipe<FurnitureRecipe.FurnitureRecipeInput> {
    @Override
    default RecipeType<? extends Recipe<FurnitureRecipeInput>> getType() {
        return RecipeTypes.FURNITURE_RECIPE;
    }

    List<CraftableFurnitureRecipe> getInnerRecipes(FeatureFlagSet features);

    String outputClass();

    default List<CraftableFurnitureRecipe> getAvailableOutputs(FurnitureRecipe.FurnitureRecipeInput inventory, HolderLookup.Provider registryManager) {
        return getInnerRecipes(inventory.playerInventory.player.level().enabledFeatures());
    }

    static int getSlotWithStackIgnoreNBT(Inventory inventory, Item item) {
        for(int i = 0; i < inventory.getNonEquipmentItems().size(); ++i) {
            if (!inventory.getNonEquipmentItems().get(i).isEmpty() && inventory.getNonEquipmentItems().get(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    default ItemStack getToastSymbol() {
        return PaladinFurnitureModBlocksItems.WORKING_TABLE.asItem().getDefaultInstance();
    }

    default int getMaxInnerRecipeSize() {
        return placementInfo().ingredients().size();
    }

    default int getOutputCount(HolderLookup.Provider registryManager) {
        return getResult(registryManager).getCount();
    }

    ItemStack getResult(HolderLookup.Provider registryManager);

    default List<? extends CraftableFurnitureRecipe> getInnerRecipesForVariant(Level world, ResourceLocation identifier) {
        return Collections.singletonList(getInnerRecipes(world.enabledFeatures()).getFirst());
    }

    default String getName(HolderLookup.Provider registryManager) {
        return getResult(registryManager).getHoverName().getString();
    }

    void write(RegistryFriendlyByteBuf buf);

    default boolean enabled(Level world) {
        for (CraftableFurnitureRecipe recipe : getInnerRecipes(world.enabledFeatures())) {
            if (!recipe.isInnerEnabled(world.enabledFeatures()))
                return false;
        }
        return true;
    }

    default List<Ingredient> getIngredients(Level world) {
        return placementInfo().ingredients();
    }

    CraftableFurnitureRecipe getInnerRecipeFromOutput(ItemStack itemStack);

    interface CraftableFurnitureRecipe extends Comparable<CraftableFurnitureRecipe> {
        List<Ingredient> getIngredients();

        ItemStack getResult(HolderLookup.Provider registryManager);

        ItemStack assemble(FurnitureRecipe.FurnitureRecipeInput inventory, HolderLookup.Provider registryManager);

        boolean matches(FurnitureRecipe.FurnitureRecipeInput playerInventory, Level world);

        FurnitureRecipe parent();

        ItemStack getRecipeOuput();
        @Override
        default int compareTo(@NotNull FurnitureRecipe.CraftableFurnitureRecipe o) {
            return getRecipeOuput().toString().compareTo(o.getRecipeOuput().toString());
        }

        default boolean isInnerEnabled(FeatureFlagSet featureSet) {
            if (!this.getRecipeOuput().isItemEnabled(featureSet))
                return false;
            for (Ingredient ingredient : this.getIngredients()) {
                for (Holder<Item> item : ingredient.items().toList()) {
                    if (!item.value().isEnabled(featureSet))
                        return false;
                }
            }
            return true;
        }

        default ItemStack craftAndRemoveItems(FurnitureRecipe.FurnitureRecipeInput input, HolderLookup.Provider registryManager) {
            ItemStack output = getResult(registryManager).copy();
            List<Ingredient> ingredients = getIngredients();
            Inventory playerInventory = input.playerInventory();
            Map<Item, Integer> ingredientCounts = getItemCounts();
            for (Map.Entry<Item, Integer> entry : ingredientCounts.entrySet()) {
                Item item = entry.getKey();
                Integer count = entry.getValue();

                int indexOfStack = FurnitureRecipe.getSlotWithStackIgnoreNBT(playerInventory, item);
                if (indexOfStack != -1) {
                    if (playerInventory.getItem(indexOfStack).getCount() >= count) {
                        ItemStack stack1 = playerInventory.getItem(indexOfStack);
                        stack1.shrink(count);
                        playerInventory.setItem(indexOfStack, stack1);
                        playerInventory.setChanged();
                    } else {
                        int remainingCount = count - playerInventory.getItem(indexOfStack).getCount();
                        playerInventory.setItem(indexOfStack, ItemStack.EMPTY);
                        while (remainingCount > 0) {
                            indexOfStack = FurnitureRecipe.getSlotWithStackIgnoreNBT(playerInventory, item);
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
            return output;
        }

        default Map<Item, Integer> getItemCounts() {
            Map<Item, Integer> ingredientCounts = new HashMap<>();
            for (Ingredient ingredient : this.getIngredients()) {
                for (Holder<Item> itemRegistryEntry : ingredient.items().toList()) {
                    if (ingredientCounts.containsKey(itemRegistryEntry.value())) {
                        ingredientCounts.put(itemRegistryEntry.value(), ingredientCounts.get(itemRegistryEntry.value())+1);
                    } else {
                        ingredientCounts.put(itemRegistryEntry.value(), 1);
                    }
                }
            }
            return ingredientCounts;
        }

    }
    record FurnitureRecipeInput(Inventory playerInventory) implements RecipeInput {

        @Override
        public ItemStack getItem(int slot) {
            return playerInventory.getItem(slot);
        }

        @Override
        public int size() {
            return playerInventory.getContainerSize();
        }

        @Override
        public boolean isEmpty() {
            return playerInventory.isEmpty();
        }
    }
}
