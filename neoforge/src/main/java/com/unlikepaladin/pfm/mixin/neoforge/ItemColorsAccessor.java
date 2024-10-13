package com.unlikepaladin.pfm.mixin.neoforge;

import com.unlikepaladin.pfm.client.neoforge.ItemColorsExtension;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(ItemColors.class)
public class ItemColorsAccessor implements ItemColorsExtension {

    @Shadow
    private final Map<Item, ItemColorProvider> providers = new HashMap<>();

    @Override
    public Map<Item, ItemColorProvider> getColorMap() {
        return providers;
    }
}
