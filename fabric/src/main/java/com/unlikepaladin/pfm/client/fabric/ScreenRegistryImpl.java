package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.client.screens.StoveScreen;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.chat.Component;

public class ScreenRegistryImpl {
    public static <T extends AbstractContainerMenu, J extends Screen & MenuAccess<T>> void registerScreen(MenuType<T> screenType, TriFunc<T, Inventory, Component, J> factory) {
        ScreenRegistry.register(screenType, factory::apply);
    }

    public static <T extends AbstractContainerMenu, J extends Screen & MenuAccess<T>> TriFunc<T, Inventory, Component, J> getStoveFactory() {
        return (t, playerInventory, text) -> (J) new StoveScreen((StoveScreenHandler) t, playerInventory, text);
    }
}
