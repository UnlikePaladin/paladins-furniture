package com.unlikepaladin.pfm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Set;

@Mixin(PointOfInterestType.class)
public interface PFMPointOfInterestTypeAccessor {
    @Accessor("blockStates")
    Set<BlockState> getBlockStates();

    @Mutable
    @Accessor("blockStates")
    void setBlockStates(Set<BlockState> states);
}
