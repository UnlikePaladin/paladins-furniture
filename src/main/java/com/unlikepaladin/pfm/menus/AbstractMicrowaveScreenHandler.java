package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.menus.slots.GenericOutputSlot;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractMicrowaveScreenHandler extends AbstractRecipeScreenHandler<Inventory> {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookCategory category;
    public boolean isActive;
    public MicrowaveBlockEntity microwaveBlockEntity;
    protected AbstractMicrowaveScreenHandler(MicrowaveBlockEntity microwaveBlockEntity, ScreenHandlerType<?> type, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookCategory category, int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(microwaveBlockEntity, type, recipeType, category, syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(4));
        this.isActive = buf.readBoolean();
        microwaveBlockEntity = (MicrowaveBlockEntity) world.getBlockEntity(buf.readBlockPos());
        this.microwaveBlockEntity = microwaveBlockEntity;
    }

    protected AbstractMicrowaveScreenHandler(MicrowaveBlockEntity microwaveBlockEntity, ScreenHandlerType<?> type, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookCategory category, int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(type, syncId);
        this.microwaveBlockEntity = microwaveBlockEntity;
        int i;
        this.recipeType = recipeType;
        this.category = category;
        AbstractMicrowaveScreenHandler.checkSize(inventory, 2);
        AbstractMicrowaveScreenHandler.checkDataCount(propertyDelegate, 2);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.world;
        this.addSlot(new Slot(inventory, 0, 56, 35));
        this.addSlot(new GenericOutputSlot(playerInventory.player, inventory, 1, 116, 35,1));
        isActive = false;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.addProperties(propertyDelegate);

    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        if (this.inventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider) this.inventory).provideRecipeInputs(finder);
        }
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        BlockPos pos = microwaveBlockEntity.getPos();
        passedData.writeBlockPos(pos);
        passedData.writeBoolean(isActive);
        System.out.println("Setting" + isActive + "in handler");

        // Send packet to server to change the block for us
        ClientSidePacketRegistry.INSTANCE.sendToServer(PaladinFurnitureMod.MICROWAVE_PACKET_ID, passedData);
    }

    @Override
    public void clearCraftingSlots() {
        this.getSlot(0).setStack(ItemStack.EMPTY);
        this.getSlot(1).setStack(ItemStack.EMPTY);
    }

    @Override
    public boolean matches(Recipe<? super Inventory> recipe) {
        return recipe.matches(this.inventory, this.world);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 1;
    }

    @Override
    public int getCraftingWidth() {
        return 1;
    }

    @Override
    public int getCraftingHeight() {
        return 1;
    }

    @Override
    public int getCraftingSlotCount() {
        return 2;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 1 || index == 0 ? !this.insertItem(itemStack2, 3, 38, false) : (this.isCookable(itemStack2) ? !this.insertItem(itemStack2, 0, 1, false) : (index >= 3 && index < 30 ? !this.insertItem(itemStack2, 30, 38, false) : index >= 30 && index < 38 && !this.insertItem(itemStack2, 3, 30, false)))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    protected boolean isCookable(ItemStack itemStack) {
        return this.world.getRecipeManager().getFirstMatch(this.recipeType, new SimpleInventory(itemStack), this.world).isPresent();
    }

    public int getCookProgress() {
        int i = this.propertyDelegate.get(0);
        int j = this.propertyDelegate.get(1);
        if (j == 0 || i == 0) {
            return 0;
        }
        return i * 24 / j;
    }


    public boolean isActive() {
        return this.propertyDelegate.get(0) != 0;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != 1;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }
}

