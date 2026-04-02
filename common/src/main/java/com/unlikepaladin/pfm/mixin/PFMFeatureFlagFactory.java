package com.unlikepaladin.pfm.mixin;


import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagUniverse;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FeatureFlag.class)
public interface PFMFeatureFlagFactory {
    @Invoker("<init>")
    static FeatureFlag newFlag(FeatureFlagUniverse universe, int id){
        throw new AssertionError();
    }
}
