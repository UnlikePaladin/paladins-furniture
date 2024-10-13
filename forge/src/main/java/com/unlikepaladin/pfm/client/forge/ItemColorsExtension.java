package com.unlikepaladin.pfm.client.forge;

import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IRegistryDelegate;

import java.util.Map;

public interface ItemColorsExtension {
    Map<IRegistryDelegate<Item>, ItemColorProvider> getColorMap();
}
