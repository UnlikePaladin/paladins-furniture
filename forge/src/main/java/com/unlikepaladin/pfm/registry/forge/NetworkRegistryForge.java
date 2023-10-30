package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import com.unlikepaladin.pfm.networking.forge.*;
import io.netty.util.AttributeKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.*;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NetworkRegistryForge {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PFM_CHANNEL = ChannelBuilder.named(
            new Identifier(PaladinFurnitureMod.MOD_ID, "main_channel")
    ).networkProtocolVersion(Integer.parseInt(PROTOCOL_VERSION)).simpleChannel();
    public static final AttributeKey<ForgePacketHandler> CONTEXT = AttributeKey.newInstance("pfm:handshake");


    public static void registerPackets() {
        int id = 0;

        PFM_CHANNEL.messageBuilder(MicrowaveUpdatePacket.class, ++id).encoder(MicrowaveUpdatePacket::encode).decoder(MicrowaveUpdatePacket::decode).consumerNetworkThread(CONTEXT, MicrowaveUpdatePacket::handle);
        PFM_CHANNEL.messageBuilder(MicrowaveActivePacket.class, ++id).encoder(MicrowaveActivePacket::encode).decoder(MicrowaveActivePacket::decode).consumerNetworkThread(CONTEXT, MicrowaveActivePacket::handle);
        PFM_CHANNEL.messageBuilder(ToiletUsePacket.class, ++id).encoder(ToiletUsePacket::encode).decoder(ToiletUsePacket::decode).consumerNetworkThread(CONTEXT, ToiletUsePacket::handle);
        PFM_CHANNEL.messageBuilder(TrashcanClearPacket.class, ++id).encoder(TrashcanClearPacket::encode).decoder(TrashcanClearPacket::decode).consumerNetworkThread(CONTEXT, TrashcanClearPacket::handle);
        PFM_CHANNEL.messageBuilder(SyncConfigPacket.class, ++id).encoder(SyncConfigPacket::encode).decoder(SyncConfigPacket::decode).consumerNetworkThread(CONTEXT, SyncConfigPacket::handle);
        //PFM_CHANNEL.registerMessage(++id, ResetConfigPacket.class, ResetConfigPacket::encode, ResetConfigPacket::decode, ResetConfigPacket::handle);
    }

    @SubscribeEvent
    public static void onServerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            if (PaladinFurnitureMod.getPFMConfig().shouldGiveGuideBook()) {
                //Give book
                PFMCriteria.GUIDE_BOOK_CRITERION.trigger((ServerPlayerEntity) event.getEntity());

                //Sync Config
                NetworkRegistryForge.PFM_CHANNEL.send(new SyncConfigPacket(PaladinFurnitureMod.getPFMConfig().options), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event.getEntity()));
            }
        }
   }
}
