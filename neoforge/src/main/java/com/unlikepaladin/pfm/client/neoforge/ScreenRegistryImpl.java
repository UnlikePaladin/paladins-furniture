package com.unlikepaladin.pfm.client.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.StoveScreen;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.client.PFMCookingForBlockheadsClient;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.mixin.neoforge.HandledScreensAccessor;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.HashMap;
import java.util.Map;

public class ScreenRegistryImpl {
    public static <T extends ScreenHandler, J extends Screen & ScreenHandlerProvider<T>> void registerScreen(ScreenHandlerType<T> screenType, TriFunc<T, PlayerInventory, Text, J> factory) {
        // fuck the event, i can't be bothered to deal with typecasting bullshit
        HandledScreensAccessor.register(screenType, factory::apply);
    }

    public static <T extends ScreenHandler, J extends Screen & ScreenHandlerProvider<T>> TriFunc<T, PlayerInventory, Text, J> getStoveFactory() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return PFMCookingForBlockheadsClient.getStoveScreen();
        } else {
            return (t, playerInventory, text) -> (J) new StoveScreen((StoveScreenHandler) t, playerInventory, text);
        }
    }
}
