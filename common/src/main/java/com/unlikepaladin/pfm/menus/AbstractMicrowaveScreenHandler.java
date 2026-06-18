package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.menus.slots.MicrowaveSlot;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public abstract class AbstractMicrowaveScreenHandler extends RecipeBookMenu<Container> {
    private final Container container;
    private final ContainerData dataAccess;
    protected final Level level;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookType category;
    public boolean isActive;
    public MicrowaveBlockEntity microwaveBlockEntity;
    // Client Constructor
    protected AbstractMicrowaveScreenHandler(MicrowaveBlockEntity microwaveBlockEntity, MenuType<?> type, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType category, int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(microwaveBlockEntity, type, recipeType, category, containerId, playerInventory, new SimpleContainer(1), new SimpleContainerData(2));
        this.isActive = buf.readBoolean();
        microwaveBlockEntity = (MicrowaveBlockEntity) level.getBlockEntity(buf.readBlockPos());
        this.microwaveBlockEntity = microwaveBlockEntity;
    }

    // Server Constructor
    protected AbstractMicrowaveScreenHandler(MicrowaveBlockEntity microwaveBlockEntity, MenuType<?> type, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType category, int containerId, Inventory playerInventory, Container container, ContainerData dataAccess) {
        super(type, containerId);
        this.microwaveBlockEntity = microwaveBlockEntity;
        int i;
        this.recipeType = recipeType;
        this.category = category;
        AbstractMicrowaveScreenHandler.checkContainerSize(container, 1);
        AbstractMicrowaveScreenHandler.checkContainerDataCount(dataAccess, 2);
        this.container = container;
        container.startOpen(playerInventory.player);
        this.dataAccess = dataAccess;
        this.level = playerInventory.player.getCommandSenderWorld();
        this.addSlot(new MicrowaveSlot(playerInventory.player, container, 0, 78, 40));

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
    public void fillCraftSlotsStackedContents(StackedContents finder) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible) this.container).fillStackedContents(finder);
        }
    }

    public boolean getActive() {
        return isActive;
    }

    @ExpectPlatform
    public static void setActive(MicrowaveBlockEntity blockEntity, boolean isActive){
        blockEntity.isActive = isActive;
    }

    @Override
    public void clearCraftingContent() {
        this.getSlot(0).set(ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(RecipeHolder<? extends Recipe<Container>> recipe) {
        return recipe != null && recipe.value() != null && recipe.value().matches(this.container, this.level);
    }

    @Override
    public int getResultSlotIndex() {
        return 1;
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 1;
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
            if (index == 0 ? !this.insertItemToSlot(itemStack2, 3, 37, false) : (this.isCookable(itemStack2) ? !this.insertItemToSlot(itemStack2, 0, 1, false) : (index >= 3 && index < 30 ? !this.insertItemToSlot(itemStack2, 30, 37, false) : index >= 30 && index < 37 && !this.insertItemToSlot(itemStack2, 3, 30, false)))) {
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

    protected boolean insertItemToSlot(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        ItemStack itemStack;
        Slot slot;
        boolean bl = false;
        int i = startIndex;
        if (fromLast) {
            i = endIndex - 1;
        }
        if (!stack.isEmpty()) {
            i = fromLast ? endIndex - 1 : startIndex;
            while (fromLast ? i >= startIndex : i < endIndex) {
                slot = this.slots.get(i);
                itemStack = slot.getItem();
                if (itemStack.isEmpty() && slot.mayPlace(stack)) {
                    if (stack.getCount() > slot.getMaxStackSize()) {
                        slot.set(stack.split(slot.getMaxStackSize()));
                    } else {
                        slot.set(stack.split(stack.getCount()));
                    }
                    slot.setChanged();
                    bl = true;
                    break;
                }
                if (fromLast) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        return bl;
    }

    protected boolean isCookable(ItemStack itemStack) {
        Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> optionalRecipeEntry = this.level.getRecipeManager().getRecipeFor(this.recipeType, new SimpleContainer(itemStack), this.level);
        return optionalRecipeEntry != null && optionalRecipeEntry.isPresent() && optionalRecipeEntry.get().value() != null;
    }

    public int getCookProgress() {
        int i = this.dataAccess.get(0);
        int j = this.dataAccess.get(1);
        if (j == 0 || i == 0) {
            return 0;
        }
        return i * 24 / j;
    }


    public boolean isActive() {
        return this.dataAccess.get(0) != 0;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return this.category;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}

