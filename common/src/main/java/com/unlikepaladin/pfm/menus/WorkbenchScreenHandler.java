package com.unlikepaladin.pfm.menus;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import java.util.List;

public class WorkbenchScreenHandler extends AbstractContainerMenu {
    private final ContainerLevelAccess context;
    private final List<FurnitureRecipe.CraftableFurnitureRecipe> availableRecipes = Lists.newArrayList();
    public static final List<FurnitureRecipe.CraftableFurnitureRecipe> ALL_RECIPES = Lists.newArrayList();
    private final List<FurnitureRecipe.CraftableFurnitureRecipe> sortedRecipes = Lists.newArrayList();
    private final List<FurnitureRecipe.CraftableFurnitureRecipe> searchableRecipes = Lists.newArrayList();

    private final DataSlot selectedRecipe = DataSlot.standalone();
    private final Level level;
    final Slot outputSlot;
    final ResultContainer output = new ResultContainer();
    long lastTakeTime;
    final Inventory playerInventory;
    Runnable contentsChangedListener;
   public boolean searching = false;

    public WorkbenchScreenHandler(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }
    public WorkbenchScreenHandler(int containerId, Inventory playerInventory, final ContainerLevelAccess context) {
        super(ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER, containerId);
        this.context = context;
        this.level = playerInventory.player.level();
        this.playerInventory = playerInventory;
        this.contentsChangedListener = () -> {
        };
        this.outputSlot = this.addSlot(new Slot(this.output, 0, 143, 50) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
            @Override
            public void onTake(Player player, ItemStack stack) {
                if (WorkbenchScreenHandler.this.craft()) {
                    stack.onCraftedBy(player.level(), player, stack.getCount());
                    WorkbenchScreenHandler.this.output.awardUsedRecipes(player, List.of());
                    WorkbenchScreenHandler.this.populateResult(player);
                    context.execute((world, pos) -> {
                        long l = world.getDayTime();
                        if (WorkbenchScreenHandler.this.lastTakeTime != l) {
                            world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0f, 1.0f);
                            WorkbenchScreenHandler.this.lastTakeTime = l;
                        }
                    });
                    WorkbenchScreenHandler.this.contentsChangedListener.run();
                    WorkbenchScreenHandler.this.updateInput();
                    super.onTake(player, stack);
                }
            }
        });
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 98 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 156));
        }
        this.addDataSlot(this.selectedRecipe);
        if (ALL_RECIPES.isEmpty()) {
           level.getRecipeManager().getAllRecipesFor(RecipeTypes.FURNITURE_RECIPE).stream().map(RecipeEntry::value).forEach(recipe -> {
               ALL_RECIPES.addAll(recipe.getInnerRecipes());
           });
           ALL_RECIPES.sort(FurnitureRecipe.CraftableFurnitureRecipe::compareTo);
        }
        this.updateInput();
        selectedRecipe.set(-1);
    }

    boolean craft() {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.availableRecipes, this.selectedRecipe.get())) {
            FurnitureRecipe.CraftableFurnitureRecipe simpleFurnitureRecipe = this.sortedRecipes.get(this.selectedRecipe.get());
            if (simpleFurnitureRecipe.matches(playerInventory, playerInventory.player.level())) {
               simpleFurnitureRecipe.craftAndRemoveItems(playerInventory, playerInventory.player.level().registryAccess());
                return true;
            }
        }
        return false;
    }

    void populateResult(Player player) {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.availableRecipes, this.selectedRecipe.get())) {
            FurnitureRecipe.CraftableFurnitureRecipe simpleFurnitureRecipe = this.sortedRecipes.get(this.selectedRecipe.get());
            this.outputSlot.set(simpleFurnitureRecipe.assemble(player.getInventory(), player.level().registryAccess()));
        } else {
            this.outputSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    public boolean isInBounds(List<FurnitureRecipe.CraftableFurnitureRecipe> simpleFurnitureRecipes, int id) {
        if (id >= simpleFurnitureRecipes.size() || id < 0) {
            return false;
        }
        return simpleFurnitureRecipes.contains(this.getSortedRecipes().get(id));
    }

    @Override
    public void slotsChanged(Container inventory) {
        this.updateInput();
        super.slotsChanged(inventory);
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<FurnitureRecipe.CraftableFurnitureRecipe> getAvailableRecipes() {
        return this.availableRecipes;
    }

    public List<FurnitureRecipe.CraftableFurnitureRecipe> getSortedRecipes() {
        return this.sortedRecipes;
    }

    public List<FurnitureRecipe.CraftableFurnitureRecipe> getAllRecipes() {
        return ALL_RECIPES;
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public int getTotalRecipeCount() {
        return this.sortedRecipes.size();
    }

    public int getVisibleRecipeCount() {
        return this.searching ? searchableRecipes.size() : getTotalRecipeCount();
    }

    public void updateInput() {
        // Reset the selected recipe and clear the output slot
        if (!this.availableRecipes.isEmpty() && getSelectedRecipe() != -1) {
            if (!this.sortedRecipes.get(getSelectedRecipe()).matches(playerInventory, level)){
                this.selectedRecipe.set(-1);
                this.outputSlot.set(ItemStack.EMPTY);
            }
        }
        // Reset the available recipes list and add all recipes that can be crafted
        this.availableRecipes.clear();
        this.availableRecipes.addAll(ALL_RECIPES.stream().filter(newFurnitureRecipe -> newFurnitureRecipe.matches(playerInventory, level)).toList());
        // Clear the visible recipe list and add the craft-able recipes first, then add the rest, checking that it's not present already so that it's not overridden.
        this.sortedRecipes.clear();
        this.sortedRecipes.addAll(availableRecipes);
        this.sortedRecipes.addAll(ALL_RECIPES.stream().filter(furnitureRecipe -> !sortedRecipes.contains(furnitureRecipe)).toList());
    }

    public boolean canCraft() {
        return !this.availableRecipes.isEmpty();
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.context, player, PaladinFurnitureModBlocksItems.WORKING_TABLE);
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.outputSlot && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            Item item = itemStack2.getItem();
            itemStack = itemStack2.copy();
            if (index == 0) {
                item.onCraftedBy(itemStack2, player.level(), player);
                if (!this.moveItemStackTo(itemStack2, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack2, itemStack);
            } else if (index >= 1 && index < 28) {
                if (!this.moveItemStackTo(itemStack2, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 37 && !this.moveItemStackTo(itemStack2, 1, 28, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
            this.broadcastChanges();
        }
        return itemStack;
    }


    @Override
    public boolean clickMenuButton(Player player, int id) {
        this.updateInput();
        if (this.isInBounds(this.availableRecipes, id) && canCraft()) {
            this.selectedRecipe.set(id);
            this.populateResult(player);
            return true;
        }
        return false;
    }

    public List<FurnitureRecipe.CraftableFurnitureRecipe> getSearchableRecipes() {
        return searchableRecipes;
    }
}