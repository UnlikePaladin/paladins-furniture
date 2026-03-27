package com.unlikepaladin.pfm.entity.fabric;

import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

public class ChairEntityImpl {
    public static void fart(BlockPos pos) {
        FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(pos);
        ClientPlayNetworking.send(NetworkIDs.TOILET_USE_ID, passedData);
    }
}
