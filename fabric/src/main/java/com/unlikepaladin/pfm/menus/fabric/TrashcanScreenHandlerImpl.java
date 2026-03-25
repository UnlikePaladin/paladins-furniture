package com.unlikepaladin.pfm.menus.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

public class TrashcanScreenHandlerImpl {
    public static void clear(TrashcanBlockEntity trashcanBlockEntity) {
        FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
        BlockPos pos = trashcanBlockEntity.getBlockPos();
        passedData.writeBlockPos(pos);
        ClientSidePacketRegistryImpl.INSTANCE.sendToServer(NetworkIDs.TRASHCAN_CLEAR, passedData);
    }
}
