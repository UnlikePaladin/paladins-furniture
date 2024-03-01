package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.runtime.PFMGenerator;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootPoolEntryTypes;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LootPool.Builder.class)
public abstract class PFMLootPool$BuilderMixin {
    @Shadow @Final private List<LootPoolEntry> entries;

    @Shadow public abstract LootPool.Builder name(String name);

    @Inject(method = "build", at = @At("HEAD"))
    private void setPFMName(CallbackInfoReturnable<LootTable> cir) {
        if ( PFMGenerator.isDataRunning() && !entries.isEmpty() && entries.get(0).getType().equals(LootPoolEntryTypes.ITEM)) {
            PFMItemEntryAccessor entry = (PFMItemEntryAccessor) entries.get(0);
            name(ForgeRegistries.ITEMS.getKey(entry.getItem()).getPath());
        }
    }
}
