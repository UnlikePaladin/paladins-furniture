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
    @Accessor("BLOCK_STATE_TO_POINT_OF_INTEREST_TYPE")
    static Map<BlockState, PointOfInterestType> getBlockStateToPointOfInterestType() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("REGISTERED_STATES")
    static void setRegisteredStates(Set<BlockState> states) {
        throw new AssertionError();
    }
}
