package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.networking.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class NetworkIDs {
    public static CustomPacketPayload.Type<MicrowaveUpdatePayload> MICROWAVE_UPDATE_PACKET_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "microwave_button_update"));
    public static final CustomPacketPayload.Type<MicrowaveActivatePayload> MICROWAVE_ACTIVATE_PACKET_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "microwave_activate"));
    public static final CustomPacketPayload.Type<TrashcanClearPayload> TRASHCAN_CLEAR = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "trashcan_clear"));
    public static final CustomPacketPayload.Id<SyncRecipesPayload> SYNC_FURNITURE_RECIPES = new CustomPacketPayload.Id<>(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "sync_furniture_recipes"));

    public static CustomPacketPayload.Type<ToiletUsePayload> TOILET_USE_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "toilet_use"));

    public static CustomPacketPayload.Type<SyncConfigPayload> CONFIG_SYNC_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "config_sync"));


}
