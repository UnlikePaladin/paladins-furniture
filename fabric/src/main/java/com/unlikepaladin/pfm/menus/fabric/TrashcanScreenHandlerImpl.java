package com.unlikepaladin.pfm.menus.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TrashcanScreenHandlerImpl {
    public static void clear(TrashcanBlockEntity trashcanBlockEntity) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        BlockPos pos = trashcanBlockEntity.getPos();
        passedData.writeBlockPos(pos);
        ClientSidePacketRegistryImpl.INSTANCE.sendToServer(NetworkIDs.TRASHCAN_CLEAR, passedData);
    }
}
