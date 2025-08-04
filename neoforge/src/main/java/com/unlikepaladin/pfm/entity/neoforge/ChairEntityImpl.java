package com.unlikepaladin.pfm.entity.neoforge;

import com.unlikepaladin.pfm.networking.ToiletUsePayload;
import net.minecraft.util.math.BlockPos;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ChairEntityImpl {
    public static void fart(BlockPos pos) {
        ToiletUsePayload usePacket = new ToiletUsePayload(pos);
        ClientPacketDistributor.sendToServer(usePacket);
    }
}
