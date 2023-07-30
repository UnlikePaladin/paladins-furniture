package com.unlikepaladin.pfm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;


@Mixin(value = PointOfInterestType.class)
public interface PFMMixinPointOfInterestTypeFactory {
}