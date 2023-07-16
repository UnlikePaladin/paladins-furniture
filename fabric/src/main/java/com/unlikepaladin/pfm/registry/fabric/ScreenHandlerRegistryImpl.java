package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class ScreenHandlerRegistryImpl {

    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandlerExtended(Identifier id, TriFunc<Integer, PlayerInventory, PacketByteBuf, T> factory) {
        return ScreenHandlerRegistry.registerExtended(id, factory::apply);
    }

    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandlerSimple(Identifier id, BiFunction<Integer, PlayerInventory, T> factory) {
        return ScreenHandlerRegistry.registerSimple(id, factory::apply);
    }

    public static <T extends ScreenHandler> TriFunc<Integer, PlayerInventory, PacketByteBuf, T> getStoveMenuFactory() {
        return (integer, playerInventory, packetByteBuf) -> (T) new StoveScreenHandler(integer, playerInventory);
    }
}
