package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.networking.forge.MicrowaveActivePacket;
import com.unlikepaladin.pfm.networking.forge.MicrowaveUpdatePacket;
import com.unlikepaladin.pfm.networking.forge.ToiletUsePacket;
import com.unlikepaladin.pfm.networking.forge.TrashcanClearPacket;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkRegistryForge {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PFM_CHANNEL = NetworkRegistry.newSimpleChannel(
            new Identifier(PaladinFurnitureMod.MOD_ID, "main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int id = 0;
        PFM_CHANNEL.registerMessage(++id, MicrowaveUpdatePacket.class, MicrowaveUpdatePacket::encode, MicrowaveUpdatePacket::decode, MicrowaveUpdatePacket::handle);
        PFM_CHANNEL.registerMessage(++id, MicrowaveActivePacket.class, MicrowaveActivePacket::encode, MicrowaveActivePacket::decode, MicrowaveActivePacket::handle);
        PFM_CHANNEL.registerMessage(++id, ToiletUsePacket.class, ToiletUsePacket::encode, ToiletUsePacket::decode, ToiletUsePacket::handle);
        PFM_CHANNEL.registerMessage(++id, TrashcanClearPacket.class, TrashcanClearPacket::encode, TrashcanClearPacket::decode, TrashcanClearPacket::handle);
    }



}
