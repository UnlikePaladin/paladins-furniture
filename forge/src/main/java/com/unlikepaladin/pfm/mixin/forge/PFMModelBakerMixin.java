package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.client.forge.PaladinFurnitureModClientForge;
import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ModelBaker.class)
public class PFMModelBakerMixin {
    @Inject(method = "bake", at = @At("RETURN"))
    private void onReturnBake(ModelBaker.ErrorCollectingSpriteGetter spriteGetter, CallbackInfoReturnable<ModelBaker.BakedModels> cir) {
        ModelBaker.BakedModels models = cir.getReturnValue();
        Map<Identifier, BakedModel> extraModels = new HashMap<>();
        PaladinFurnitureModClientForge.registerExtraModels(modelIdentifier -> {
            BakedModel model = ((ModelBaker) (Object) this).new BakerImpl(spriteGetter, modelIdentifier::toString)
                    .bake(modelIdentifier, ModelRotation.X0_Y0);
            extraModels.put(modelIdentifier, model);
        });

        ((PFModelBakerBakedExtensions) (Object) models).pfm_setExtraModels(extraModels);
    }
}
