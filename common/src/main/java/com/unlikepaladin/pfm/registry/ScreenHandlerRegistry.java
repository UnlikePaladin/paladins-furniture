package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.cookingforblockheads.PFMCookingForBlockheads;
import com.unlikepaladin.pfm.menus.*;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public class ScreenHandlerRegistry {
    public static void registerScreenHandlers() {
        ScreenHandlerIDs.FREEZER_SCREEN_HANDLER = registerScreenHandlerSimple(ScreenHandlerIDs.FREEZER, FreezerScreenHandler::new);
        ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER = registerScreenHandlerSimple(new ResourceLocation(PaladinFurnitureMod.MOD_ID,"furniture"), WorkbenchScreenHandler::new);
        ScreenHandlerIDs.STOVE_SCREEN_HANDLER = registerScreenHandlerExtended(new ResourceLocation(PaladinFurnitureMod.MOD_ID,"stove_block_entity"), getStoveMenuFactory());
        ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER = registerScreenHandlerSimple(new ResourceLocation(PaladinFurnitureMod.MOD_ID,"iron_stove_block_entity"), IronStoveScreenHandler::new);
        ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER = registerScreenHandlerExtended(new ResourceLocation(PaladinFurnitureMod.MOD_ID,"microwave_block_entity"), MicrowaveScreenHandler::new);
        ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER = registerScreenHandlerExtended(new ResourceLocation(PaladinFurnitureMod.MOD_ID,"trashcan_block_entity"), TrashcanScreenHandler::new);

        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerScreenHandlers);
    }
    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerExtended(ResourceLocation id, TriFunc<Integer, Inventory, FriendlyByteBuf, T> factory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerSimple(ResourceLocation id, BiFunction<Integer, Inventory, T> factory) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> TriFunc<Integer, Inventory, FriendlyByteBuf, T> getStoveMenuFactory(){
        throw new AssertionError();
    }
}
