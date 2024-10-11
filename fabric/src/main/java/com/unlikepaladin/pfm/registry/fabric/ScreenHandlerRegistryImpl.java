package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StovePacket;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.function.BiFunction;

public class ScreenHandlerRegistryImpl {

    public static <T extends ScreenHandler, D>  ScreenHandlerType<T> registerScreenHandlerExtended(Identifier id, TriFunc<Integer, PlayerInventory, D, T> factory, PacketCodec<RegistryByteBuf, D> pac) {
        if (pac == null)
            return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<>((syncId, playerInventory) -> factory.apply(syncId, playerInventory, null), FeatureFlags.VANILLA_FEATURES));

        return Registry.register(Registries.SCREEN_HANDLER, id, new ExtendedScreenHandlerType<>(factory::apply, pac));
    }

    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandlerSimple(Identifier id, BiFunction<Integer, PlayerInventory, T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory::apply, FeatureFlags.VANILLA_FEATURES));
    }

    public static <T extends ScreenHandler> Pair<TriFunc<Integer, PlayerInventory, StoveScreenHandler.StoveData, T>, PacketCodec<RegistryByteBuf, StoveScreenHandler.StoveData>> getStoveMenuFactory() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return new Pair<>((TriFunc<Integer, PlayerInventory, StoveScreenHandler.StoveData, T>) PFMCookingForBlockHeadsCompat.getStoveScreenHandler(), PFMCookingForBlockHeadsCompat.getStovePacket()) ;
        }
        else
            return new Pair<>((integer, playerInventory, data) -> (T) new StoveScreenHandler(integer, playerInventory, data), StoveScreenHandler.PACKET_CODEC);
    }
}
