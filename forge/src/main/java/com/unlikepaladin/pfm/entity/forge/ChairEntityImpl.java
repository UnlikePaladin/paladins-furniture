package com.unlikepaladin.pfm.entity.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.forge.ClientPacketsForge;
import com.unlikepaladin.pfm.networking.forge.ToiletUsePacket;
import net.minecraft.util.math.BlockPos;

public class ChairEntityImpl {
    public static void fart(BlockPos pos) {
        ToiletUsePacket usePacket = new ToiletUsePacket(pos);
        ClientPacketsForge.PFM_CLIENT_CHANNEL.sendToServer(usePacket);
    }
}
