package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockModelShaper.class)
public abstract class PFMBlockModelShaperMixin {

    @Shadow
    public abstract BlockStateModel getBlockModel(BlockState arg);

    @Inject(method = "getParticleIcon", at = @At("HEAD"), cancellable = true)
    public void setCustomModelParticle(BlockState state, CallbackInfoReturnable<TextureAtlasSprite> cir) {
        BlockStateModel model = this.getBlockModel(state);
        if (model instanceof PFMBakedModelParticleExtension) {
            cir.setReturnValue(((PFMBakedModelParticleExtension) model).pfm$getParticle(state));
        }
    }
}
