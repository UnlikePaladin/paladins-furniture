package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.client.PFMBakedModelManagerAccessor;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ModelManager.class)
public abstract class PFMModelManagerMixin implements PFMBakedModelManagerAccessor {
    @Shadow
    private Map<ResourceLocation, BakedModel> bakedRegistry;

    @Shadow private BakedModel missingModel;

    @Override
    public BakedModel pfm$getModelFromNormalID(ResourceLocation id) {
        return bakedRegistry.getOrDefault(id, missingModel);
    }
}
