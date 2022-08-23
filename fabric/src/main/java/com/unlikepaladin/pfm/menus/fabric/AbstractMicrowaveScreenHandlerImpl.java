package com.unlikepaladin.pfm.menus.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class AbstractMicrowaveScreenHandlerImpl {
        public static void setActive(MicrowaveBlockEntity microwaveBlockEntity, boolean isActive) {
            microwaveBlockEntity.isActive = isActive;
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            BlockPos pos = microwaveBlockEntity.getPos();
            passedData.writeBlockPos(pos);
            passedData.writeBoolean(isActive);
            // Send packet to server to change the block for us
            ClientSidePacketRegistryImpl.INSTANCE.sendToServer(NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID, passedData);
        }
}
