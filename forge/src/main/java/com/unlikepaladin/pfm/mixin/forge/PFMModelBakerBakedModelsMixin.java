package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
@Mixin(ModelBakery.BakingResult.class)
abstract class PFMModelBakerBakedModelsMixin implements PFModelBakerBakedExtensions {
    @Unique
    @Nullable
    private Map<ResourceLocation, BakedModel> pfm$extraModels;

    @Override
    @Nullable
    public Map<ResourceLocation, BakedModel> pfm_getExtraModels() {
        return pfm$extraModels;
    }

    @Override
    public void pfm_setExtraModels(@Nullable Map<ResourceLocation, BakedModel> extraModels) {
        this.pfm$extraModels = extraModels;
    }

}
