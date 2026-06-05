package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.menus.slots.OvenFuelSlot;
import com.unlikepaladin.pfm.menus.slots.OvenProcessingSlot;
import com.unlikepaladin.pfm.menus.slots.OvenResultSlot;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
// import net.minecraft.network.FriendlyByteBuf; // unused
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class OvenScreenHandler extends RecipeBookMenu<Container> {
    // container layout (container slot indices)
    public static final int INPUT_SLOT_START = 0;
    public static final int INPUT_SLOT_COUNT = 3;
    public static final int PROCESSING_SLOT_START = INPUT_SLOT_START + INPUT_SLOT_COUNT; // 3
    public static final int PROCESSING_SLOT_COUNT = 9;
    public static final int OUTPUT_SLOT_START = PROCESSING_SLOT_START + PROCESSING_SLOT_COUNT; // 12
    public static final int OUTPUT_SLOT_COUNT = 3;
    public static final int FUEL_SLOT_INDEX = OUTPUT_SLOT_START + OUTPUT_SLOT_COUNT; // 15
    public static final int SLOT_COUNT = FUEL_SLOT_INDEX + 1; // total container slots (16)
    public static final int DATA_COUNT = 4;
    private int invSlotStart;
    private int invSlotEnd;
    private int hotbarSlotStart;
    private int hotbarSlotEnd;
    // screen indices for container slot groups (computed while adding slots)
    private int inputScreenStart;
    private int inputScreenEnd;
    private int processingScreenStart;
    private int processingScreenEnd;
    private int outputScreenStart;
    private int outputScreenEnd;
    private int fuelScreenIndex;
    private Container container;
    private final ContainerData data;
    protected final Level level;
    protected static int SLOT_DIMENSION = 18;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookType recipeBookType;

    public OvenScreenHandler(MenuType<? extends AbstractContainerMenu> menuType, int containerId, Inventory inventory, FriendlyByteBuf byteBuf) {
        // client-side menu: create a PropertyDelegate sized to match the server-side layout
        this(menuType, containerId, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(2 + PROCESSING_SLOT_COUNT * 2));
        BlockPos pos = byteBuf.readBlockPos();
        if (inventory.player.level.getBlockEntity(pos) instanceof OvenBlockEntity oven) {
            this.container = oven;
        }
    }

    public OvenScreenHandler(MenuType<? extends AbstractContainerMenu> menuType,int containerId, Inventory inventory, Container container, ContainerData containerData) {
        super(menuType, containerId);
        this.recipeType = RecipeType.SMOKING;
        this.recipeBookType = RecipeBookType.SMOKER;
        checkContainerSize(container, SLOT_COUNT);
        this.data = containerData;
        this.level = inventory.player.level;
        this.container = container;
        this.container.startOpen(inventory.player);
        // input slots
        this.inputScreenStart = this.slots.size();
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            this.addSlot(new Slot(container, INPUT_SLOT_START + i, 59 + i * SLOT_DIMENSION, 19));
        }
        this.inputScreenEnd = this.inputScreenStart + INPUT_SLOT_COUNT;

        // processing slots (9) - arrange in 3x3 grid below inputs
        this.processingScreenStart = this.slots.size();
        int procGridX = 59;
        int procGridY = 41;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                int idx = PROCESSING_SLOT_START + r * 3 + c;
                this.addSlot(new OvenProcessingSlot(container, idx, procGridX + c * SLOT_DIMENSION, procGridY + r * SLOT_DIMENSION));
            }
        }
        this.processingScreenEnd = this.processingScreenStart + PROCESSING_SLOT_COUNT;

        // output slots (3)
        this.outputScreenStart = this.slots.size();
        for (int o = 0; o < OUTPUT_SLOT_COUNT; o++) {
            this.addSlot(new OvenResultSlot<>(inventory.player, container, OUTPUT_SLOT_START + o, 117, 41 + o * SLOT_DIMENSION));
        }
        this.outputScreenEnd = this.outputScreenStart + OUTPUT_SLOT_COUNT;

        // fuel slot
        this.fuelScreenIndex = this.slots.size();
        this.addSlot(new OvenFuelSlot(this, container, FUEL_SLOT_INDEX, 37, 59));

        // record the index where the player inventory begins (screen slot index)
        this.invSlotStart = this.slots.size();
        this.invSlotEnd = this.invSlotStart + 27; // 3 rows * 9 columns
        this.hotbarSlotStart = this.invSlotEnd;
        this.hotbarSlotEnd = this.hotbarSlotStart + 9; // 9 hotbar slots

        int inventorySlotYOffset = 113;
        int inventorySlotHorizontalOffset = 8;
        // inventory slots
        for (int rowIndex = 0; rowIndex < 3; ++rowIndex) {
            for (int columnIndex = 0; columnIndex < 9; ++columnIndex) {
                int invSlotIdx = columnIndex + rowIndex * 9 + 9;
                this.addSlot(new Slot(inventory, invSlotIdx, inventorySlotHorizontalOffset + columnIndex * SLOT_DIMENSION, inventorySlotYOffset + rowIndex * SLOT_DIMENSION));
            }
        }

        int hotBarOffset = 171;
        for (int columnIndex = 0; columnIndex < 9; ++columnIndex) {
            this.addSlot(new Slot(inventory, columnIndex, inventorySlotHorizontalOffset + columnIndex * SLOT_DIMENSION, hotBarOffset));
        }

        this.addDataSlots(containerData);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.container).fillStackedContents(stackedContents);
        }
    }

    @Override
    public void clearCraftingContent() {
        // Clear container-side ingredient/result slots if available
        if (this.container != null) {
            // clear inputs
            for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
                int idx = INPUT_SLOT_START + i;
                if (this.container.getContainerSize() > idx) this.container.setItem(idx, ItemStack.EMPTY);
            }
            // clear outputs
            for (int o = 0; o < OUTPUT_SLOT_COUNT; o++) {
                int idx = OUTPUT_SLOT_START + o;
                if (this.container.getContainerSize() > idx) this.container.setItem(idx, ItemStack.EMPTY);
            }
        }

        // Also clear the corresponding screen slots if they exist (inputs + outputs)
        if (!this.slots.isEmpty()) {
            for (int s = this.inputScreenStart; s < this.inputScreenEnd && s < this.slots.size(); s++) {
                this.getSlot(s).set(ItemStack.EMPTY);
            }
            for (int s = this.outputScreenStart; s < this.outputScreenEnd && s < this.slots.size(); s++) {
                this.getSlot(s).set(ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean recipeMatches(Recipe<? super Container> recipe) {
        return recipe.matches(this.container, this.level);
    }

    @Override
    public int getResultSlotIndex() {
        // return first output screen index (used by recipe book UI)
        return this.outputScreenStart;
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
        return this.container.getContainerSize();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            // If clicked an output slot -> move to player inventory
            if (slotIndex >= this.outputScreenStart && slotIndex < this.outputScreenEnd) {
                if (!this.moveItemStackTo(itemStack2, this.invSlotStart, this.hotbarSlotEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            }
            // clicked some other container slot -> move to player inventory
            else if (slotIndex < this.invSlotStart) {
                if (!this.moveItemStackTo(itemStack2, this.invSlotStart, this.hotbarSlotEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // clicked player inventory -> try to move into inputs then fuel
            else {
                if (this.canSmelt(itemStack2)) {
                    // move to input slots
                    if (!this.moveItemStackTo(itemStack2, this.inputScreenStart, this.inputScreenEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemStack2)) {
                    // move to fuel slot
                    if (!this.moveItemStackTo(itemStack2, this.fuelScreenIndex, this.fuelScreenIndex + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= this.invSlotStart && slotIndex < this.invSlotEnd) {
                    if (!this.moveItemStackTo(itemStack2, this.hotbarSlotStart, this.hotbarSlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= this.hotbarSlotStart && slotIndex < this.hotbarSlotEnd) {
                    if (!this.moveItemStackTo(itemStack2, this.invSlotStart, this.invSlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
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

    protected boolean canSmelt(ItemStack itemStack) {
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, new SimpleContainer(itemStack), this.level).isPresent();
    }

    public boolean isFuel(ItemStack itemStack) {
        return AbstractFurnaceBlockEntity.isFuel(itemStack);
    }

    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    // badumm tss, get it, overcooked, not that funnny...
    public int getOverovercookProgress(int slotIndex, int scale) {
        int dataIdx = 2 + (slotIndex * 2);
        int cookTime = this.data.get(dataIdx);
        int cookTimeTotal = this.data.get(dataIdx + 1);

        if (cookTimeTotal == 0 || cookTime <= cookTimeTotal) {
            return 0;
        }

        // The burning phase spans from cookTimeTotal to (cookTimeTotal * 2)
        // So the progress inside this phase is: current - total
        int burnProgress = cookTime - cookTimeTotal;

        return Math.min(scale, (burnProgress * scale) / cookTimeTotal);
    }

    public int getCookProgress(int slotIndex, int scale) {
        // Map the 0-8 slot index back to the ContainerData array positions
        int dataIdx = 2 + (slotIndex * 2);

        int cookTime = this.data.get(dataIdx);
        int cookTimeTotal = this.data.get(dataIdx + 1);

        if (cookTimeTotal == 0 || cookTime == 0) {
            return 0;
        }

        return cookTime * scale / cookTimeTotal;
    }

    public boolean isSlotOverheating(int slotIndex) {
        int dataIdx = 2 + (slotIndex * 2);
        int cookTime = this.data.get(dataIdx);
        int cookTimeTotal = this.data.get(dataIdx + 1);

        // If cookTime is greater than cookTimeTotal, it means it's already cooked
        // and is currently burning/charring down toward charcoal.
        return cookTime > cookTimeTotal;
    }

    public boolean isSlotCooking(int slotIndex) {
        int dataIdx = 2 + (slotIndex * 2);
        return this.data.get(dataIdx) > 0;
    }

    public int getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.data.get(0) * 13 / i;
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    public boolean shouldMoveToInventory(int i) {
        return i != 1;
    }
}