package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Set;

@Mixin(PointOfInterestTypes.class)
public abstract class PFMMixinPointOfInterestType {
    @ModifyArg(
            method = "registerAndGetDefault(Lnet/minecraft/registry/Registry;)Lnet/minecraft/world/poi/PointOfInterestType;",
            slice = @Slice(from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/poi/PointOfInterestTypes;HOME:Lnet/minecraft/registry/RegistryKey;"
            )),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/poi/PointOfInterestTypes;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/util/Set;II)Lnet/minecraft/world/poi/PointOfInterestType;",
                    ordinal = 0
            ), index = 2
    )
    private static Set<BlockState> appendBeds(Set<BlockState> states) {
        PaladinFurnitureModBlocksItems.originalHomePOIBedStates.addAll(states);
        return states;
    }
}