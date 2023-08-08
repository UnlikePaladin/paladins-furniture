package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.util.Identifier;

public class NetworkIDs {
    public static Identifier MICROWAVE_UPDATE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_button_update");
    public static final Identifier MICROWAVE_ACTIVATE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_activate");
    public static final Identifier TRASHCAN_CLEAR = new Identifier(PaladinFurnitureMod.MOD_ID, "trashcan_clear");

    public static Identifier TOILET_USE_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "toilet_use");

    public static Identifier CONFIG_SYNC_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "config_sync");


}
