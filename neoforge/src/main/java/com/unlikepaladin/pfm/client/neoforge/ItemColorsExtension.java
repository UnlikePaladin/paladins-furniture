package com.unlikepaladin.pfm.client.neoforge;

import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Map;

public interface ItemColorsExtension {
    Map<Item, ItemColorProvider> getColorMap();
}
