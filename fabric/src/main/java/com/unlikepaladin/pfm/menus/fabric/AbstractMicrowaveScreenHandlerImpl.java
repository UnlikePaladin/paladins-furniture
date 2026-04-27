package com.unlikepaladin.pfm.menus.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

public class AbstractMicrowaveScreenHandlerImpl {
        public static void setActive(MicrowaveBlockEntity microwaveBlockEntity, boolean isActive) {
            microwaveBlockEntity.isActive = isActive;
            FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
            BlockPos pos = microwaveBlockEntity.getBlockPos();
            passedData.writeBlockPos(pos);
            passedData.writeBoolean(isActive);
            // Send packet to server to change the block for us
            ClientSidePacketRegistryImpl.INSTANCE.sendToServer(NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID, passedData);
        }
}
