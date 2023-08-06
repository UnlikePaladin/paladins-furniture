package com.unlikepaladin.pfm.mixin;

import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureUniverse;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FeatureFlag.class)
public interface PFMFeatureFlagFactory {
    @Invoker("<init>")
    static FeatureFlag newFlag(FeatureUniverse universe, int id){
        throw new AssertionError();
    }
}
