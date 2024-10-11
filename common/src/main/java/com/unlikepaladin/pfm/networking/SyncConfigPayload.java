package com.unlikepaladin.pfm.networking;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.TranslatableTextContent;

import java.util.*;

public final class SyncConfigPayload implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, SyncConfigPayload> PACKET_CODEC = CustomPayload.codecOf(SyncConfigPayload::write, SyncConfigPayload::new);
    public static final PacketCodec<PacketByteBuf, SyncConfigPayload> PACKET_SIMPLE_CODEC = CustomPayload.codecOf(SyncConfigPayload::write, SyncConfigPayload::new);

    private final Map<String, AbstractConfigOption> configOptionMap;

    public SyncConfigPayload(Map<String, AbstractConfigOption> configOptionMap) {
        this.configOptionMap = configOptionMap;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return NetworkIDs.CONFIG_SYNC_ID;
    }

    public SyncConfigPayload(RegistryByteBuf buf) {
        this((PacketByteBuf) buf);
    }

    public SyncConfigPayload(PacketByteBuf buf) {
        ArrayList<AbstractConfigOption> configOptions = buf.readCollection(Lists::newArrayListWithCapacity, AbstractConfigOption::readConfigOption);
        Map<String, AbstractConfigOption> map = new HashMap<>();
        configOptions.forEach(abstractConfigOption -> {
            map.put(((TranslatableTextContent) abstractConfigOption.getTitle().getContent()).getKey(), abstractConfigOption);
        });
        this.configOptionMap = map;
    }

    public Map<String, AbstractConfigOption> configOptionMap() {
        return configOptionMap;
    }

    public void write(RegistryByteBuf buf) {
        write(buf);
    }

    public void write(PacketByteBuf buf) {
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
