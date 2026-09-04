package com.unlikepaladin.pfm.mixin.forge;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.forge.PFMExtraModelsForge;
import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;
import net.minecraft.util.thread.ParallelMapTransform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ModelBakery.class)
public class PFMModelBakerMixin {

    @ModifyReturnValue(method = "bakeModels", at = @At("RETURN"))
    private CompletableFuture<ModelBakery.BakingResult> withExtraModels(CompletableFuture<ModelBakery.BakingResult> models, @Local Executor executor, @Local ModelBakery.ModelBakerImpl baker) {
        CompletableFuture<Map<Identifier, BlockStateModel>> extraModels = ParallelMapTransform.schedule(PFMExtraModelsForge.unbakedModels, (key, model) -> {
            try {
                return model.bake(baker);
            } catch (Exception e) {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Unable to bake extra model: '{}'", key, e);
                return null;
            }
        }, executor);

        return models.thenCombine(extraModels, (res, extra) -> {
            ((PFModelBakerBakedExtensions) (Object) res).pfm_setExtraModels(extra);
            return res;
        });
    }
}
