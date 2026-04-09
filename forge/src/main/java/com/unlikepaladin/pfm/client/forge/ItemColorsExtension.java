package com.unlikepaladin.pfm.client.forge;

import net.minecraft.core.Holder;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;

import java.util.Map;

public interface ItemColorsExtension {
    Map<Holder.Reference<Item>, ItemColor> getColorMap();
}
