package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.client.forge.PaladinFurnitureModClientForge;
import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ModelBakery.class)
public class PFMModelBakerMixin {
    @Inject(method = "bakeModels", at = @At("RETURN"))
    private void onReturnBake(ModelBakery.TextureGetter spriteGetter, CallbackInfoReturnable<ModelBakery.BakingResult> cir) {
        ModelBakery.BakingResult models = cir.getReturnValue();
        Map<ResourceLocation, BakedModel> extraModels = new HashMap<>();
        PaladinFurnitureModClientForge.registerExtraModels(modelIdentifier -> {
            BakedModel model = ((ModelBakery) (Object) this).new ModelBakerImpl(spriteGetter, modelIdentifier::toString)
                    .bake(modelIdentifier, BlockModelRotation.X0_Y0);
            extraModels.put(modelIdentifier, model);
        });

        ((PFModelBakerBakedExtensions) (Object) models).pfm_setExtraModels(extraModels);
    }
}
