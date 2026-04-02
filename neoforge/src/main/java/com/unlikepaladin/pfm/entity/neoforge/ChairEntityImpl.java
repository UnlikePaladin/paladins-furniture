package com.unlikepaladin.pfm.entity.neoforge;

import com.unlikepaladin.pfm.networking.neoforge.ToiletUsePacket;
import com.unlikepaladin.pfm.registry.neoforge.NetworkRegistryNeoForge;
import net.minecraft.core.BlockPos;

public class ChairEntityImpl {
    public static void fart(BlockPos pos) {
        ToiletUsePacket usePacket = new ToiletUsePacket(pos);
        NetworkRegistryNeoForge.PFM_CHANNEL.sendToServer(usePacket);
    }
}
