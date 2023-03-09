package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.config.option.Side;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Supplier;

public class ClientResetConfigPacketHandler {
    public static void handlePacket(ResetConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
    }
}
