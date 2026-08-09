package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveData;
import com.unlikepaladin.pfm.blocks.blockentities.StovePacket;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.menus.OvenScreenHandler;
import com.unlikepaladin.pfm.menus.OvenScreenHandler;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ScreenHandlerRegistryImpl {
    public static final Map<Identifier, MenuType<?>> screenHandlerMap = new LinkedHashMap<>();
    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandlerSimple(Identifier id, BiFunction<Integer, Inventory, T> factory) {
        MenuType<T> type = new MenuType<>(factory::apply, FeatureFlags.DEFAULT_FLAGS);
        screenHandlerMap.put(id, type);
        return type;
    }

    public static <T extends AbstractContainerMenu, D> MenuType<T> registerScreenHandlerExtended(Identifier id, TriFunc<Integer, Inventory, D, T> factory, StreamCodec<RegistryFriendlyByteBuf, D> pac) {
        if (pac == null) {
            MenuType<T> type = new MenuType<>((syncId, playerInventory) -> factory.apply(syncId, playerInventory, null), FeatureFlags.DEFAULT_FLAGS);
            screenHandlerMap.put(id, type);
            return type;
        } else {
            MenuType<T> type = IForgeMenuType.create((syncId, inventory, buf) -> factory.apply(syncId, inventory, pac.decode(new RegistryFriendlyByteBuf(buf, inventory.player.registryAccess()))));
            screenHandlerMap.put(id, type);
            return type;
        }
    }

    public static <T extends AbstractContainerMenu> Tuple<TriFunc<Integer, Inventory, StoveData, T>, StreamCodec<RegistryFriendlyByteBuf, StoveData>> getStoveMenuFactory() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return new Tuple<>((TriFunc<Integer, Inventory, StoveData, T>) PFMCookingForBlockHeadsCompat.getStoveScreenHandler(), PFMCookingForBlockHeadsCompat.getStovePacket()) ;
        }
        else
            return new Tuple<>((integer, playerInventory, data) -> (T) new OvenScreenHandler(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, integer, playerInventory, data), StoveData.PACKET_CODEC);
    }

    public static Tuple<TriFunc<Integer, Inventory, StoveData, OvenScreenHandler>, StreamCodec<RegistryFriendlyByteBuf, StoveData>> getOvenMenuFactory(){
        return new Tuple<>((integer, playerInventory, data) -> new OvenScreenHandler(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, integer, playerInventory, data), StoveData.PACKET_CODEC);
    }
}
