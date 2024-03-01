package com.unlikepaladin.pfm.mixin.neoforge;

import net.minecraft.item.Item;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemEntry.class)
public interface PFMItemEntryAccessor {
    @Accessor("item")
    RegistryEntry<Item> getItem();
}
