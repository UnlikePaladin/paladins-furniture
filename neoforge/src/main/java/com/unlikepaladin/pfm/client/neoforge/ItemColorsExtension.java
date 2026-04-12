package com.unlikepaladin.pfm.client.neoforge;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.core.Holder;

import java.util.Map;

public interface ItemColorsExtension {
    Map<Item, ItemColor> getColorMap();
}
