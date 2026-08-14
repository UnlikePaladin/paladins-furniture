package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.sprite.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateModelSet.class)
public abstract class PFMBlockModelShaperMixin {

    @Shadow
    public abstract BlockStateModel get(BlockState state);

    @Inject(method = "getParticleMaterial", at = @At("HEAD"), cancellable = true)
    public void setCustomModelParticle(BlockState state, CallbackInfoReturnable<Material.Baked> cir) {
        BlockStateModel model = this.get(state);
        if (model instanceof PFMBakedModelParticleExtension extension) {
            cir.setReturnValue(new Material.Baked(extension.pfm$getParticle(state), false));
        }
    }
}
