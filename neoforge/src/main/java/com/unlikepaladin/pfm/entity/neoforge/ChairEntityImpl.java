package com.unlikepaladin.pfm.entity.neoforge;

import com.unlikepaladin.pfm.registry.neoforge.NetworkRegistryNeoForge;
import net.minecraft.core.BlockPos;
import com.unlikepaladin.pfm.networking.ToiletUsePayload;
import net.neoforged.neoforge.network.PacketDistributor;

public class ChairEntityImpl {
    public static void fart(BlockPos pos) {
        ToiletUsePayload usePacket = new ToiletUsePayload(pos);
        PacketDistributor.sendToServer(usePacket);
    }
}
