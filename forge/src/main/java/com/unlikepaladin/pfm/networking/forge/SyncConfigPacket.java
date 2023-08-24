package com.unlikepaladin.pfm.networking.forge;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncConfigPacket {
    public final Map<String, AbstractConfigOption> configOptions;
    public SyncConfigPacket(Map<String, AbstractConfigOption> configOptions) {
        this.configOptions = configOptions;
    }

    public static void handle(SyncConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientSyncConfigPacketHandler.handlePacket(msg, ctx)));
        ctx.get().setPacketHandled(true);
    }

    public static void encode(SyncConfigPacket packet, PacketByteBuf buffer) {
        //Sync Config
        Collection<AbstractConfigOption> configOptions = packet.configOptions.values();
        //Write length
        buffer.writeInt(configOptions.size());
        //Write Options
        configOptions.forEach(abstractConfigOption -> AbstractConfigOption.writeConfigOption(buffer, abstractConfigOption));
    }

    public static SyncConfigPacket decode(PacketByteBuf buf) {
        int configTotalNum = buf.readInt();
        ArrayList<AbstractConfigOption> configOptions = Lists.newArrayListWithCapacity(configTotalNum); // buf.readCollection(Lists::newArrayListWithCapacity, AbstractConfigOption::readConfigOption);
        for (int i = 0; i < configTotalNum; i++){
            configOptions.add(AbstractConfigOption.readConfigOption(buf));
        }
        Map<String, AbstractConfigOption> map = new HashMap<>();
        configOptions.forEach(abstractConfigOption -> {
            map.put(((TranslatableText)abstractConfigOption.getTitle()).getKey(), abstractConfigOption);
        });
        return new SyncConfigPacket(map);
    }
}
