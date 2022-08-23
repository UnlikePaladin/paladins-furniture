package com.unlikepaladin.pfm.entity.fabric;

import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class ChairEntityImpl {
    public static void fart(BlockPos pos) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(pos);
        ClientPlayNetworking.send(NetworkIDs.TOILET_USE_ID, passedData);
    }
}
