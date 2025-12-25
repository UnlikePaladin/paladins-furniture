package com.unlikepaladin.pfm.mixin.forge;

import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.ducks.forge.PFMBakedModelManagerExtensions;
import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BakedModelManager.class)
public class PFMBakedModelManagerMixin implements PFMBakedModelManagerExtensions {
    @Shadow
    private BakedModel missingBlockModel;
    @Unique
    @Nullable
    private Map<Identifier, BakedModel> pfm$extraModels;

    @Inject(method = "upload", at = @At(value = "INVOKE", target = "net/minecraft/util/profiler/Profiler.swap(Ljava/lang/String;)V", ordinal = 0))
    private void onUpload(CallbackInfo ci, @Local ModelBaker.BakedModels bakedModels) {
        pfm$extraModels = ((PFModelBakerBakedExtensions) (Object) bakedModels).pfm_getExtraModels();
    }

    @Override
    public BakedModel pfm_getModel(Identifier id) {
        if (pfm$extraModels == null) {
            return missingBlockModel;
        }
        return pfm$extraModels.getOrDefault(id, missingBlockModel);
    }
}
