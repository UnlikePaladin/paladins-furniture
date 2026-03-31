package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.cookingforblockheads.PFMCookingForBlockheads;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import com.unlikepaladin.pfm.registry.TriFunc;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public class ScreenRegistry {
    public static void registerScreens() {
        registerScreen(ScreenHandlerIDs.FREEZER_SCREEN_HANDLER, FreezerScreen::new);
        registerScreen(ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER, WorkbenchScreen::new);
        registerScreen(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, getStoveFactory());
        registerScreen(ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER, IronStoveScreen::new);
        registerScreen(ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER, MicrowaveScreen::new);
        registerScreen(ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER, TrashcanScreen::new);

        PaladinFurnitureMod.pfmModCompatibilities.forEach(pfmModCompatibility -> {
            if (pfmModCompatibility.getClientModCompatiblity().isPresent()){
                pfmModCompatibility.getClientModCompatiblity().get().registerScreens();
            }
        });
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu, J extends Screen & MenuAccess<T>> void registerScreen(MenuType<T> screenType, TriFunc<T, Inventory, Component, J> factory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu, J extends Screen & MenuAccess<T>> TriFunc<T, Inventory, Component, J> getStoveFactory() {
        throw new AssertionError();
    }
}
