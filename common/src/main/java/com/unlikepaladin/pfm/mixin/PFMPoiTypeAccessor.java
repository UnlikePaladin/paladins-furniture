package com.unlikepaladin.pfm.mixin;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Set;

@Mixin(PoiType.class)
public interface PFMPoiTypeAccessor {
    @Accessor("TYPE_BY_STATE")
    static Map<BlockState, PoiType> getBlockStateToPointOfInterestType() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("ALL_STATES")
    static void setRegisteredStates(Set<BlockState> states) {
        throw new AssertionError();
    }

    @Accessor("matchingStates")
    Set<BlockState> getMatchingStates();

    @Mutable
    @Accessor("matchingStates")
    void setMatchingStates(Set<BlockState> states);
}
