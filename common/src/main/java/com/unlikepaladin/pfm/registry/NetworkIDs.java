package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.networking.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class NetworkIDs {
    public static CustomPacketPayload.Type<MicrowaveUpdatePayload> MICROWAVE_UPDATE_PACKET_ID = new CustomPacketPayload.Type<>(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "microwave_button_update"));
    public static final CustomPacketPayload.Type<MicrowaveActivatePayload> MICROWAVE_ACTIVATE_PACKET_ID = new CustomPacketPayload.Type<>(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "microwave_activate"));
    public static final CustomPacketPayload.Type<TrashcanClearPayload> TRASHCAN_CLEAR = new CustomPacketPayload.Type<>(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "trashcan_clear"));

    public static CustomPacketPayload.Type<ToiletUsePayload> TOILET_USE_ID = new CustomPacketPayload.Type<>(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "toilet_use"));

    public static CustomPacketPayload.Type<SyncConfigPayload> CONFIG_SYNC_ID = new CustomPacketPayload.Type<>(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "config_sync"));


}
