package com.unlikepaladin.pfm.mixin.neoforge;

import com.unlikepaladin.pfm.client.neoforge.ItemColorsExtension;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.Item;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(ItemColors.class)
public class ItemColorsAccessor implements ItemColorsExtension {

    @Shadow
    private final Map<Item, ItemColor> itemColors = new HashMap<>();

    @Override
    public Map<Item, ItemColor> getColorMap() {
        return itemColors;
    }
}
