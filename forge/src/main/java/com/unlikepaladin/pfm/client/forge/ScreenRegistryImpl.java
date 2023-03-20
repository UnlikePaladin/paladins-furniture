package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;

public class ScreenRegistryImpl {
    public static <T extends ScreenHandler, J extends Screen & ScreenHandlerProvider<T>> void registerScreen(ScreenHandlerType<T> screenType, TriFunc<T, PlayerInventory, Text, J> factory) {
        HandledScreens.register(screenType, factory::apply);
    }
}
