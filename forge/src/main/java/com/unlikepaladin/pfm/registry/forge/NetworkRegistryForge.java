package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.networking.forge.MicrowaveActivePacket;
import com.unlikepaladin.pfm.networking.forge.ToiletUsePacket;
import net.minecraft.util.Identifier;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class NetworkRegistryForge {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PFM_SERVER_CHANNEL = NetworkRegistry.newSimpleChannel(
            new Identifier(PaladinFurnitureMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int id = 0;
        PFM_SERVER_CHANNEL.registerMessage(++id, MicrowaveActivePacket.class, MicrowaveActivePacket::encode, MicrowaveActivePacket::decode, MicrowaveActivePacket::handle);
        PFM_SERVER_CHANNEL.registerMessage(++id, ToiletUsePacket.class, ToiletUsePacket::encode, ToiletUsePacket::decode, ToiletUsePacket::handle);
    }



}
