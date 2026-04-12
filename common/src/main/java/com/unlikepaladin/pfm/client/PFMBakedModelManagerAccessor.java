package com.unlikepaladin.pfm.client;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;

public interface PFMBakedModelManagerAccessor {
    BakedModel pfm$getModelFromNormalID(ResourceLocation id);
}
