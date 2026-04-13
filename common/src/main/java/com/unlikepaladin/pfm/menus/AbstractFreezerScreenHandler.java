package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.menus.slots.GenericOutputSlot;
import com.unlikepaladin.pfm.menus.slots.FreezerFuelSlot;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class AbstractFreezerScreenHandler extends RecipeBookMenu{
    private final Container container;
    private final ContainerData dataAccess;
    protected final Level level;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookType category;
    private final RecipePropertySet recipePropertySet;

    protected AbstractFreezerScreenHandler(MenuType<?> type, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType category, int containerId, Inventory playerInventory) {
        this(type, recipeType, category, containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(4));
    }

    protected AbstractFreezerScreenHandler(MenuType<?> type, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType category, int containerId, Inventory playerInventory, Container container, ContainerData dataAccess) {
        super(type, containerId);
        int i;
        this.recipeType = recipeType;
        this.category = category;
        AbstractFreezerScreenHandler.checkContainerSize(container, 3);
        AbstractFreezerScreenHandler.checkContainerDataCount(dataAccess, 4);
        this.container = container;
        container.startOpen(playerInventory.player);
        this.dataAccess = dataAccess;
        this.level = playerInventory.player.getCommandSenderWorld();
        this.recipePropertySet = this.world.getRecipeManager().getPropertySet(RecipeTypes.FREEZING_INPUT);
        this.addSlot(new Slot(container, 0, 56, 17));
        this.addSlot(new FreezerFuelSlot(this, container, 1, 56, 53));
        this.addSlot(new GenericOutputSlot(playerInventory.player, container, 2, 116, 35,0));
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.addDataSlots(dataAccess);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents finder) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)((Object)this.container)).fillStackedContents(finder);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public Container getContainer() {
        return this.container;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index == 2) {
                if (!this.moveItemStackTo(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack2, itemStack);
            } else if (index == 1 || index == 0 ? !this.moveItemStackTo(itemStack2, 3, 39, false) : (this.isFreezeable(itemStack2) ? !this.moveItemStackTo(itemStack2, 0, 1, false) : (this.isFuel(itemStack2) ? !this.moveItemStackTo(itemStack2, 1, 2, false) : (index >= 3 && index < 30 ? !this.moveItemStackTo(itemStack2, 30, 39, false) : index >= 30 && index < 39 && !this.moveItemStackTo(itemStack2, 3, 30, false))))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStack2);
        }
        return itemStack;
    }

    protected boolean isFreezeable(ItemStack itemStack) {
        return this.recipePropertySet.canUse(itemStack);
    }

    public boolean isFuel(ItemStack itemStack) {
        return FreezerBlockEntity.canUseAsFuel(itemStack);
    }

    public int getFreezeProgress() {
        int i = this.dataAccess.get(2);
        int j = this.dataAccess.get(3);
        if (j == 0 || i == 0) {
            return 0;
        }
        return i * 24 / j;
    }

    public int getFuelProgress() {
        int i = this.dataAccess.get(1);
        if (i == 0) {
            i = 200;
        }
        return this.dataAccess.get(0) * 13 / i;
    }

    public boolean isActive() {
        return this.dataAccess.get(0) > 0;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return this.category;
    }

    @Override
    public boolean shouldMoveToInventory(Slot slot) {
        return slot.id != 1;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.id != 1;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public AbstractRecipeScreenHandler.PostFillAction fillInputSlots(boolean craftAll, boolean creative, RecipeEntry<?> recipe, final ServerWorld world, PlayerInventory inventory) {
        final List<Slot> list = List.of(this.getSlot(0), this.getSlot(2));
        RecipeEntry<AbstractCookingRecipe> recipeEntry = (RecipeEntry<AbstractCookingRecipe>) recipe;
        return InputSlotFiller.fill(new InputSlotFiller.Handler<>() {
            public void populateRecipeFinder(RecipeFinder finder) {
                AbstractFreezerScreenHandler.this.populateRecipeFinder(finder);
            }

            public void clear() {
                list.forEach((slot) -> {
                    slot.setStackNoCallbacks(ItemStack.EMPTY);
                });
            }

            public boolean matches(RecipeEntry<AbstractCookingRecipe> entry) {
                return entry.value().matches(new SingleStackRecipeInput(AbstractFreezerScreenHandler.this.inventory.getStack(0)), world);
            }
        }, 1, 1, List.of(this.getSlot(0)), list, inventory, recipeEntry, craftAll, creative);
    }
}

