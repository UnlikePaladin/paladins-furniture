package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.networking.forge.MicrowaveActivePacket;
import com.unlikepaladin.pfm.networking.forge.MicrowaveUpdatePacket;
import net.minecraft.util.Identifier;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class ClientPacketsForge {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PFM_CLIENT_CHANNEL = NetworkRegistry.newSimpleChannel(
            new Identifier(PaladinFurnitureMod.MOD_ID, "client"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );


    public static void registerClientPackets() {
        int id = 0;
        PFM_CLIENT_CHANNEL.registerMessage(++id, MicrowaveUpdatePacket.class, MicrowaveUpdatePacket::encode, MicrowaveUpdatePacket::decode, MicrowaveUpdatePacket::handle);
    }
}
