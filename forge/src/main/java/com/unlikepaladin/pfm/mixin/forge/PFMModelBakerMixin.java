package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.render.model.ErrorCollectingSpriteGetter;
import net.minecraft.client.render.model.ModelBaker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ModelBaker.class)
public class PFMModelBakerMixin {
    @Inject(method = "bake", at = @At("RETURN"))
    private void onReturnBake(ErrorCollectingSpriteGetter spriteGetter, Executor executor, CallbackInfoReturnable<CompletableFuture<ModelBaker.BakedModels>> cir) {

    }
}
