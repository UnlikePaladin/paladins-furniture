package com.unlikepaladin.pfm.mixin.forge;

import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.ducks.forge.PFMBakedModelManagerExtensions;
import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelManager.class)
public class PFMBakedModelManagerMixin implements PFMBakedModelManagerExtensions {
    @Shadow
    private BakedModel missingModel;
    @Unique
    @Nullable
    private Map<ResourceLocation, BakedModel> pfm$extraModels;

    @Inject(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 0))
    private void onUpload(CallbackInfo ci, @Local ModelBakery.BakingResult bakedModels) {
        pfm$extraModels = ((PFModelBakerBakedExtensions) (Object) bakedModels).pfm_getExtraModels();
    }

    @Override
    public BakedModel pfm_getModel(ResourceLocation id) {
        if (pfm$extraModels == null) {
            return missingModel;
        }
        return pfm$extraModels.getOrDefault(id, missingModel);
    }
}
