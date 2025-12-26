package com.unlikepaladin.pfm.mixin.forge;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.forge.PFMExtraModelsForge;
import com.unlikepaladin.pfm.client.forge.PaladinFurnitureModClientForge;
import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.ErrorCollectingSpriteGetter;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.util.Identifier;
import net.minecraft.util.thread.AsyncHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ModelBaker.class)
public class PFMModelBakerMixin {

    @ModifyReturnValue(method = "bake", at = @At("RETURN"))
    private CompletableFuture<ModelBaker.BakedModels> withExtraModels(CompletableFuture<ModelBaker.BakedModels> models, @Local Executor executor, @Local ModelBaker.BakerImpl baker) {
        CompletableFuture<Map<Identifier, BlockStateModel>> extraModels = AsyncHelper.mapValues(PFMExtraModelsForge.unbakedModels, (key, model) -> {
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
