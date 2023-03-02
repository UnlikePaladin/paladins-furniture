package com.unlikepaladin.pfm.mixin;

import com.google.common.collect.ImmutableSet;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Mixin(PointOfInterestType.class)
public abstract class MixinPointOfInterestType {
    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(
                    value = "CONSTANT",
                    args = "stringValue=home")
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/poi/PointOfInterestType;register(Ljava/lang/String;Ljava/util/Set;II)Lnet/minecraft/world/poi/PointOfInterestType;",
                    ordinal = 0
            ),
            index = 1
    )

    private static Set<BlockState> appendBeds(Set<BlockState> workStationStates) {
        Set<BlockState> addedBedStates = Arrays.stream(PaladinFurnitureModBlocksItems.getBeds()).flatMap(block -> block.getStateManager().getStates().stream().filter(state -> state.get(SimpleBedBlock.PART) == BedPart.HEAD)).collect(ImmutableSet.toImmutableSet());
        Set<BlockState> newBedStates = new HashSet<>();
        newBedStates.addAll(workStationStates);
        newBedStates.addAll(addedBedStates);
        newBedStates = newBedStates.stream().collect(ImmutableSet.toImmutableSet());

        return newBedStates;
    }
}