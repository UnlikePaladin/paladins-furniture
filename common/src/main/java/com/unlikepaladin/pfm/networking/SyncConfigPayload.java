package com.unlikepaladin.pfm.networking;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.*;

public final class SyncConfigPayload implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncConfigPayload> PACKET_CODEC = CustomPacketPayload.codec(SyncConfigPayload::write, SyncConfigPayload::new);
    public static final StreamCodec<FriendlyByteBuf, SyncConfigPayload> PACKET_SIMPLE_CODEC = CustomPacketPayload.codec(SyncConfigPayload::write, SyncConfigPayload::new);

    private final Map<String, AbstractConfigOption> configOptionMap;

    public SyncConfigPayload(Map<String, AbstractConfigOption> configOptionMap) {
        this.configOptionMap = configOptionMap;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkIDs.CONFIG_SYNC_ID;
    }

    public SyncConfigPayload(RegistryFriendlyByteBuf buf) {
        this((FriendlyByteBuf) buf);
    }

    public SyncConfigPayload(FriendlyByteBuf buf) {
        ArrayList<AbstractConfigOption> configOptions = buf.readCollection(Lists::newArrayListWithCapacity, AbstractConfigOption::readConfigOption);
        Map<String, AbstractConfigOption> map = new HashMap<>();
        configOptions.forEach(abstractConfigOption -> {
            map.put(((TranslatableContents) abstractConfigOption.getTitle().getContents()).getKey(), abstractConfigOption);
        });
        this.configOptionMap = map;
    }

    public Map<String, AbstractConfigOption> configOptionMap() {
        return configOptionMap;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        write((FriendlyByteBuf) buf);
    }

    public void write(FriendlyByteBuf buf) {
        Collection<AbstractConfigOption> configOptions = configOptionMap.values();
        buf.writeCollection(configOptions, AbstractConfigOption::writeConfigOption);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SyncConfigPayload) obj;
        return Objects.equals(this.configOptionMap, that.configOptionMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configOptionMap);
    }

    @Override
    public String toString() {
        return "SyncConfigPayload[" +
                "configOptionMap=" + configOptionMap + ']';
    }


}
