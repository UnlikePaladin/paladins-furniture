package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveData;
import com.unlikepaladin.pfm.blocks.blockentities.StovePacket;
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
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;

import java.util.function.BiFunction;

public class ScreenHandlerRegistry {
    public static void registerScreenHandlers() {
        ScreenHandlerIDs.FREEZER_SCREEN_HANDLER = registerScreenHandlerSimple(ScreenHandlerIDs.FREEZER, FreezerScreenHandler::new);
        ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER = registerScreenHandlerSimple(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"furniture"), WorkbenchScreenHandler::new);
        Tuple<TriFunc<Integer, Inventory, StoveData, AbstractContainerMenu>, StreamCodec<RegistryFriendlyByteBuf, StoveData>> stoveHandler = getStoveMenuFactory();
        ScreenHandlerIDs.STOVE_SCREEN_HANDLER = registerScreenHandlerExtended(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"stove_block_entity"), stoveHandler.getA(), stoveHandler.getB());
        Tuple<TriFunc<Integer, Inventory, StoveData, OvenScreenHandler>, StreamCodec<RegistryFriendlyByteBuf, StoveData>> ovenHandler = getOvenMenuFactory();
        ScreenHandlerIDs.OVEN_SCREEN_HANDLER = registerScreenHandlerExtended(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"oven_block_entity"), ovenHandler.getA(), ovenHandler.getB());
        ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER = registerScreenHandlerExtended(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"microwave_block_entity"), MicrowaveScreenHandler::new, MicrowaveScreenHandler.PACKET_CODEC);
        ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER = registerScreenHandlerExtended(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"trashcan_block_entity"), TrashcanScreenHandler::new, TrashcanScreenHandler.PACKET_CODEC);

        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerScreenHandlers);
    }
    @ExpectPlatform
    public static <T extends AbstractContainerMenu, D> MenuType<T> registerScreenHandlerExtended(Identifier id, TriFunc<Integer, Inventory, D, T> factory, StreamCodec<RegistryFriendlyByteBuf, D> packetCodec) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerSimple(Identifier id, BiFunction<Integer, Inventory, T> factory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> Tuple<TriFunc<Integer, Inventory, StoveData, T>, StreamCodec<RegistryFriendlyByteBuf, StoveData>> getStoveMenuFactory(){
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Tuple<TriFunc<Integer, Inventory, StoveData, OvenScreenHandler>, StreamCodec<RegistryFriendlyByteBuf, StoveData>> getOvenMenuFactory(){
        throw new AssertionError();
    }
}
