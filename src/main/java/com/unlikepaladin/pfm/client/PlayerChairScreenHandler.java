package com.unlikepaladin.pfm.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;

public class PlayerChairScreenHandler extends Generic3x3ContainerScreenHandler {


    public PlayerChairScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(syncId, playerInventory, inventory);
    }

    public PlayerChairScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(syncId, playerInventory);
    }
        @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
   /* @Override
    public ScreenHandlerType<?> getType() {
        return PaladinFurnitureMod.Player_Chair_Screen_Handler;
    }
*/

}
