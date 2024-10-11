package com.unlikepaladin.pfm.entity.fabric;

import com.unlikepaladin.pfm.networking.ToiletUsePayload;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class ChairEntityImpl {
    public static void fart(BlockPos pos) {
        ClientPlayNetworking.send(new ToiletUsePayload(pos));
    }
}
