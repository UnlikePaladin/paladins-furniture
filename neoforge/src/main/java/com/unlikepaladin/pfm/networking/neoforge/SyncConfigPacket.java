package com.unlikepaladin.pfm.networking.neoforge;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncConfigPacket {
    public final Map<String, AbstractConfigOption> configOptions;
    public SyncConfigPacket(Map<String, AbstractConfigOption> configOptions) {
        this.configOptions = configOptions;
    }

    public static void handle(SyncConfigPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if (FMLEnvironment.dist.isClient()) {
                ClientSyncConfigPacketHandler.handlePacket(msg, ctx);
            }
        });
        ctx.setPacketHandled(true);
    }

    public static void encode(SyncConfigPacket packet, FriendlyByteBuf buffer) {
        Collection<AbstractConfigOption> configOptions = packet.configOptions.values();
        buffer.writeCollection(configOptions, AbstractConfigOption::writeConfigOption);
    }

    public static SyncConfigPacket decode(FriendlyByteBuf buffer) {
        Collection<AbstractConfigOption> configOptions = buffer.readCollection(Lists::newArrayListWithCapacity, AbstractConfigOption::readConfigOption);
        Map<String, AbstractConfigOption> map = new HashMap<>();
        configOptions.forEach(abstractConfigOption -> {
            map.put(((TranslatableContents)abstractConfigOption.getTitle().getContents()).getKey(), abstractConfigOption);
        });
        return new SyncConfigPacket(map);
    }
}