package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockModels.class)
public abstract class PFMBlockModelsMixin {
    @Shadow public abstract BakedModel getModel(BlockState state);

    @Inject(method = "getSprite", at = @At("HEAD"), cancellable = true)
    public void setCustomModelParticle(BlockState state, CallbackInfoReturnable<Sprite> cir) {
        BakedModel model = this.getModel(state);
        if (model instanceof PFMBakedModelParticleExtension) {
            cir.setReturnValue(((PFMBakedModelParticleExtension) model).pfm$getParticle(state));
        }
    }
}
