package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.menus.OvenScreenHandler;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public class ScreenHandlerRegistryImpl {

    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerExtended(ResourceLocation id, TriFunc<Integer, Inventory, FriendlyByteBuf, T> factory) {
        return ScreenHandlerRegistry.registerExtended(id, factory::apply);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerSimple(ResourceLocation id, BiFunction<Integer, Inventory, T> factory) {
        return ScreenHandlerRegistry.registerSimple(id, factory::apply);
    }

    public static <T extends AbstractContainerMenu> TriFunc<Integer, Inventory, FriendlyByteBuf, T> getStoveMenuFactory() {
        return (integer, playerInventory, packetByteBuf) -> (T) new OvenScreenHandler(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, integer, playerInventory, packetByteBuf);
    }
}
