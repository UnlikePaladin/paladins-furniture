package com.unlikepaladin.pfm.networking.neoforge;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.Identifier;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SyncConfigPacket implements CustomPayload {
    public static final Identifier ID = new Identifier(PaladinFurnitureMod.MOD_ID, "sync_config");
    public final Map<String, AbstractConfigOption> configOptions;
    public SyncConfigPacket(Map<String, AbstractConfigOption> configOptions) {
        this.configOptions = configOptions;
    }

    public SyncConfigPacket(FriendlyByteBuf buffer) {
        Collection<AbstractConfigOption> configOptions = buffer.readCollection(Lists::newArrayListWithCapacity, AbstractConfigOption::readConfigOption);
        Map<String, AbstractConfigOption> map = new HashMap<>();
        configOptions.forEach(abstractConfigOption -> {
            map.put(((TranslatableContents)abstractConfigOption.getTitle().getContents()).getKey(), abstractConfigOption);
        });
        this.configOptions = map;
    }

    public static void handle(SyncConfigPacket msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            if (FMLEnvironment.dist.isClient()) {
                ClientSyncConfigPacketHandler.handlePacket(msg, ctx);
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        Collection<AbstractConfigOption> configOptions = this.configOptions.values();
        buffer.writeCollection(configOptions, AbstractConfigOption::writeConfigOption);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}