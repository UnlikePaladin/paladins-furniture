package com.unlikepaladin.pfm.client.forge;

import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.Item;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Map;

public interface ItemColorsExtension {
    Map<RegistryEntry.Reference<Item>, ItemColorProvider> getColorMap();
}
