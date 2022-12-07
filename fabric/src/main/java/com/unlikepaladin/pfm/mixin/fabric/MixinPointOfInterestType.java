package com.unlikepaladin.pfm.mixin.fabric;

import com.google.common.collect.ImmutableSet;
import com.unlikepaladin.pfm.blocks.SimpleBed;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Set;


@Mixin(PointOfInterestTypes.class)
public abstract class MixinPointOfInterestType {
    @ModifyArgs(
            method = "registerAndGetDefault(Lnet/minecraft/registry/Registry;)Lnet/minecraft/world/poi/PointOfInterestType;",
            slice = @Slice(from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/poi/PointOfInterestTypes;HOME:Lnet/minecraft/registry/RegistryKey;"
            )),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/poi/PointOfInterestTypes;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/util/Set;II)Lnet/minecraft/world/poi/PointOfInterestType;",
                    ordinal = 0
            )
    )
    private static void appendBeds(Args args) {
        Set<BlockState> originalBedStates = args.get(2);
        Set<BlockState> addedBedStates = Arrays.stream(PaladinFurnitureModBlocksItems.getBeds()).flatMap(block -> block.getStateManager().getStates().stream().filter(state -> state.get(SimpleBed.PART) == BedPart.HEAD)).collect(ImmutableSet.toImmutableSet());
        Set<BlockState> newBedStates = new HashSet<>();
        newBedStates.addAll(originalBedStates);
        newBedStates.addAll(addedBedStates);
        newBedStates = newBedStates.stream().collect(ImmutableSet.toImmutableSet());
        // Add new blockStates
        args.set(2, newBedStates);
        // Set ticketCount
        args.set(3, 1);

    }
}