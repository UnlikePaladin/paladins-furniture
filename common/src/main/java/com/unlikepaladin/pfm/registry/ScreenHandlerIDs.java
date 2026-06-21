package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.menus.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;

public class ScreenHandlerIDs {

    public static MenuType<AbstractFreezerScreenHandler> FREEZER_SCREEN_HANDLER;
    public static MenuType<? extends AbstractContainerMenu> STOVE_SCREEN_HANDLER;
    public static MenuType<OvenScreenHandler> OVEN_SCREEN_HANDLER;
    public static MenuType<MicrowaveScreenHandler> MICROWAVE_SCREEN_HANDLER;
    public static MenuType<WorkbenchScreenHandler> WORKBENCH_SCREEN_HANDLER;
    public static MenuType<TrashcanScreenHandler> TRASHCAN_SCREEN_HANDLER;
    public static final ResourceLocation FREEZER = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "freezer_block_entity");

}
