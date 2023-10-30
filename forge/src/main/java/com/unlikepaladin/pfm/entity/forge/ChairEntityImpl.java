package com.unlikepaladin.pfm.entity.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.networking.forge.ToiletUsePacket;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class ChairEntityImpl {
    public static void fart(BlockPos pos) {
        ToiletUsePacket usePacket = new ToiletUsePacket(pos);
        NetworkRegistryForge.PFM_CHANNEL.send(usePacket, MinecraftClient.getInstance().getNetworkHandler().getConnection());
    }
}
