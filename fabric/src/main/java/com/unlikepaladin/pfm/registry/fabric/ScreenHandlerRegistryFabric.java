package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.menus.*;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.util.Identifier;

public class ScreenHandlerRegistryFabric {

    public static void registerScreenHandlers() {
        ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(PaladinFurnitureMod.MOD_ID,"furniture"), WorkbenchScreenHandler::new);
        ScreenHandlerIDs.STOVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(PaladinFurnitureMod.MOD_ID, "stove_block_entity"), StoveScreenHandler::new);
        ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(PaladinFurnitureMod.MOD_ID, "iron_stove_block_entity"), IronStoveScreenHandler::new);
        ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_block_entity"), MicrowaveScreenHandler::new);
        ScreenHandlerIDs.FREEZER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(ScreenHandlerIDs.FREEZER, FreezerScreenHandler::new);
        ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(PaladinFurnitureMod.MOD_ID, "trashcan_block_entity"), TrashcanScreenHandler::new);
    }
}
