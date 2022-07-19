package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.menus.*;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class ScreenHandlersRegistry {

    public static ScreenHandlerType<AbstractFreezerScreenHandler> FREEZER_SCREEN_HANDLER;
    public static ScreenHandlerType<StoveScreenHandler> STOVE_SCREEN_HANDLER;
    public static ScreenHandlerType<IronStoveScreenHandler> IRON_STOVE_SCREEN_HANDLER;
    public static ScreenHandlerType<MicrowaveScreenHandler> MICROWAVE_SCREEN_HANDLER;
    public static ScreenHandlerType<WorkbenchScreenHandler> WORKBENCH_SCREEN_HANDLER;
    public static final Identifier FREEZER = new Identifier(MOD_ID, "freezer_block_entity");

    public static void registerScreenHandlers() {

        WORKBENCH_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID,"furniture"), WorkbenchScreenHandler::new);
        STOVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "stove_block_entity"), StoveScreenHandler::new);
        IRON_STOVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "iron_stove_block_entity"), IronStoveScreenHandler::new);
        MICROWAVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(MOD_ID, "microwave_block_entity"), MicrowaveScreenHandler::new);
        FREEZER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(FREEZER, FreezerScreenHandler::new);

    }
}
