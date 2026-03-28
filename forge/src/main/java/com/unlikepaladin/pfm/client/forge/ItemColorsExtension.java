package com.unlikepaladin.pfm.client.forge;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IRegistryDelegate;

import java.util.Map;

public interface ItemColorsExtension {
    Map<RegistryEntry.Reference<Item>, ItemColor> getColorMap();
}
