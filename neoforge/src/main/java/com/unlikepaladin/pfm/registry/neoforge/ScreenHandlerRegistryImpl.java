package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StovePacket;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ScreenHandlerRegistryImpl {
    public static final Map<ResourceLocation, MenuType<?>> screenHandlerMap = new LinkedHashMap<>();
    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerExtended(ResourceLocation id, TriFunc<Integer, Inventory, FriendlyByteBuf, T> factory) {
        MenuType<T> type = IMenuTypeExtension.create(factory::apply);
        screenHandlerMap.put(id, type);
        return type;
    }

    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerSimple(ResourceLocation id, BiFunction<Integer, Inventory, T> factory) {
        MenuType<T> type = new MenuType<>(factory::apply, FeatureFlags.DEFAULT_FLAGS);
        screenHandlerMap.put(id, type);
        return type;
    }

    public static <T extends AbstractContainerMenu, D> MenuType<T> registerScreenHandlerExtended(ResourceLocation id, TriFunc<Integer, Inventory, D, T> factory, StreamCodec<RegistryFriendlyByteBuf, D> pac) {
        if (pac == null) {
            MenuType<T> type = new MenuType<>((syncId, playerInventory) -> factory.apply(syncId, playerInventory, null), FeatureFlags.DEFAULT_FLAGS);
            screenHandlerMap.put(id, type);
            return type;
        } else {
            MenuType<T> type = IMenuTypeExtension.create((syncId, inventory, buf) -> factory.apply(syncId, inventory, pac.decode(buf)));
            screenHandlerMap.put(id, type);
            return type;
        }
    }

    public static <T extends AbstractContainerMenu> Tuple<TriFunc<Integer, Inventory, StoveScreenHandler.StoveData, T>, StreamCodec<RegistryFriendlyByteBuf, StoveScreenHandler.StoveData>> getStoveMenuFactory() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return new Tuple<>((TriFunc<Integer, Inventory, StoveScreenHandler.StoveData, T>) PFMCookingForBlockHeadsCompat.getStoveScreenHandler(), PFMCookingForBlockHeadsCompat.getStovePacket()) ;
        }
        else
            return new Tuple<>((integer, playerInventory, data) -> (T) new StoveScreenHandler(integer, playerInventory, data), StoveScreenHandler.PACKET_CODEC);
    }
}
