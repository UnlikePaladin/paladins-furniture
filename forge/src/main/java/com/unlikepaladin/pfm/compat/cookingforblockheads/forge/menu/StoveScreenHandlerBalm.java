package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu;

import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.StoveBlockEntityBalm;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.slot.StoveFuelSlot;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.slot.StoveResultSlot;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.networking.ClientStoveResultsPacket;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.blay09.mods.cookingforblockheads.menu.IContainerWithDoor;
import net.blay09.mods.cookingforblockheads.menu.slot.SlotOven;
import net.blay09.mods.cookingforblockheads.menu.slot.SlotOvenFuel;
import net.blay09.mods.cookingforblockheads.menu.slot.SlotOvenTool;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.network.PacketDistributor;

public class StoveScreenHandlerBalm extends AbstractContainerMenu implements IContainerWithDoor {
    private final StoveBlockEntityBalm tileEntity;
    private final RecipePropertySet acceptedInputs;
    private final Player player;
    private NonNullList<ItemStack> resultItems;

    public StoveScreenHandlerBalm(int windowId, Inventory playerInventory, StoveBlockEntityBalm oven) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, windowId);
        this.tileEntity = oven;
        this.acceptedInputs = playerInventory.player.level().recipeAccess().propertySet(RecipePropertySet.SMOKER_INPUT);
        this.resultItems = NonNullList.withSize(9, ItemStack.EMPTY);
        this.player = playerInventory.player;
        oven.startOpen(playerInventory.player);
        Container container = oven.getContainer();
        int offsetX = oven.hasPowerUpgrade() ? -5 : 0;

        int i;
        for(i = 0; i < 3; ++i) {
            this.addSlot(new Slot(container, i, 84 + i * 18 + offsetX, 19));
        }

        this.addSlot(new SlotOvenFuel(this, container, 3, 61 + offsetX, 59));

        for(i = 0; i < 3; ++i) {
            this.addSlot(new StoveResultSlot(playerInventory.player, oven, container, i + 4, 142 + offsetX, 41 + i * 18));
        }

        int j;
        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 3; ++j) {
                this.addSlot(new SlotOven(container, 7 + j + i * 3, 84 + j * 18 + offsetX, 41 + i * 18));
            }
        }

        for(i = 0; i < 4; ++i) {
            this.addSlot(new SlotOvenTool(container, 16 + i, 8, 19 + i * 18, i));
        }

        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 30 + j * 18, 111 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 30 + i * 18, 169));
        }

        this.addDataSlots(oven.getContainerData());
    }

    public StoveBlockEntityBalm getTileEntity() {
        return this.tileEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotIndex >= 7 && slotIndex < 20) {
                if (!this.moveItemStackTo(slotStack, 20, 56, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 4 && slotIndex <= 6) {
                if (!this.moveItemStackTo(slotStack, 20, 56, false)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemStack);
            } else if (slotIndex >= 20) {
                if (StoveBlockEntityBalm.isItemFuel(tileEntity.getLevel(), slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (acceptedInputs.test(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 20 && slotIndex < 47) {
                    if (!this.moveItemStackTo(slotStack, 47, 56, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 47 && slotIndex < 56 && !this.moveItemStackTo(slotStack, 20, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 20, 47, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public boolean isTileEntity(BlockEntity blockEntity) {
        return this.tileEntity == blockEntity;
    }

    @Override
    public void removed(Player player){
        super.removed(player);
        this.tileEntity.stopOpen(player);
    }

    public boolean isFuel(ItemStack itemStack) {
        return OvenBlockEntity.isItemFuel(this.tileEntity.getLevel(), itemStack);
    }

    public static class SlotOvenFuel extends Slot {
        private final StoveScreenHandlerBalm menu;

        public SlotOvenFuel(StoveScreenHandlerBalm menu, Container container, int i, int x, int y) {
            super(container, i, x, y);
            this.menu = menu;
        }

        public boolean canInsert(ItemStack itemStack) {
            return this.menu.isFuel(itemStack);
        }
    }

    public void setResultItems(NonNullList<ItemStack> itemStacks) {
        this.resultItems = itemStacks;
    }

    public NonNullList<ItemStack> getResultItems() {
        return resultItems;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (!this.player.isLocalPlayer()) {
            boolean changes = false;

            for(int i = 0; i < 3; ++i) {
                for(int j = 0; j < 3; ++j) {
                    ItemStack result = this.tileEntity.getSmeltingResult((this.slots.get(7 + j + i * 3)).getItem(), (ServerLevel) player.level());
                    if (!ItemStack.isSameItemSameComponents(this.resultItems.get(j+i*3), result)) {
                        this.resultItems.set(j+i*3, result);
                        changes = true;
                    }
                }
            }

            if (changes) {
                NetworkRegistryForge.PFM_CHANNEL.send(new ClientStoveResultsPacket(this.resultItems), PacketDistributor.PLAYER.with((ServerPlayer) player));
            }
        }

    }
}