package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import com.unlikepaladin.pfm.registry.TriFunc;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;

public class ScreenRegistry {
    public static void registerScreens() {
        registerScreen(ScreenHandlerIDs.FREEZER_SCREEN_HANDLER, FreezerScreen::new);
        registerScreen(ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER, WorkbenchScreen::new);
        registerScreen(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, StoveScreen::new);
        registerScreen(ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER, IronStoveScreen::new);
        registerScreen(ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER, MicrowaveScreen::new);
        registerScreen(ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER, TrashcanScreen::new);
    }

    @ExpectPlatform
    public static <T extends ScreenHandler, J extends Screen & ScreenHandlerProvider<T>> void registerScreen(ScreenHandlerType<T> screenType, TriFunc<T, PlayerInventory, Text, J> factory) {
        throw new RuntimeException();
    }
}
