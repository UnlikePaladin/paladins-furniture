package com.unlikepaladin.pfm.mixin.neoforge;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootPoolEntryTypes;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LootPool.Builder.class)
public abstract class PFMLootPool$BuilderMixin {
    @Shadow @Final private ImmutableList.Builder<LootPoolEntry> entries;

    @Shadow public abstract LootPool.Builder name(String name);

    @Inject(method = "build", at = @At("HEAD"))
    private void setPFMName(CallbackInfoReturnable<LootTable> cir) {
        List<LootPoolEntry> entryList = entries.build();
        if ( PFMGenerator.isDataRunning() && !entryList.isEmpty() && entryList.get(0).getType().equals(LootPoolEntryTypes.ITEM)) {
            PFMItemEntryAccessor entry = (PFMItemEntryAccessor) entryList.get(0);
            name(Registries.ITEM.getId(entry.getItem().value()).getPath());
        }
    }
}
