package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import com.unlikepaladin.pfm.config.option.Side;
import com.unlikepaladin.pfm.networking.*;
import com.unlikepaladin.pfm.networking.neoforge.*;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkRegistryNeoForge {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.configurationToClient(NetworkIDs.CONFIG_SYNC_ID, SyncConfigPayload.PACKET_SIMPLE_CODEC, (payload, context) ->
                context.enqueueWork(() -> {
            payload.configOptionMap().forEach((title, configOption) -> {
                PFMConfigScreen.isOnServer = true;
                if (configOption.getSide() == Side.SERVER) {
                    LeaveEventHandlerNeoForge.originalConfigValues.put(title, PaladinFurnitureMod.getPFMConfig().options.get(title).getValue());
                    PaladinFurnitureMod.getPFMConfig().options.get(title).setValue(configOption.getValue());
                }
            });
        }));

        registrar.playToServer(NetworkIDs.TRASHCAN_CLEAR, TrashcanClearPayload.PACKET_SIMPLE_CODEC, (payload, context) -> payload.handle(context.player().getServer(), (ServerPlayerEntity) context.player()));

        registrar.playToServer(NetworkIDs.TOILET_USE_ID, ToiletUsePayload.PACKET_SIMPLE_CODEC, (payload, context) -> payload.handle(context.player().getServer(), (ServerPlayerEntity) context.player()));

        registrar.playToServer(NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID, MicrowaveActivatePayload.PACKET_SIMPLE_CODEC, (payload, context) -> payload.handle(context.player().getServer(), (ServerPlayerEntity) context.player()));

        registrar.playToClient(NetworkIDs.MICROWAVE_UPDATE_PACKET_ID, MicrowaveUpdatePayload.PACKET_SIMPLE_CODEC, (payload, context) -> payload.handle(context.player(), MinecraftClient.getInstance()));

    }

    @SubscribeEvent
    public static void onServerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            if (PaladinFurnitureMod.getPFMConfig().shouldGiveGuideBook()) {
                //Give book
                PFMCriteria.GUIDE_BOOK_CRITERION.trigger((ServerPlayerEntity) event.getEntity());
            }
            //Sync Config
            PacketDistributor.sendToPlayer((ServerPlayerEntity) event.getEntity(), new SyncConfigPayload(PaladinFurnitureMod.getPFMConfig().options));
        }
   }
}
