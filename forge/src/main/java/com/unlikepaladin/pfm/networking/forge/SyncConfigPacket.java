package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ForgePacketHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SyncConfigPacket {
    public final Map<String, AbstractConfigOption> configOptions;
    public SyncConfigPacket(Map<String, AbstractConfigOption> configOptions) {
        this.configOptions = configOptions;
    }

    public static void handle(ForgePacketHandler forgePacketHandler, SyncConfigPacket msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() ->  ClientSyncConfigPacketHandler.handlePacket(msg, ctx));
        ctx.setPacketHandled(true);
    }

    public static void encode(SyncConfigPacket packet, FriendlyByteBuf buffer) {
        AbstractConfigOption.COLLECTION_STREAM_CODEC.encode(buffer, packet.configOptions.values());
    }

    public static SyncConfigPacket decode(FriendlyByteBuf buffer) {
        Collection<AbstractConfigOption> configOptions = AbstractConfigOption.COLLECTION_STREAM_CODEC.decode(buffer);
        Map<String, AbstractConfigOption> map = new HashMap<>();
        configOptions.forEach(abstractConfigOption -> {
            map.put(((TranslatableContents)abstractConfigOption.getTitle().getContents()).getKey(), abstractConfigOption);
        });
        return new SyncConfigPacket(map);
    }
}
