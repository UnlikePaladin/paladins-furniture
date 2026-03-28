package com.unlikepaladin.pfm.entity.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.networking.forge.ToiletUsePacket;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import net.minecraft.core.BlockPos;

public class ChairEntityImpl {
    public static void fart(BlockPos pos) {
        ToiletUsePacket usePacket = new ToiletUsePacket(pos);
        NetworkRegistryForge.PFM_CHANNEL.sendToServer(usePacket);
    }
}
