package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import com.unlikepaladin.pfm.networking.neoforge.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NetworkRegistryNeoForge {
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
        PFM_CHANNEL.registerMessage(++id, SyncConfigPacket.class, SyncConfigPacket::encode, SyncConfigPacket::decode, SyncConfigPacket::handle);
        // PFM_CHANNEL.registerMessage(++id, ResetConfigPacket.class, ResetConfigPacket::encode, ResetConfigPacket::decode, ResetConfigPacket::handle);
    }

    @SubscribeEvent
    public static void onServerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            if (PaladinFurnitureMod.getPFMConfig().shouldGiveGuideBook()) {
                //Give book
                PFMCriteria.GUIDE_BOOK_CRITERION.trigger((ServerPlayerEntity) event.getEntity());
            }
            //Sync Config
            NetworkRegistryNeoForge.PFM_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new SyncConfigPacket(PaladinFurnitureMod.getPFMConfig().options));
        }
   }
}
