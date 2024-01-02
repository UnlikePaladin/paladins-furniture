package com.unlikepaladin.pfm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Set;

@Mixin(PointOfInterestTypes.class)
public interface PFMPointOfInterestTypesAccessor {
    @Accessor("POI_STATES_TO_TYPE")
    static Map<BlockState, RegistryEntry<PointOfInterestType>> getBlockStateToPointOfInterestType() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("POI_STATES")
    static void setRegisteredStates(Set<BlockState> states) {
        throw new AssertionError();
    }


    @Mutable
    @Accessor("HOME")
    static void setHome(RegistryKey<PointOfInterestType> home) {
        throw new AssertionError();
    }

}
