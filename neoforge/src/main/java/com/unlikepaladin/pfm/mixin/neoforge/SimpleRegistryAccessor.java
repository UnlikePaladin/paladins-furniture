package com.unlikepaladin.pfm.mixin.neoforge;

import net.minecraft.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleRegistry.class)
public interface SimpleRegistryAccessor {
    @Accessor("frozen")
    boolean isFrozen();
}
