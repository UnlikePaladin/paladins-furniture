package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StovePacket;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.cookingforblockheads.PFMCookingForBlockheads;
import com.unlikepaladin.pfm.menus.*;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.function.BiFunction;

public class ScreenHandlerRegistry {
    public static void registerScreenHandlers() {
        ScreenHandlerIDs.FREEZER_SCREEN_HANDLER = registerScreenHandlerSimple(ScreenHandlerIDs.FREEZER, FreezerScreenHandler::new);
        ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER = registerScreenHandlerSimple(new Identifier(PaladinFurnitureMod.MOD_ID,"furniture"), WorkbenchScreenHandler::new);
        Pair<TriFunc<Integer, PlayerInventory, StoveScreenHandler.StoveData, ScreenHandler>, PacketCodec<RegistryByteBuf, StoveScreenHandler.StoveData>> stoveHandler = getStoveMenuFactory();
        ScreenHandlerIDs.STOVE_SCREEN_HANDLER = registerScreenHandlerExtended(new Identifier(PaladinFurnitureMod.MOD_ID,"stove_block_entity"), stoveHandler.getLeft(), stoveHandler.getRight());
        ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER = registerScreenHandlerSimple(new Identifier(PaladinFurnitureMod.MOD_ID,"iron_stove_block_entity"), IronStoveScreenHandler::new);
        ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER = registerScreenHandlerExtended(new Identifier(PaladinFurnitureMod.MOD_ID,"microwave_block_entity"), MicrowaveScreenHandler::new, MicrowaveScreenHandler.PACKET_CODEC);
        ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER = registerScreenHandlerExtended(new Identifier(PaladinFurnitureMod.MOD_ID,"trashcan_block_entity"), TrashcanScreenHandler::new, TrashcanScreenHandler.PACKET_CODEC);

        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerScreenHandlers);
    }
    @ExpectPlatform
    public static <T extends ScreenHandler, D> ScreenHandlerType<T> registerScreenHandlerExtended(Identifier id, TriFunc<Integer, PlayerInventory, D, T> factory, PacketCodec<RegistryByteBuf, D> pac) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandlerSimple(Identifier id, BiFunction<Integer, PlayerInventory, T> factory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <T extends ScreenHandler> Pair<TriFunc<Integer, PlayerInventory, StoveScreenHandler.StoveData, T>, PacketCodec<RegistryByteBuf, StoveScreenHandler.StoveData>> getStoveMenuFactory(){
        throw new AssertionError();
    }
}
