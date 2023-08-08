package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.client.screens.StoveScreen;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;

public class ScreenRegistryImpl {
    public static <T extends ScreenHandler, J extends Screen & ScreenHandlerProvider<T>> void registerScreen(ScreenHandlerType<T> screenType, TriFunc<T, PlayerInventory, Text, J> factory) {
        ScreenRegistry.register(screenType, factory::apply);
    }

    public static <T extends ScreenHandler, J extends Screen & ScreenHandlerProvider<T>> TriFunc<T, PlayerInventory, Text, J> getStoveFactory() {
        return (t, playerInventory, text) -> (J) new StoveScreen((StoveScreenHandler) t, playerInventory, text);
    }
}
