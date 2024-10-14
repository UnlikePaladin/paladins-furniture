package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.client.forge.ItemColorsExtension;
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
    private final Map<RegistryEntry.Reference<Item>, ItemColorProvider> providers = new HashMap<>();

    @Override
    public Map<RegistryEntry.Reference<Item>, ItemColorProvider> getColorMap() {
        return providers;
    }
}
