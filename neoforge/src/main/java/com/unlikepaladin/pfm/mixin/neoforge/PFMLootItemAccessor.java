package com.unlikepaladin.pfm.mixin.neoforge;

import net.minecraft.world.item.Item;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootItem.class)
public interface PFMLootItemAccessor {
    @Accessor("item")
    Holder<Item> getItem();
}
