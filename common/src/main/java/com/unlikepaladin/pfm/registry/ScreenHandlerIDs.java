package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.menus.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlerIDs {

    public static ScreenHandlerType<AbstractFreezerScreenHandler> FREEZER_SCREEN_HANDLER;
    public static ScreenHandlerType<? extends ScreenHandler> STOVE_SCREEN_HANDLER;
    public static ScreenHandlerType<IronStoveScreenHandler> IRON_STOVE_SCREEN_HANDLER;
    public static ScreenHandlerType<MicrowaveScreenHandler> MICROWAVE_SCREEN_HANDLER;
    public static ScreenHandlerType<WorkbenchScreenHandler> WORKBENCH_SCREEN_HANDLER;
    public static ScreenHandlerType<TrashcanScreenHandler> TRASHCAN_SCREEN_HANDLER;
    public static final Identifier FREEZER = Identifier.of(PaladinFurnitureMod.MOD_ID, "freezer_block_entity");

}
