package com.unlikepaladin.pfm.mixin;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(PoiType.class)
public interface PFMPointOfInterestTypeAccessor {
    @Accessor("matchingStates")
    Set<BlockState> getBlockStates();

    @Mutable
    @Accessor("matchingStates")
    void setBlockStates(Set<BlockState> states);
}
