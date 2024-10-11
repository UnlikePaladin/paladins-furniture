
package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrashcanScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    protected final World world;
    public TrashcanBlockEntity trashcanBlockEntity;
    public TrashcanScreenHandler(int syncId, PlayerInventory playerInventory, TrashCanData canData) {
        this(null, syncId, playerInventory, new SimpleInventory(9));
        trashcanBlockEntity = (TrashcanBlockEntity) world.getBlockEntity(canData.pos());
    }

    public TrashcanScreenHandler(TrashcanBlockEntity trashcanBlockEntity, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER, syncId);
        int j;
        int i;
        this.trashcanBlockEntity = trashcanBlockEntity;
        TrashcanScreenHandler.checkSize(inventory, 9);
        this.inventory = inventory;
        this.world = playerInventory.player.getWorld();
        inventory.onOpen(playerInventory.player);
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                this.addSlot(new Slot(inventory, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @ExpectPlatform
    public static void clear(TrashcanBlockEntity trashcanBlockEntity) {
        throw new AssertionError("Should've been replaced");
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < 9 ? !this.insertItem(itemStack2, 9, 45, true) : !this.insertItem(itemStack2, 0, 9, false)) {
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

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    public static final PacketCodec<RegistryByteBuf, TrashCanData> PACKET_CODEC = PacketCodec.of(TrashCanData::write, TrashCanData::new);
    public record TrashCanData(BlockPos pos) {
        public TrashCanData(RegistryByteBuf buf) {
            this(buf.readBlockPos());
        }
        public void write(RegistryByteBuf buf) {
            buf.writeBlockPos(pos);
        }
    }
}
