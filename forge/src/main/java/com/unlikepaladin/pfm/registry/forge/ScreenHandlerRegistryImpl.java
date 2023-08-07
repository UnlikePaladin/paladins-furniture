package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
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

    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandlerSimple(Identifier id, BiFunction<Integer, PlayerInventory, T> factory) {
        ScreenHandlerType<T> type = new ScreenHandlerType<>(factory::apply, FeatureFlags.DEFAULT_ENABLED_FEATURES);
        screenHandlerMap.put(id, type);
        return type;
    }

    public static <T extends ScreenHandler> TriFunc<Integer, PlayerInventory, PacketByteBuf, T> getStoveMenuFactory() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return PFMCookingForBlockHeadsCompat.getStoveScreenHandler();
        }
        else
            return (integer, playerInventory, packetByteBuf) -> (T) new StoveScreenHandler(integer, playerInventory);
    }
}
