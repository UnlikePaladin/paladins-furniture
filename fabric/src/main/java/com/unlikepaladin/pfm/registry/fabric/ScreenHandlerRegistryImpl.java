package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.menus.OvenScreenHandler;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public class ScreenHandlerRegistryImpl {

    public static <T extends AbstractContainerMenu, D>  MenuType<T> registerScreenHandlerExtended(ResourceLocation id, TriFunc<Integer, Inventory, D, T> factory, StreamCodec<RegistryFriendlyByteBuf, D> pac) {
        if (pac == null)
            return Registry.register(BuiltInRegistries.MENU, id, new MenuType<>((syncId, playerInventory) -> factory.apply(syncId, playerInventory, null), FeatureFlags.DEFAULT_FLAGS));

        return Registry.register(BuiltInRegistries.MENU, id, new ExtendedScreenHandlerType<>(factory::apply, pac));
    }

    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerSimple(ResourceLocation id, BiFunction<Integer, Inventory, T> factory) {
        return Registry.register(BuiltInRegistries.MENU, id, new MenuType<>(factory::apply, FeatureFlags.DEFAULT_FLAGS));
    }

    public static <T extends AbstractContainerMenu> Tuple<TriFunc<Integer, Inventory, StoveScreenHandler.StoveData, T>, StreamCodec<RegistryFriendlyByteBuf, StoveScreenHandler.StoveData>> getStoveMenuFactory() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return new Tuple<>((TriFunc<Integer, Inventory, StoveScreenHandler.StoveData, T>) PFMCookingForBlockHeadsCompat.getStoveScreenHandler(), PFMCookingForBlockHeadsCompat.getStovePacket()) ;
        }
        else
            return new Tuple<>((integer, playerInventory, data) -> (T) new StoveScreenHandler(integer, playerInventory, data), StoveScreenHandler.PACKET_CODEC);
    }

    public static <T extends AbstractContainerMenu> Tuple<TriFunc<Integer, Inventory, StoveScreenHandler.StoveData, T>, StreamCodec<RegistryFriendlyByteBuf, StoveScreenHandler.StoveData>> getOvenMenuFactory(){
        return new Tuple<>((integer, playerInventory, data) -> (T) new OvenScreenHandler(integer, playerInventory, data), StoveScreenHandler.PACKET_CODEC);
    }
}
