package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StovePacket;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ScreenHandlerRegistryImpl {
    public static final Map<Identifier, ScreenHandlerType<?>> screenHandlerMap = new LinkedHashMap<>();
    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandlerExtended(Identifier id, TriFunc<Integer, PlayerInventory, PacketByteBuf, T> factory) {
        ScreenHandlerType<T> type = IMenuTypeExtension.create(factory::apply);
        screenHandlerMap.put(id, type);
        return type;
    }

    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandlerSimple(Identifier id, BiFunction<Integer, PlayerInventory, T> factory) {
        ScreenHandlerType<T> type = new ScreenHandlerType<>(factory::apply, FeatureFlags.DEFAULT_ENABLED_FEATURES);
        screenHandlerMap.put(id, type);
        return type;
    }

    public static <T extends ScreenHandler, D> ScreenHandlerType<T> registerScreenHandlerExtended(Identifier id, TriFunc<Integer, PlayerInventory, D, T> factory, PacketCodec<RegistryByteBuf, D> pac) {
        if (pac == null) {
            ScreenHandlerType<T> type = new ScreenHandlerType<>((syncId, playerInventory) -> factory.apply(syncId, playerInventory, null), FeatureFlags.DEFAULT_ENABLED_FEATURES);
            screenHandlerMap.put(id, type);
            return type;
        } else {
            ScreenHandlerType<T> type = IMenuTypeExtension.create((syncId, inventory, buf) -> factory.apply(syncId, inventory, pac.decode(buf)));
            screenHandlerMap.put(id, type);
            return type;
        }
    }

    public static <T extends ScreenHandler> Pair<TriFunc<Integer, PlayerInventory, StoveScreenHandler.StoveData, T>, PacketCodec<RegistryByteBuf, StoveScreenHandler.StoveData>> getStoveMenuFactory() {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return new Pair<>((TriFunc<Integer, PlayerInventory, StoveScreenHandler.StoveData, T>) PFMCookingForBlockHeadsCompat.getStoveScreenHandler(), PFMCookingForBlockHeadsCompat.getStovePacket()) ;
        }
        else
            return new Pair<>((integer, playerInventory, data) -> (T) new StoveScreenHandler(integer, playerInventory, data), StoveScreenHandler.PACKET_CODEC);
    }
}
