package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeMenuType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ScreenHandlerRegistryImpl {
    public static final Map<Identifier, ScreenHandlerType<?>> screenHandlerMap = new LinkedHashMap<>();
    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandlerExtended(Identifier id, TriFunc<Integer, PlayerInventory, PacketByteBuf, T> factory) {
        ScreenHandlerType<T> type = IForgeMenuType.create(factory::apply);
        screenHandlerMap.put(id, type);
        return type;
    }

    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerSimple(ResourceLocation id, BiFunction<Integer, Inventory, T> factory) {
        MenuType<T> type = new MenuType<>(factory::apply);
        screenHandlerMap.put(id, type);
        return type;
    }

    public static <T extends AbstractContainerMenu> TriFunc<Integer, Inventory, FriendlyByteBuf, T> getStoveMenuFactory() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return PFMCookingForBlockHeadsCompat.getStoveScreenHandler();
        }
        else
            return (integer, playerInventory, packetByteBuf) -> (T) new StoveScreenHandler(integer, playerInventory);
    }
}
