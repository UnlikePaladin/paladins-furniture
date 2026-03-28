package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.client.forge.ItemColorsExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(ItemColors.class)
public class ItemColorsAccessor implements ItemColorsExtension {

    @Shadow
    private final Map<Holder.Reference<Item>, ItemColor> itemColors = new HashMap<>();

    @Override
    public Map<Holder.Reference<Item>, ItemColor> getColorMap() {
        return itemColors;
    }
}
