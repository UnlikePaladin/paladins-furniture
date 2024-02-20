package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import com.unlikepaladin.pfm.networking.neoforge.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class NetworkRegistryNeoForge {

    public static void register(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(PaladinFurnitureMod.MOD_ID);
        registrar.play(TrashcanClearPacket.ID, TrashcanClearPacket::new, handler -> handler
                .client(TrashcanClearPacket::handle)
                .server(TrashcanClearPacket::handle));

        registrar.play(ToiletUsePacket.ID, ToiletUsePacket::new, handler -> handler
                .server(ToiletUsePacket::handle));

        registrar.play(SyncConfigPacket.ID, SyncConfigPacket::new, handler -> handler
                .client(SyncConfigPacket::handle));

        registrar.play(MicrowaveUpdatePacket.ID, MicrowaveUpdatePacket::new, handler -> handler
                .client(MicrowaveUpdatePacket::handle));

        registrar.play(MicrowaveActivePacket.ID, MicrowaveActivePacket::new, handler -> handler
                .server(MicrowaveActivePacket::handle));
    }

    @SubscribeEvent
    public static void onServerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            if (PaladinFurnitureMod.getPFMConfig().shouldGiveGuideBook()) {
                //Give book
                PFMCriteria.GUIDE_BOOK_CRITERION.trigger((ServerPlayerEntity) event.getEntity());
            }
            //Sync Config
            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event.getEntity()).send(new SyncConfigPacket(PaladinFurnitureMod.getPFMConfig().options));
        }
   }
}
