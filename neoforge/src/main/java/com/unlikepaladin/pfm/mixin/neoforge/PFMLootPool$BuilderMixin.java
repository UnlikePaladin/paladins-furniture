package com.unlikepaladin.pfm.mixin.neoforge;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LootPool.Builder.class)
public abstract class PFMLootPool$BuilderMixin {
    @Shadow @Final private ImmutableList.Builder<LootPoolEntryContainer> entries;

    @Shadow public abstract LootPool.Builder name(String name);

    @Inject(method = "build", at = @At("HEAD"))
    private void setPFMName(CallbackInfoReturnable<LootTable> cir) {
        List<LootPoolEntryContainer> entryList = entries.build();
        if ( PFMGenerator.isDataRunning() && !entryList.isEmpty() && entryList.get(0).getType().equals(LootPoolEntries.ITEM)) {
            PFMLootItemAccessor entry = (PFMLootItemAccessor) entryList.get(0);
            name(BuiltInRegistries.ITEM.getKey(entry.getItem().value()).getPath());
        }
    }
}
