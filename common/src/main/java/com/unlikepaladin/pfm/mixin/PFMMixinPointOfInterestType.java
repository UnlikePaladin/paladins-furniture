package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.HashSet;
import java.util.Set;

@Mixin(value = PointOfInterestType.class, priority = 3000)
public class PFMMixinPointOfInterestType {
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
        PaladinFurnitureModBlocksItems.originalHomePOIBedStates.addAll(workStationStates);
        return new HashSet<>();
    }
}