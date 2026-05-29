package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.menus.*;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

import java.util.function.BiFunction;

public class ScreenHandlerRegistry {
    public static void registerScreenHandlers() {
        ScreenHandlerIDs.FREEZER_SCREEN_HANDLER = registerScreenHandlerSimple(ScreenHandlerIDs.FREEZER, FreezerScreenHandler::new);
        ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER = registerScreenHandlerSimple(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"furniture"), WorkbenchScreenHandler::new);
        Tuple<TriFunc<Integer, Inventory, StoveScreenHandler.StoveData, AbstractContainerMenu>, StreamCodec<RegistryFriendlyByteBuf, StoveScreenHandler.StoveData>> stoveHandler = getStoveMenuFactory();
        ScreenHandlerIDs.STOVE_SCREEN_HANDLER = registerScreenHandlerExtended(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"stove_block_entity"), stoveHandler.getA(), stoveHandler.getB());
        Tuple<TriFunc<Integer, Inventory, StoveScreenHandler.StoveData, AbstractContainerMenu>, StreamCodec<RegistryFriendlyByteBuf, StoveScreenHandler.StoveData>> ovenMenuFactory = getOvenMenuFactory();
        ScreenHandlerIDs.OVEN_SCREEN_HANDLER = registerScreenHandlerExtended(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"oven_block_entity"), ovenMenuFactory.getA(), ovenMenuFactory.getB());
        ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER = registerScreenHandlerExtended(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"microwave_block_entity"), MicrowaveScreenHandler::new, MicrowaveScreenHandler.PACKET_CODEC);
        ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER = registerScreenHandlerExtended(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"trashcan_block_entity"), TrashcanScreenHandler::new, TrashcanScreenHandler.PACKET_CODEC);

        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerScreenHandlers);
    }
    @ExpectPlatform
    public static <T extends AbstractContainerMenu, D> MenuType<T> registerScreenHandlerExtended(ResourceLocation id, TriFunc<Integer, Inventory, D, T> factory, StreamCodec<RegistryFriendlyByteBuf, D> packetCodec) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerSimple(ResourceLocation id, BiFunction<Integer, Inventory, T> factory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> Tuple<TriFunc<Integer, Inventory, StoveScreenHandler.StoveData, T>, StreamCodec<RegistryFriendlyByteBuf, StoveScreenHandler.StoveData>> getStoveMenuFactory(){
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> Tuple<TriFunc<Integer, Inventory, StoveScreenHandler.StoveData, T>, StreamCodec<RegistryFriendlyByteBuf, StoveScreenHandler.StoveData>> getOvenMenuFactory(){
        throw new AssertionError();
    }
}
